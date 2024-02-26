package com.example.shoppingMall.service;

import com.example.shoppingMall.user.dto.BusinessConfirmDto;
import com.example.shoppingMall.user.dto.UserDto;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.entity.BusinessConfirm;
import com.example.shoppingMall.user.repository.BusinessConfirmRepo;
import com.example.shoppingMall.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final BusinessConfirmRepo businessRepo;

    // 사용자 회원가입
    public UserDto createUser(UserDto dto){
        if(userRepo.existsByUserId(dto.getUserId()))
            throw new IllegalArgumentException("Userid already exist");
        User user = User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .businessGrade(0)
                .build();

        return UserDto.fromEntity(userRepo.save(user));
    }

    // 일반 사용자로 등업
    public UserDto upgradeRegular(Long id,UserDto dto){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));

        //일반 사용자로 등업하기 필요한 자료들을 다 입력했는지
        if((!dto.getNickname().isEmpty()) & (!dto.getEmail().isEmpty()) &
                (!dto.getPersonName().isEmpty()) & (dto.getAge() >= 0) & (!dto.getPhone().isEmpty())){
            user.setEmail(dto.getEmail());
            user.setNickname(dto.getNickname());
            user.setPersonName(dto.getPersonName());
            user.setAge(dto.getAge());
            user.setPhone(dto.getPhone());
            user.setBusinessGrade(1);
            return UserDto.fromEntity(userRepo.save(user));
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 사업자로 신청
    public UserDto upgradeBusiness(Long id, String businessNumber){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));
        //일반 사용자 등급인지
        if(user.getBusinessGrade() == 1){
            user.setBusinessNumber(businessNumber);

            //최근 신청 건 중에 신청 대기중이 있는지
            if(businessRepo.findTopByUserId(id).getUserId() == 1){
                throw new IllegalArgumentException("You have already applied");
            }
            BusinessConfirm businessConfirm = BusinessConfirm.builder()
                    .userId(user.getId())
                    .status(1)
                    .build();
            businessRepo.save(businessConfirm);
        }
        else if(user.getBusinessGrade() == 0)
            throw new IllegalArgumentException("You have to upgrade regular grade");
        else if(user.getBusinessGrade() == 2)
            throw new IllegalArgumentException("You are already business grade");
        else if(user.getBusinessGrade() == 3)
            throw new IllegalArgumentException("You are Master");
        return  UserDto.fromEntity(userRepo.save(user));
    }

    // 사업자 신청자 보기
    public List<BusinessConfirmDto> findAllApply(){
        List<BusinessConfirm> BList = businessRepo.findAllByStatus(1);
        if (BList.isEmpty())
            throw new IllegalArgumentException("Application is not exist");

        List<BusinessConfirmDto>BDtoList = new ArrayList<>();
        for (BusinessConfirm  businessConfirm : BList){
            BDtoList.add(BusinessConfirmDto.fromEntity(businessConfirm));
        }
        return BDtoList;
    }

    //사업자 신청 승인
    public UserDto confirmBusiness(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));
        BusinessConfirm businessConfirm = businessRepo.findByUserId(id);
        //사용자의 신청을 승인
        businessConfirm.setStatus(2);
        //사용자의 등급을 사업자로 등업
        user.setBusinessGrade(2);
        businessRepo.save(businessConfirm);
        return UserDto.fromEntity(userRepo.save(user));
    }

    //사업자 신청 거절
    public UserDto rejectBusiness(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not exist"));
        BusinessConfirm businessConfirm = businessRepo.findByUserId(id);
        //사용자의 신청을 거절
        businessConfirm.setStatus(0);
        businessRepo.save(businessConfirm);
        return UserDto.fromEntity(userRepo.save(user));
    }
}
