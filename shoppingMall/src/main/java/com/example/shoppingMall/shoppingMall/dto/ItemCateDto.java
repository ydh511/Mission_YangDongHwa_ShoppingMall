package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ItemCate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemCateDto {
    private Long id;
    private String cateName;
    private Long cateParent;
    private Integer tier;

    public static ItemCateDto fromEntity(ItemCate entity){
        return ItemCateDto.builder()
                .id(entity.getCateCode())
                .cateName(entity.getCateName())
                .cateParent(entity.getCateParent())
                .build();
    }
}
