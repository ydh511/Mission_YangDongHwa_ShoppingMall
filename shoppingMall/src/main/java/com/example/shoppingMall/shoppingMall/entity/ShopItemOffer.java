package com.example.shoppingMall.shoppingMall.entity;

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
public class ShopItemOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 사는사람
    private Long offerId;
    // 파는사람
    private Long offeredId;
    private Long itemId;
    private Integer stock;
    private Integer price;
    // 0: (돈이 지불 된 후)거래 대기중, 1: 거래 완료 2: (돈이 지불 된 후)구매자쪽에서 거래 승인 전 취소
    // 3: 구매 후 환불 요청중, 4: 환불(취소) 완료 / (돈이 지불 된 후)판매자쪽에서 구매승인 전 거래 취소함
    @Setter
    private Integer status;
}
