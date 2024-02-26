package com.example.shoppingMall.controller;

import com.example.shoppingMall.jwt.JwtRequestDto;
import com.example.shoppingMall.jwt.UserDetailsManagerImpl;
import com.example.shoppingMall.user.dto.BusinessConfirmDto;
import com.example.shoppingMall.user.dto.UserDto;
import com.example.shoppingMall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 유저, 중고물품
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
public class UserController {
    private final UserService userService;
    private final UserDetailsManagerImpl manager;

    // 회원가입
    @PostMapping("/create-user")
    public UserDto create(
            @Valid @RequestBody UserDto dto
    ){
        return userService.createUser(dto);
    }



    // 일반 사용자로 등업
    @PutMapping("/upgrade-regular/{id}")
    public UserDto upgradeRegular(
            @PathVariable("id") Long id,
            @RequestBody UserDto dto
    ){
        return userService.upgradeRegular(id,dto);
    }

    @GetMapping("/req-auth")
    public String reqAuth(){
        return "done";
    }

    // 사업자 신청
    @PutMapping("/upgrade-business/{id}")
    public UserDto upgradeBusiness(
            @PathVariable("id") Long id,
            @RequestParam String businessNumber
    ){
        return userService.upgradeBusiness(id,businessNumber);
    }

    // 사업자 신청목록 찾기
    @GetMapping("/find-apply")
    public List<BusinessConfirmDto> findAllByGrade(
    ){
        return userService.findAllApply();
    }

    // 사업자 신청 승인
    @PutMapping("/confirm-business/{id}")
    public UserDto confirmBusiness(
            @PathVariable("id") Long id
    ){
        return userService.confirmBusiness(id);
    }

    // 사업자 신청 거절
    @PutMapping("/reject-business/{id}")
    public UserDto rejectBusiness(
            @PathVariable("id") Long id
    ){
        return userService.rejectBusiness(id);
    }
}
