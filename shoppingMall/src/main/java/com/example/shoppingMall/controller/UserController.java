package com.example.shoppingMall.controller;

import com.example.shoppingMall.service.MakeMarketService;
import com.example.shoppingMall.user.dto.*;
import com.example.shoppingMall.config.SecurityUtils;
import com.example.shoppingMall.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// 유저, 중고물품
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
public class UserController {
    private final UserDetailsServiceImpl userService;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;
    private final MakeMarketService makeMarketService;

    // 회원가입
    @PostMapping("/create-user")
    public UserDto create(
            @Valid @RequestBody UserRequestDto dto
    ){
        return userService.createUser(dto.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .authority("ROLE_INACTIVE")
                        .build());
    }

    // 로그인해서 토큰 발급
    @PostMapping("/login")
    public UserTokenDto login(@RequestBody UserRequestDto dto) {
        if (!userService.userExists(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails
                = userService.loadUserByUsername(dto.getUsername());
        log.info("name: {}",userDetails.getUsername());
        log.info("password: {}",userDetails.getPassword());
        // 평문 비밀번호와 암호화 비밀번호를 비교할 수 있다.
        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())){
            log.info("Password matches: {}",passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserTokenDto response = new UserTokenDto();
        response.setToken(securityUtils.generateToken(userDetails));
        return response;
    }
    // 모든 유저 검색, 관리자만 가능
    @GetMapping("/view-all")
    public List<UserDto> findAllUser(){
        return userService.findAllUser();
    }

    // 유저 하나 검색, 관리자만 가능
    @GetMapping("/view/{id}")
    public UserDto findUser(
            @PathVariable
            Long id
    ){
        return userService.findUser(id);
    }
    // 비활성 유저정보 수정, 해당 비활성 유저만 가능
    @PutMapping("/update-inactive/{id}")
    public UserDto updateInactive(
            @PathVariable("id")
            Long id,
            @RequestBody
            UserDto dto,
            Authentication authentication
    ){
        if(userService.matchUser(id, authentication.getName()))
            return userService.updateInactive(id, dto);
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    // 활성 유저정보 수정, 해당 활성 유저이상만 가능
    @PutMapping("/update-active/{id}")
    public UserDto updateActive(
            @PathVariable("id")
            Long id,
            @RequestBody
            UserDto dto,
            Authentication authentication
    ){
        if(userService.matchUser(id, authentication.getName()))
            return userService.updateActive(id, dto);
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    // 일반 사용자로 등업
    @PutMapping("/upgrade-regular/{id}")
    public UserDto upgradeRegular(
            @PathVariable("id") Long id,
            @RequestBody UserDto dto,
            Authentication authentication
    ){
        if(userService.matchUser(id, authentication.getName()))
            return userService.upgradeRegular(id,dto);
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    // 사업자 신청, 해당 유저만 가능
    @PutMapping("/upgrade-business/{id}")
    public UserDto upgradeBusiness(
            @PathVariable("id") Long id,
            @RequestParam String businessNumber,
            Authentication authentication
    ){
        if(userService.matchUser(id, authentication.getName()))
            return userService.upgradeBusiness(id,businessNumber);
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    // 사업자 신청목록 찾기, 관리자만 가능
    @GetMapping("/find-apply")
    public List<BusinessConfirmDto> findAllApply(
    ){
        return userService.findAllApply();
    }

    // 사업자 신청 승인, 관리자만 가능
    @PutMapping("/confirm-business/{id}")
    public UserDto confirmBusiness(
            @PathVariable("id") Long id
    ){
        return userService.confirmBusiness(id);
    }
    // 사업자 신청 거절, 관리자만 가능
    @PutMapping("/reject-business/{id}")
    public UserDto rejectBusiness(
            @PathVariable("id") Long id
    ){
        return userService.rejectBusiness(id);
    }
}
