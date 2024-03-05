package com.example.shoppingMall.user.dto;

import com.example.shoppingMall.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

// 유저정보와 토큰과 중고거래를 담당
@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$",
            message = "아이디는 영문, 숫자만 가능하며 4~10자리까지 가능합니다." )
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$",
            message = "비밀번호는 영문, 숫자만 가능하며 1~16자리까지 가능합니다." )
    private String password;
    private boolean certification;

    //@Email
    //잠시만 꺼두기
    private String email;
    private String nickname;
    private String personName;
    private Integer age;

    private String phone;
    private String authority;
    private String businessNumber;
    private String roadAddress;

    public static UserDto fromEntity(User entity){
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .personName(entity.getPersonName())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .authority(entity.getAuthority())
                .businessNumber(entity.getBusinessNumber())
                .roadAddress(entity.getRoadAddress())
                .certification(entity.isCertification())
                .build();
    }
}
