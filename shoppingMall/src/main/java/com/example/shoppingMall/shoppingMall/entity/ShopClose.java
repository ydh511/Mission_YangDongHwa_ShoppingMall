package com.example.shoppingMall.shoppingMall.entity;

import com.example.shoppingMall.user.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShopClose extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long shopId;
    private String closeReason;
    // 0: 폐쇄 거부 1: 요청중, 2:폐쇄 허가
    @Setter
    private Integer status;
}
