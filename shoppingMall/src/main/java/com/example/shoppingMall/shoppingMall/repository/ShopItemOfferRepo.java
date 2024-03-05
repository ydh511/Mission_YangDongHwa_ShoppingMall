package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ShopItemOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopItemOfferRepo extends JpaRepository<ShopItemOffer, Long> {
    List<ShopItemOffer> findAllByItemId(Long itemId);
    List<ShopItemOffer> findAllByStatusAndItemId(Integer status, Long id);
}
