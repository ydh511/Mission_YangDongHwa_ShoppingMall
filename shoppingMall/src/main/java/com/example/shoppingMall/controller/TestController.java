package com.example.shoppingMall.controller;

import com.example.shoppingMall.config.SecurityUtils;
import com.example.shoppingMall.service.UserDetailsServiceImpl;
import com.example.shoppingMall.user.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    private final UserDetailsServiceImpl userService;
    private final SecurityUtils jwtSecurityUtils;

    // 테스트용으로 loadByUsername해봄
    @GetMapping("/load")
    public UserDetails checkDetails(
            @RequestParam("userId")
            String userId
    ){
        return userService.loadUserByUsername(userId);
    }
    // 테스트용으로 jwt secret 가져옴
    @GetMapping("/get-secret")
    public String getSecret(){
        return jwtSecurityUtils.JwtSecret();
    }

    // 테스트용으로 claim정보 가져옴
    @GetMapping("/get-subject")
    public String getSubject(
            @RequestParam("token")
            String token
    ){
        return jwtSecurityUtils.getSubject(token);
    }
    //테스트용으로 유저id 받아서 토큰의 subject와 userId가 같은지 비교용
    @GetMapping("/compare-userid")
    public String compareToUserId(
            @RequestParam("id")
            Long id,
            @RequestParam("token")
            String token
    ){
        boolean checkId = jwtSecurityUtils.checkUserFromToken(token, id);
        return checkId?"true":"false";
    }

    @GetMapping("/validate")
    public Claims validateToken(
            @RequestParam("token")
            String token
    ) {
        if (!jwtSecurityUtils.validate(token))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return jwtSecurityUtils.parseClaims(token);
    }

    @GetMapping("/auth")
    public CustomUserDetails auth(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return customUserDetails;
    }
}
