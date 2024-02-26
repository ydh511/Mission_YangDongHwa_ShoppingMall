package com.example.shoppingMall.user.dto;

import com.example.shoppingMall.user.entity.User;
import jakarta.validation.constraints.Email;
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
public class UserDto {
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$",
            message = "아이디는 영문, 숫자만 가능하며 4~10자리까지 가능합니다." )
    private String userId;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$",
            message = "비밀번호는 영문, 숫자만 가능하며 1~16자리까지 가능합니다." )
    private String password;

    //@Email
    //잠시만 꺼두기
    private String email;
    private String nickname;
    private String personName;
    private Integer age;

    private String phone;
    private Integer businessGrade;
    private String businessNumber;
    private String roadAddress;

    public static UserDto fromEntity(User entity){
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .personName(entity.getPersonName())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .businessGrade(entity.getBusinessGrade())
                .businessNumber(entity.getBusinessNumber())
                .roadAddress(entity.getRoadAddress())
                .build();
    }
}
