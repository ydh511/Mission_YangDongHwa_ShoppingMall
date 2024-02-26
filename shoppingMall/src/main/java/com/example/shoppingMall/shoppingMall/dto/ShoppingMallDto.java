package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShoppingMall;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingMallDto {
    private Long id;
    private Long userId;
    private String shopName;
    private String shopIntroduce;
    private Long shopCateId;
    private String cancelReason;
    private LocalDateTime lastTrans;

    public static ShoppingMallDto fromEntity(ShoppingMall entity){
        return ShoppingMallDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .shopName(entity.getShopName())
                .shopIntroduce(entity.getShopIntroduce())
                .shopCateId(entity.getShopCateId())
                .cancelReason(entity.getCancelReason())
                .lastTrans(entity.getLastTrans())
                .build();
    }
}
