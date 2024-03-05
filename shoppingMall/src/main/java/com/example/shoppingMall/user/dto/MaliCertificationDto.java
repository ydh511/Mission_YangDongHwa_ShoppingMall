package com.example.shoppingMall.user.dto;

import com.example.shoppingMall.user.entity.MailCerification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaliCertificationDto {
    private Long id;
    private Long userId;
    private String code;

    public static MaliCertificationDto fromEntity(MailCerification entity){
        return MaliCertificationDto.builder()
                .userId(entity.getUserId())
                .code(entity.getCode())
                .build();
    }
}
