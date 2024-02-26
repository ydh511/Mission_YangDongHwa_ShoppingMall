package com.example.shoppingMall.shoppingMall.dto;

import com.example.shoppingMall.shoppingMall.entity.ItemCateMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemCateMapperDto {
    private Long id;
    private Long itemId;
    private Long cateId;
    public static ItemCateMapperDto fromEntity(ItemCateMapper entity){
        return ItemCateMapperDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .cateId(entity.getCateId())
                .build();
    }
}
