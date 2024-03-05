package com.example.shoppingMall.shoppingMall.repository;

import com.example.shoppingMall.shoppingMall.entity.ItemCateMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCateMapperRepo extends JpaRepository<ItemCateMapper, Long> {
    void deleteAllByItemId(Long itemId);
    List<ItemCateMapper> findAllByShopIdAndCateId(Long shopId, Long cateId);

}
