package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShopClose;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShopCloseDto {
    private Long id;
    private Long shopId;
    private String closeReason;
    // 0: 폐쇄 거부 1: 요청중, 2:폐쇄 허가
    private Integer status;

    public static ShopCloseDto fromEntity(ShopClose entity){
        return  ShopCloseDto.builder()
                .id(entity.getId())
                .shopId(entity.getShopId())
                .closeReason(entity.getCloseReason())
                .status(entity.getStatus())
                .build();
    }
}
