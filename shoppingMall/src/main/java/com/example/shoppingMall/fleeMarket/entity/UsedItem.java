package com.example.shoppingMall.fleeMarket.entity;

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
@Builder
public class UsedItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    private String comment;
    private Integer price;
    // 0: 물품 등록만, 1: 구매 제안 받았음,
    // 2: 승락 후 거래 대기중, 3: 거래 완료
    @Setter
    private Integer transStatus;
    private String roadAddress;

    public void updateItem(String title, String comment, Integer price, String roadAddress){
        this.title = title;
        this.comment = comment;
        this.price = price;
        this.roadAddress = roadAddress;
    }
}
