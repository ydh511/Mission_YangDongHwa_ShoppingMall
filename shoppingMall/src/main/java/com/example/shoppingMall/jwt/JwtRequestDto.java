package com.example.shoppingMall.jwt;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDto {
    private String userId;
    private String password;
}
