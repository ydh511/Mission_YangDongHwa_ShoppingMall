package com.example.shoppingMall.shoppingMall.entity;

import com.example.shoppingMall.user.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ShopItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;
    private Integer stock;
    private String itemName;
    private String comment;
    private Integer price;
    private Long cateId;
}
