package com.example.shoppingMall.jwt;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDto {
    private String token;
}
