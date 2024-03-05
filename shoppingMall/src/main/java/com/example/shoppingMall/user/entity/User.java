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
    //@Column(name = "usernames", nullable = false, unique = true)
    private String username;
    private String password;
    @Setter
    private boolean certification;
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

    // ROLE_INACTIVE: 비활성 사용자, ROLE_ACTIVE: 활성 사용자
    // ROLE_BUSINESS: 사업자, ROLE_ADMIN: 관리자
    @Setter
    private String authority;
    @Setter
    private String businessNumber;
    private String roadAddress;

    public void updateInactiveUser(String password){
        this.password = password;
    }
    public void updateActiveUser(
            String password,
            String email,
            String nickname,
            String personName,
            Integer age,
            String phone,
            String roadAddress
    ){
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.personName = personName;
        this.age = age;
        this.phone = phone;
        this.roadAddress = roadAddress;
    }
}
