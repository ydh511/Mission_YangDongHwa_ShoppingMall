package com.example.shoppingMall.service;

import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import com.example.shoppingMall.fleeMarket.repository.UsedItemRepo;
import com.example.shoppingMall.shoppingMall.entity.*;
import com.example.shoppingMall.shoppingMall.repository.*;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// 유저를 db로 만들어서 넣으려고 했는데 유저 비밀번호가 encoding 돼서
// 서비스로 유저를 만들어 넣었다.
// 만들어놓은 중고물품, 쇼핑물, 쇼핑몰상품은 user 의 id 기준으로 돌아가서
// 아래 유저를 만들어둔 순서대로 id가 부여되니 그 순서들을 참고해서 넣었다.
@Service
public class MakeMarketService {
    private final UserRepo userRepo;
    private final UsedItemRepo usedItemRepo;
    private final ShopCateRepo shopCateRepo;
    private final ItemCateRepo itemCateRepo;
    private final ShopItemRepo shopItemRepo;
    private final ShoppingMallRepo shoppingMallRepo;
    private final ItemCateMapperRepo itemCateMapperRepo;

    public MakeMarketService(
            PasswordEncoder passwordEncoder,
            UserRepo userRepo,
            UsedItemRepo usedItemRepo,
            ShopCateRepo shopCateRepo,
            ItemCateRepo itemCateRepo,
            ShopItemRepo shopItemRepo,
            ShoppingMallRepo shoppingMallRepo,
            ItemCateMapperRepo itemCateMapperRepo
    ){
        this.userRepo = userRepo;
        this.usedItemRepo = usedItemRepo;
        this.shopCateRepo = shopCateRepo;
        this.itemCateRepo = itemCateRepo;
        this.shopItemRepo = shopItemRepo;
        this.shoppingMallRepo = shoppingMallRepo;
        this.itemCateMapperRepo = itemCateMapperRepo;

        // 유저 만들어놓기
        if(userRepo.count() == 0)
            this.userRepo.saveAll(List.of(
                    User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("password"))
                            .authority("ROLE_ADMIN")
                            .email("aaa@naver.com")
                            .nickname("nickname")
                            .personName("Jung")
                            .age(20)
                            .certification(true)
                            .phone("010-0000-0000")
                            .build(),
                    User.builder()
                            .username("user1")
                            .password(passwordEncoder.encode("password"))
                            .authority("ROLE_INACTIVE")
                            .certification(false)
                            .build(),
                    User.builder()
                            .username("user2")
                            .password(passwordEncoder.encode("password"))
                            .email("bbb@naver.com")
                            .nickname("nickname")
                            .personName("Kim")
                            .age(22)
                            .phone("010-1111-2222")
                            .authority("ROLE_ACTIVE")
                            .certification(true)
                            .build(),
                    User.builder()
                            .username("user3")
                            .password(passwordEncoder.encode("password"))
                            .email("ccc@naver.com")
                            .nickname("nickname")
                            .personName("Jung")
                            .age(30)
                            .phone("010-3333-4444")
                            .authority("ROLE_BUSINESS")
                            .businessNumber("abcde")
                            .certification(true)
                            .build(),
                    User.builder()
                            .username("user4")
                            .password(passwordEncoder.encode("password"))
                            .email("ddd@naver.com")
                            .nickname("nickname")
                            .personName("Lee")
                            .age(35)
                            .phone("010-5555-6666")
                            .authority("ROLE_BUSINESS")
                            .businessNumber("fghij")
                            .certification(true)
                            .build()
            ));
        // 중고물건 만들어놓기
        if(usedItemRepo.count() == 0){
            this.usedItemRepo.saveAll(List.of(
                    UsedItem.builder()
                            .userId(3L)
                            .title("mouse")
                            .comment("for sail")
                            .transStatus(0)
                            .price(5000)
                            .build(),
                    UsedItem.builder()
                            .userId(3L)
                            .title("keyboard")
                            .comment("for sail")
                            .transStatus(0)
                            .price(10000)
                            .build(),
                    UsedItem.builder()
                            .userId(4L)
                            .title("car")
                            .comment("for sail")
                            .transStatus(0)
                            .price(50000000)
                            .build(),
                    UsedItem.builder()
                            .userId(4L)
                            .title("air conditioner")
                            .comment("for sail")
                            .transStatus(0)
                            .price(500000)
                            .build()
            ));
        }
        // 쇼핑물카테고리
        if (shopCateRepo.count() == 0){
            this.shopCateRepo.saveAll(List.of(
                    ShopCate.builder()
                            .cateName("food")
                            .build(),
                    ShopCate.builder()
                            .cateName("health")
                            .build(),
                    ShopCate.builder()
                            .cateName("clothes")
                            .build(),
                    ShopCate.builder()
                            .cateName("food")
                            .build(),
                    ShopCate.builder()
                            .cateName("office")
                            .build(),
                    ShopCate.builder()
                            .cateName("sports")
                            .build()
            ));
        }
        // 아이템 카테고리
        if (itemCateRepo.count() == 0) {
            this.itemCateRepo.saveAll(List.of(
                    ItemCate.builder()
                            .cateCode(1000L)
                            .cateName("food")
                            .tier(1)
                            .build(),
                    ItemCate.builder()
                            .cateCode(1001L)
                            .cateName("frozen food")
                            .tier(2)
                            .cateParent(1000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(1002L)
                            .cateName("fresh food")
                            .tier(2)
                            .cateParent(1000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(2000L)
                            .cateName("furniture")
                            .tier(1)
                            .build(),
                    ItemCate.builder()
                            .cateCode(2001L)
                            .cateName("chair")
                            .tier(2)
                            .cateParent(2000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(2002L)
                            .cateName("desk")
                            .tier(2)
                            .cateParent(2000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(3000L)
                            .cateName("health")
                            .tier(1)
                            .build(),
                    ItemCate.builder()
                            .cateCode(3001L)
                            .cateName("nutrients")
                            .tier(2)
                            .cateParent(3000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(3002L)
                            .cateName("health products")
                            .tier(2)
                            .cateParent(3000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(4000L)
                            .cateName("office")
                            .tier(1)
                            .build(),
                    ItemCate.builder()
                            .cateCode(4001L)
                            .cateName("pen")
                            .tier(2)
                            .cateParent(4000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(4002L)
                            .cateName("ball pen")
                            .tier(2)
                            .cateParent(4000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(5000L)
                            .cateName("sports")
                            .tier(1)
                            .build(),
                    ItemCate.builder()
                            .cateCode(5001L)
                            .cateName("ball")
                            .tier(2)
                            .cateParent(5000L)
                            .build(),
                    ItemCate.builder()
                            .cateCode(5002L)
                            .cateName("shoes")
                            .tier(2)
                            .cateParent(5000L)
                            .build()
            ));
        }
        // 상점 만들어두기
        if (shoppingMallRepo.count() == 0){
            this.shoppingMallRepo.saveAll(List.of(
                    ShoppingMall.builder()
                            .userId(4L)
                            .shopName("healthy day")
                            .shopIntroduce("for healthy days")
                            .shopCateId(2L)
                            .cancelReason("")
                            .shopStatus(3)
                            .lastTrans(LocalDateTime.now())
                            .build(),
                    ShoppingMall.builder()
                            .userId(5L)
                            .shopName("All of food")
                            .shopIntroduce("Every foods are here")
                            .shopCateId(4L)
                            .cancelReason("")
                            .shopStatus(3)
                            .lastTrans(LocalDateTime.now())
                            .build()
            ));

        }
        // 아이템 만들어두기
        if(shopItemRepo.count() == 0){
            this.shopItemRepo.saveAll(List.of(
                    ShopItem.builder()
                            .shopId(1L)
                            .stock(40)
                            .itemName("Great nutrients")
                            .comment("It is good for you")
                            .price(30000)
                            .build(),
                    ShopItem.builder()
                            .shopId(1L)
                            .stock(50)
                            .itemName("massager")
                            .comment("It is good for you")
                            .price(70000)
                            .build(),
                    ShopItem.builder()
                            .shopId(2L)
                            .stock(20)
                            .itemName("frozen pizza")
                            .comment("yummy")
                            .price(6000)
                            .build(),
                    ShopItem.builder()
                            .shopId(2L)
                            .stock(20)
                            .itemName("fresh milk")
                            .comment("drink!")
                            .price(2000)
                            .build()
                    ));
            // 아이템 카테고리 mapper 만들어두기
        }
        if (itemCateMapperRepo.count() == 0){
            this.itemCateMapperRepo.saveAll(List.of(
                    ItemCateMapper.builder()
                            .shopId(1L)
                            .itemId(1L)
                            .cateId(3001L)
                            .build(),
                    ItemCateMapper.builder()
                            .shopId(1L)
                            .itemId(2L)
                            .cateId(3002L)
                            .build(),
                    ItemCateMapper.builder()
                            .shopId(2L)
                            .itemId(3L)
                            .cateId(1001L)
                            .build(),
                    ItemCateMapper.builder()
                            .shopId(2L)
                            .itemId(4L)
                            .cateId(1002L)
                            .build()
            ));
        }
    }
}

