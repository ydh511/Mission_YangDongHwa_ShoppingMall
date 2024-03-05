package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ShopClose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopCloseRepo extends JpaRepository<ShopClose, Long> {
    List<ShopClose> findAllByStatus(Integer status);
    Optional<ShopClose> findByShopIdAndStatus(Long shopId, Integer status);
    Boolean existsByShopIdAndStatus(Long shopId, Integer status);
}
