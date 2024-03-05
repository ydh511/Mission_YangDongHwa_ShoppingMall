package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopItemRepo extends JpaRepository<ShopItem, Long> {
    List<ShopItem> findAllById(Long id);

}
