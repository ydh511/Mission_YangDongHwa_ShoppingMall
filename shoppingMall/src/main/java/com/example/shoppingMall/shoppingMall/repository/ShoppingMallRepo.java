package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingMallRepo extends JpaRepository<ShoppingMall, Long> {
    List<ShoppingMall> findAllByShopStatusOrderByLastTransDesc(Integer status);
    List<ShoppingMall> findAllByShopStatus(Integer status);
    Optional<ShoppingMall> findByUserId(Long id);
}
