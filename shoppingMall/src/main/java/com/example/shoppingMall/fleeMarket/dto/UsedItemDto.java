package com.example.shoppingMall.fleeMarket.dto;

import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsedItemDto {
    private Long id;
    private Long userId;
    private String title;
    private String comment;
    private Integer price;
    private Integer transStatus;
    private String roadAddress;

    public static UsedItemDto fromEntity(UsedItem entity){
        return UsedItemDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .comment(entity.getComment())
                .price(entity.getPrice())
                .transStatus(entity.getTransStatus())
                .roadAddress(entity.getRoadAddress())
                .build();
    }
}
