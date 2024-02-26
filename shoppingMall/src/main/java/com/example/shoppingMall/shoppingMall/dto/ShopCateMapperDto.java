package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShopCateMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShopCateMapperDto {
    private Long id;
    private Long shopId;
    private Long cateId;

    public static ShopCateMapperDto fromEntity(ShopCateMapper entity){
        return ShopCateMapperDto.builder()
                .id(entity.getId())
                .shopId(entity.getShopId())
                .cateId(entity.getCateId())
                .build();
    }
}
