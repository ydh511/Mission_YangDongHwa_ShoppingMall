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
public class UsedItemOffer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구매제안 한 사람
    private Long offerId;
    // 구매제안 할 사람
    private Long offeredId;
    private Long itemId;
    // 0이면 제안 거절, 1이면 거래 제안중, 2면 상대가 제안 수락한 상태
    @Setter
    private Integer offerStatus;
}
