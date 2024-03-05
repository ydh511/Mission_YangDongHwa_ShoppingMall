package com.example.shoppingMall.fleeMarket.repository;

import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsedItemRepo extends JpaRepository<UsedItem, Long> {
    List<UsedItem> findAllByOrderByIdDesc();
    List<UsedItem> findAllByUserIdOrderByIdDesc(Long userId);
    UsedItem findByUserId(Long userId);
}
