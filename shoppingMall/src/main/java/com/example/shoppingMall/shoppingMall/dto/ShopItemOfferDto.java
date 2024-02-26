package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShopItemOffer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShopItemOfferDto {
    private Long id;
    private Long UserId;
    private Long ItemId;

    public static ShopItemOfferDto fromEntity(ShopItemOffer entity){
        return ShopItemOfferDto.builder()
                .id(entity.getId())
                .UserId(entity.getUserId())
                .ItemId(entity.getItemId())
                .build();
    }
}
