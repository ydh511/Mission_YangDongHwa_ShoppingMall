package com.example.shoppingMall.jwt;

import com.example.shoppingMall.controller.CustomUserDetails;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsManagerImpl implements UserDetailsManager {
    private final UserRepo userRepo;

    public UserDetailsManagerImpl (
            UserRepo userRepo
    ) {
        this.userRepo = userRepo;
        // 오롯이 테스트 목적의 사용자를 추가하는 용도
        createUser(CustomUserDetails.builder()
                .userId("user1")
                .password("password")
                .build());
        createUser(CustomUserDetails.builder()
                .userId("user2")
                .password("password")
                .build());
    }

    @Override
    // formLogin 등 Spring Security 내부에서
    // 인증을 처리할때 사용하는 메서드이다.
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        Optional<User> optionalUser
                = userRepo.findByUserId(userId);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(userId);

        User userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .userId(userEntity.getUserId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();

        /*return User.withUsername(username)
                .password(optionalUser.get().getPassword())
                .build();*/
    }

    @Override
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    public void createUser(UserDetails user) {}

    @Override
    public boolean userExists(String username) {
        return userRepo.existsByUserId(username);
    }

    // 나중에...
    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
