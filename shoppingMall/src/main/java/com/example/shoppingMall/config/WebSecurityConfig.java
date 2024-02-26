package com.example.shoppingMall.config;

import com.example.shoppingMall.jwt.JwtTokenFilter;
import com.example.shoppingMall.jwt.JwtTokenUtils;
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

import java.security.Security;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception{
        http    // csrf 보안 해제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른요청 인가
                .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(
                                "/user/create-user"
                                )
                        .anonymous()
                        .requestMatchers(
                                "token/issue"
                        )
                        .permitAll()
                        .requestMatchers(
                                "/user/upgrade-regular/{id}",
                                "/user/req-auth"
                        )
                        .authenticated())
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtils),
                        AuthorizationFilter.class
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}