package com.example.shoppingMall.service;

import com.example.shoppingMall.fleeMarket.dto.UsedItemDto;
import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import com.example.shoppingMall.shoppingMall.entity.ShoppingMall;
import com.example.shoppingMall.shoppingMall.repository.ShoppingMallRepo;
import com.example.shoppingMall.user.dto.CustomUserDetails;
import com.example.shoppingMall.user.dto.BusinessConfirmDto;
import com.example.shoppingMall.user.dto.UserDto;
import com.example.shoppingMall.user.dto.UserRequestDto;
import com.example.shoppingMall.user.entity.BusinessConfirm;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.BusinessConfirmRepo;
import com.example.shoppingMall.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final BusinessConfirmRepo businessRepo;
    private final ShoppingMallRepo shopRepo;

    @Override
    // 인증을 처리할때 사용하는 메서드
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.:" + username));

        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthority())
                .build();
    }
    // 유저가 존재하는지
    public boolean userExists(String userId){
        return userRepo.existsByUsername(userId);
    }

    // 인증과정에서 받아온 토큰의 username과 받아온id의 username를 비교
    public boolean matchUser(Long id, String matchName){
        User user = userRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User is not exist"));
        return(user.getUsername().equals(matchName));
    }

    // 사용자 회원가입, 비로그인시 가능
    public UserDto createUser(UserRequestDto dto){
        if(userRepo.existsByUsername(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .authority(dto.getAuthority())
                .build();
        return UserDto.fromEntity(userRepo.save(user));
    }

    // 모든 회원 검색, 관리자만 가능
    public List<UserDto> findAllUser(){
        List<User> users = userRepo.findAll();
        if (users.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(UserDto.fromEntity(user));
        }
        return userDtoList;
    }

    //회원 하나 검색, 관리자만 가능
    public UserDto findUser(Long id){
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return UserDto.fromEntity(user);
    }
    // 비활성 유저정보 수정, 해당 비활성 유저만 가능, 아이디는 바꿀 수 없다
    public UserDto updateInactive(Long id, UserDto dto){
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.updateInactiveUser(dto.getPassword());
        return UserDto.fromEntity(userRepo.save(user));
    }

    //활성 유저정보 수정, 해당 활성 유저만 가능
    public UserDto updateActive(Long id, UserDto dto){
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //활성유저로서 필요한 자료들이 다 있는지 확인
        if((!dto.getNickname().isEmpty()) & (!dto.getEmail().isEmpty()) &
                (!dto.getPersonName().isEmpty()) & (dto.getAge() >= 0) & (!dto.getPhone().isEmpty())){
            user.updateActiveUser(dto.getPassword(), dto.getEmail(),dto.getNickname(),
                    dto.getPersonName(), dto.getAge(), dto.getPhone(), dto.getRoadAddress());
            return UserDto.fromEntity(userRepo.save(user));
        }
        else throw new IllegalArgumentException("All args need");
    }

    // 일반 사용자로 등업, 해당 유저만 가능
    public UserDto upgradeRegular(Long id,UserDto dto){
        User user = userRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User is not exist"));

        //일반 사용자로 등업하기 필요한 자료들을 다 입력했는지
        if((!dto.getNickname().isEmpty()) & (!dto.getEmail().isEmpty()) &
                (!dto.getPersonName().isEmpty()) & (dto.getAge() >= 0) & (!dto.getPhone().isEmpty())){
            user.setEmail(dto.getEmail());
            user.setNickname(dto.getNickname());
            user.setPersonName(dto.getPersonName());
            user.setAge(dto.getAge());
            user.setPhone(dto.getPhone());
            user.setAuthority("ROLE_ACTIVE");
            return UserDto.fromEntity(userRepo.save(user));
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 사업자로 신청, 해당 유저만 가능
    public UserDto upgradeBusiness(Long id, String businessNumber){
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setBusinessNumber(businessNumber);
        //최근 신청 건 중에 신청 대기중이 있는지
        if(businessRepo.findTopByUserId(id).getStatus() == 1){
            throw new IllegalArgumentException("You have already applied");
        }
        BusinessConfirm businessConfirm = BusinessConfirm.builder()
                .userId(user.getId())
                .status(1)
                .build();
        businessRepo.save(businessConfirm);
        return  UserDto.fromEntity(userRepo.save(user));
    }

    // 사업자 신청자 보기, 관리자만 가능
    public List<BusinessConfirmDto> findAllApply(){
        List<BusinessConfirm> applyList= businessRepo.findAllByStatus(1);
        if (applyList.isEmpty())
            throw new IllegalArgumentException("Application is not exist");

        List<BusinessConfirmDto>applyDtoList = new ArrayList<>();
        for (BusinessConfirm  businessConfirm : applyList){
            applyDtoList.add(BusinessConfirmDto.fromEntity(businessConfirm));
        }
        return applyDtoList;
    }

    // 사업자 신청 승인, 관리자만 가능
    public UserDto confirmBusiness(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));
        BusinessConfirm businessConfirm = businessRepo.findByUserId(id);
        //사용자의 신청을 승인
        businessConfirm.setStatus(2);

        //사용자의 등급을 사업자로 등업
        user.setAuthority("ROLE_BUSINESS");
        businessRepo.save(businessConfirm);
        // 준비중인 쇼핑몰 추가해주기
        shopRepo.save(ShoppingMall.builder()
                .userId(user.getId())
                .shopStatus(1)
                .build());

        return UserDto.fromEntity(userRepo.save(user));
    }

    // 사업자 신청 거절, 관리자만 가능
    public UserDto rejectBusiness(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));
        BusinessConfirm businessConfirm = businessRepo.findByUserId(id);
        //사용자의 신청을 거절
        businessConfirm.setStatus(0);
        businessRepo.save(businessConfirm);
        return UserDto.fromEntity(userRepo.save(user));
    }

}
