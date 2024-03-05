package com.example.shoppingMall.user.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDto {
    private String token;
}
