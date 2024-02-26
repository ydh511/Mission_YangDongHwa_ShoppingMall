package com.example.shoppingMall.shoppingMall.dto;

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

    public static ItemCateDto fromEntity(ItemCateDto entity){
        return ItemCateDto.builder()
                .id(entity.getId())
                .cateName(entity.cateName)
                .cateParent(entity.cateParent)
                .build();
    }
}
