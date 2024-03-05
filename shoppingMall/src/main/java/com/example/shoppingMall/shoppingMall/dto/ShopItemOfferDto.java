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
    private Long offerId;
    private Long offeredId;
    private Long itemId;
    private Integer stock;
    private Integer price;
    private Integer status;

    public static ShopItemOfferDto fromEntity(ShopItemOffer entity){
        return ShopItemOfferDto.builder()
                .id(entity.getId())
                .offerId(entity.getOfferId())
                .offeredId(entity.getOfferedId())
                .itemId(entity.getItemId())
                .stock(entity.getStock())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .build();
    }
}
