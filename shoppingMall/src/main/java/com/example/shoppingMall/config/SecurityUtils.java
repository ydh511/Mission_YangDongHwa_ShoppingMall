package com.example.shoppingMall.config;

import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class SecurityUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;
    private final String jwtSecret;
    private final UserRepo userRepo;

    public SecurityUtils(
            @Value("${jwt.secret}")
            String jwtSecret,
            UserRepo userRepo) {
        this.jwtSecret = jwtSecret;
        this.signingKey
                = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.userRepo = userRepo;
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }
    // 토큰 발급
    public String generateToken(UserDetails userDetails){
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)));
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }
    // 토큰이 유효한지
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e){
            log.warn("invalid jwt");
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // test용?
    public boolean checkUserFromToken(String token,Long id){
        String subject;
        try {
            subject = Jwts.parser().setSigningKey(jwtSecret)
                    .parseClaimsJws(token).getBody().getSubject().toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not get all claims Token from passed token");
        }
        User user = userRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User is not exist"));
        return user.getUsername().equals(subject);
    }

    //test용
    public String JwtSecret(){
        return jwtSecret;
    }
    //test용
    public String getSubject(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims.getSubject().toString();
    }
}
