package com.example.shoppingMall.user.dto;

import com.example.shoppingMall.user.entity.BusinessConfirm;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BusinessConfirmDto {
    private Long id;
    private Long userId;
    private Integer status;

    public static BusinessConfirmDto fromEntity(BusinessConfirm entity){
        return BusinessConfirmDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .status(entity.getStatus())
                .build();
    }
}
