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

    private Long offerId;
    private Long offeredId;
    private Long itemId;
    private Integer offerStatus;

    public static UsedItemOfferDto fromEntity(UsedItemOffer entity){
        return UsedItemOfferDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .offerId(entity.getOfferId())
                .offeredId(entity.getOfferedId())
                .offerStatus(entity.getOfferStatus())
                .build();
    }
}
