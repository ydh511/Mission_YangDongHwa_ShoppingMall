package com.example.shoppingMall.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;

    @Setter
    private String email;
    @Setter
    private String nickname;
    @Setter
    private String personName;
    @Setter
    private Integer age;
    @Setter
    private String phone;
    @Setter
    private Integer businessGrade;
    @Setter
    private String businessNumber;
    private String roadAddress;
}
