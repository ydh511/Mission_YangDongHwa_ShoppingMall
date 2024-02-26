package com.example.shoppingMall.user.repository;

import com.example.shoppingMall.user.entity.BusinessConfirm;
import com.example.shoppingMall.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    List<User> findAllByBusinessGrade(Integer businessGrade);
    Optional<User> findByUserId(String userId);
}
