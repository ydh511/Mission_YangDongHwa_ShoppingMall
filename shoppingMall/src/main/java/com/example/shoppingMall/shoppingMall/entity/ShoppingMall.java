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
@Builder
public class ShoppingMall extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    //@Column(name = "shopnames", unique = true)
    private String shopName;
    private String shopIntroduce;
    private Long shopCateId;
    //  0: 폐쇄, 1: 준비중, 2: 개설신청중, 3: 개설
    @Setter
    private Integer shopStatus;
    @Setter
    private String cancelReason;
    @Setter
    private LocalDateTime lastTrans;

    public void openOffer(
            String shopName,
            String shopIntroduce,
            Long shopCateId,
            Integer shopStatus
    ){
        this.shopName = shopName;
        this.shopIntroduce = shopIntroduce;
        this.shopCateId = shopCateId;
        this.shopStatus = shopStatus;
    }
}
