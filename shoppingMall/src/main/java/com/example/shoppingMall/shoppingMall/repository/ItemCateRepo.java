package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ItemCate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCateRepo extends JpaRepository<ItemCate, Long> {
    List<ItemCate> findByTier(Integer tier);
    List<ItemCate> findByCateParent(Long cateParent);
}
