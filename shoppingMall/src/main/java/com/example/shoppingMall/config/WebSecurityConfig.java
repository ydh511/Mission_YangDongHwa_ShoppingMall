package com.example.shoppingMall.config;

import com.example.shoppingMall.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final SecurityUtils securityUtils;
    private final UserDetailsServiceImpl service;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception{
        http    // csrf 보안 해제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른요청 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // 테스트용
                                "/test/get-secret",
                                "/test/compare-userid",
                                "/test/validate",
                                "/test/get-subject",
                                "/test/auth",

                                // 중고물품, 쇼핑몰 볼때
                                "/flee-market/items",
                                "/flee-market/items/**",
                                "/shoppingmall/view-shopcate",
                                "/shoppingmall/view-shoplist",
                                "/shoppingmall/**/items",
                                "/shoppingmall/view-itemcate",
                                "/shoppingmall/view-itemcate/**",
                                "/shoppingmall/**/items/**"
                                )
                        .permitAll()

                        // 비로그인시 회원가입, 로그인만
                        .requestMatchers(
                                "/user/login",
                                "/user/req-auth"
                        )
                        .anonymous()

                        // 비활성 유저만 가능
                        .requestMatchers(
                                "/user/update-inactive/**",
                                "/user/upgrade-regular/**"
                        )
                        .hasRole("INACTIVE")

                        // 활성유저만 가능
                        .requestMatchers(
                                "/user/upgrade-business/**"
                        )
                        .hasRole("ACTIVE")

                        // 사업자, 활성유저만 가능
                        .requestMatchers(
                                "/user/update-active/**",
                                "/flee-market/create-item",
                                "/flee-market/update/**",
                                "/flee-market/delete/**",
                                "/flee-market/offer-item/**",
                                "/flee-market/find-item-offers/**",
                                "/flee-market/confirm-offer/**",
                                "/flee-market/confirm-item/**",
                                "/shoppingmall/offer/**",
                                "/shoppingmall/cancel-offer/**",
                                "/shoppingmall//cancel-itemoffer/**"
                        )
                        .hasAnyRole("ACTIVE", "BUSINESS")

                        // 사업자만 가능
                        .requestMatchers(
                                "/shoppingmall/opening-offer",
                                "/shoppingmall/**/close-offer",
                                "/shoppingmall/**/create-item",
                                "/shoppingmall/**/update-item/**",
                                "/shoppingmall/**/delete-item/**",
                                "/shoppingmall/offered-item/**",
                                "/shoppingmall/confirm-item/**",
                                "/shoppingmall/reject-item/**"
                        )
                        .hasRole("BUSINESS")

                        // 관리자만 가능
                        .requestMatchers(
                                "/user/view-all",
                                "/user/view/**",
                                "/user/find-apply",
                                "/user/confirm-business/**",
                                "/user/reject-business/**",
                                "/shoppingmall/view-offered-shop",
                                "/shoppingmall/cancel-offer/**",
                                "/shoppingmall/confirm-offer/**",
                                "/shoppingmall/**/confirm-close",
                                "/shoppingmall/view-close-offer"
                        )
                        .hasRole("ADMIN")
                        .anyRequest()
                        .hasRole("ADMIN"))
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new TokenFilter(
                                securityUtils,
                                service),
                        AuthorizationFilter.class
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}