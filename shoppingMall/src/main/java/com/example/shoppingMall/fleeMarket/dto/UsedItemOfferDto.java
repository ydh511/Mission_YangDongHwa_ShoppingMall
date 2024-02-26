package com.example.shoppingMall.fleeMarket.dto;

import com.example.shoppingMall.fleeMarket.entity.UsedItemOffer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsedItemOfferDto {
    private Long id;

    private Long userId;
    private Long itemId;
    private Integer offerStatus;

    public static UsedItemOfferDto fromEntity(UsedItemOffer entity){
        return UsedItemOfferDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .userId(entity.getUserId())
                .offerStatus(entity.getOfferStatus())
                .build();
    }
}
