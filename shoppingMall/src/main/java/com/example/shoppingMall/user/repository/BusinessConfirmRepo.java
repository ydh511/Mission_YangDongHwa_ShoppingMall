package com.example.shoppingMall.user.repository;

import com.example.shoppingMall.user.entity.BusinessConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessConfirmRepo extends JpaRepository<BusinessConfirm, Long> {
    List<BusinessConfirm> findAllByStatus(Integer status);
    BusinessConfirm findByUserId(Long id);
    BusinessConfirm findTopByUserId(Long id);
}
