package com.example.shoppingMall.shoppingMall.entity;

import com.example.shoppingMall.user.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ShoppingMall extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String shopName;
    private String shopIntroduce;
    private Long shopCateId;
    private Integer shopStatus;
    private String cancelReason;
    private LocalDateTime lastTrans;
}
