package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShopItem;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShopItemDto {
    private Long id;
    private Long shopId;
    private Integer stock;
    private String itemName;
    private String comment;
    private Integer price;
    private List<Long> cateId;

    public static ShopItemDto fromEntity(ShopItem entity){
        return ShopItemDto.builder()
                .id(entity.getId())
                .shopId(entity.getShopId())
                .stock(entity.getStock())
                .itemName(entity.getItemName())
                .comment(entity.getComment())
                .price(entity.getPrice())
                .build();
    }
}
