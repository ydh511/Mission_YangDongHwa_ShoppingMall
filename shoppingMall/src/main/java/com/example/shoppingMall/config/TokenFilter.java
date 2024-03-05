package com.example.shoppingMall.config;

import com.example.shoppingMall.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TokenFilter extends OncePerRequestFilter {
    private final SecurityUtils SecurityUtils;
    private final UserDetailsServiceImpl service;

    public TokenFilter(
            SecurityUtils SecurityUtils,
            UserDetailsServiceImpl service
    ) {
        this.SecurityUtils = SecurityUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (SecurityUtils.validate(token)){
                SecurityContext context
                        = SecurityContextHolder.createEmptyContext();
                // Claims에 저장된 사용자 이름을 회수
                String username = SecurityUtils
                        .parseClaims(token)
                        .getSubject();
                UserDetails userDetails = service.loadUserByUsername(username);
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    log.info("authority: {}", authority.getAuthority());
                }
                AbstractAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        token,
                        userDetails.getAuthorities());
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                log.info("set security context with jwt");
            }
            else {
                log.warn("jwt validation failed");
            }
        }
        filterChain.doFilter(request, response);
    }

}
