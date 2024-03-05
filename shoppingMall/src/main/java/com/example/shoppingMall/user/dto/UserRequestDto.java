package com.example.shoppingMall.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$",
            message = "아이디는 영문, 숫자만 가능하며 4~10자리까지 가능합니다." )
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$",
            message = "비밀번호는 영문, 숫자만 가능하며 1~10자리까지 가능합니다." )
    private String password;
    private String authority;
}
