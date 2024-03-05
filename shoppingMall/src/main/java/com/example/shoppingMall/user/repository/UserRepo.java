package com.example.shoppingMall.user.repository;

import com.example.shoppingMall.user.entity.BusinessConfirm;
import com.example.shoppingMall.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    List<User> findAllByAuthority(String authority);
    Optional<User> findByUsername(String username);
}
