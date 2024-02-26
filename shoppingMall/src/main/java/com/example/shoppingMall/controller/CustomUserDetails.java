package com.example.shoppingMall.controller;

import com.example.shoppingMall.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String personName;
    private Integer age;
    private String phone;
    private Integer businessGrade;
    private String businessNumber;
    private String roadAddress;
    // 권한 데이터를 담기 위한 속성
    /*private String authorities;
    public String getRawAuthorities() {
        return this.authorities;
    }*/

    @Override
    // ROLE_USER,ROLE_ADMIN,READ_AUTHORITY,WRITE_AUTHORITY
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*return Arrays.stream(authorities.split(","))
                .sorted()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*//*

        List<GrantedAuthority> grantedAuthorities
                = new ArrayList<>();
        String[] rawAuthorities = authorities.split(",");
        for (String rawAuthority: rawAuthorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
        }

        return grantedAuthorities;
        // return List.of(new SimpleGrantedAuthority("ROLE_USER"));*/
        return null;
    }

    public CustomUserDetails(UserDto dto){
        this.userId = getUserId();
        this.password = getPassword();
        this.email = getEmail();
        this.nickname = getNickname();
        this.personName = getPersonName();
        this.age = getAge();
        this.phone = getPhone();
        this.businessGrade = getBusinessGrade();
        this.businessNumber = getBusinessNumber();
        this.roadAddress = getRoadAddress();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }




    // 먼 미래
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
