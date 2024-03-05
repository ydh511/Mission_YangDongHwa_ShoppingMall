package com.example.shoppingMall.fleeMarket.repository;

import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import com.example.shoppingMall.fleeMarket.entity.UsedItemOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsedItemOfferRepo extends JpaRepository<UsedItemOffer, Long> {
    List<UsedItemOffer> findAllByOfferIdOrderByIdDesc(Long userId);
    List<UsedItemOffer> findAllByItemIdOrderByIdDesc(Long itemId);
    Boolean existsByOfferIdAndOfferStatusOrOfferIdAndOfferStatus(
            Long id1, Integer status1, Long id2, Integer status2
    );
}
