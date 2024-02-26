package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ShopCate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShopCateDto {
    private Long id;
    private String cateName;

    public static ShopCateDto fromEntity(ShopCate entity){
        return ShopCateDto.builder()
                .id(entity.getId())
                .cateName(entity.getCateName())
                .build();
    }
}
