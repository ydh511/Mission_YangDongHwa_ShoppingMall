package com.example.shoppingMall.controller;
import com.example.shoppingMall.fleeMarket.dto.UsedItemDto;
import com.example.shoppingMall.fleeMarket.dto.UsedItemOfferDto;
import com.example.shoppingMall.service.FleeMarketService;
import com.example.shoppingMall.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/flee-market")
public class FleeMarketController {
    private final UserDetailsServiceImpl userService;
    private final FleeMarketService marketService;

    // 중고물품 등록, 활성사용자 이상 가능
    @PostMapping("create-item")
    public UsedItemDto createItem(
            @RequestBody
            UsedItemDto dto,
            Authentication authentication
            ){
        return marketService.makeItem(dto, authentication.getName());
    }

    // 중고물품 전체 보기, 비로그인까지 가능
    @GetMapping("/items")
    public List<UsedItemDto> findAllItemDesc(){
        return marketService.findAllItemDesc();
    }

    // 중고물품 하나 보기, 비로그인까지 가능
    @GetMapping("/items/{id}")
    public UsedItemDto findItem(
            @PathVariable("id")
            Long id
    ){
        return marketService.findItem(id);
    }
    // 내 아이템 수정하기, 해당 활성유저 이상만 가능
    @PutMapping("update/{id}")
    public UsedItemDto updateItem(
            @RequestBody
            UsedItemDto dto,
            @PathVariable("id")
            Long id,
            Authentication authentication
    ){
        return marketService.updateItem(dto, authentication.getName(), id);
    }
    // 내 아이템 삭제하기, 해당 활성유저 이상만 가능
    @DeleteMapping("delete/{id}")
    public UsedItemDto deleteItem(
            @PathVariable("id")
            Long id,
            Authentication authentication
    ){
        return marketService.deleteItem(authentication.getName(), id);
    }

    // 중고물품 구매 신청, 활성 사용자 이상 가능
    @PostMapping("/offer-item/{id}")
    public UsedItemOfferDto offerItem(
            @PathVariable
            Long id,
            Authentication authentication
    ){
        return marketService.offerItem(id,authentication.getName());
    }
    // 내가 등록한 중고물품 보기, 해당 활성사용자 이상 가능
    @GetMapping("/find-my-items")
    public List<UsedItemDto> findMyItems(
            Authentication authentication
    ){
        return marketService.findMyItems(authentication.getName());
    }
    // 내가 구매신청한 물품 목록 보기. 해당 활성사용자 이상 가능
    @GetMapping("/find-my-offers")
    public List<UsedItemOfferDto> findMyOffers(
            Authentication authentication
    ){
        return marketService.findMyOffers(authentication.getName());
    }
    //내가 등록한 아이템에 걸려있는 구매신청들 보기.
    @GetMapping("/find-item-offers/{itemId}")
    public List<UsedItemOfferDto> findItemOffers(
            Authentication authentication,
            @PathVariable("itemId")
            Long itemId
    ){
        return marketService.findItemOffers(itemId, authentication.getName());
    }

    // 아이템에 걸려있는 구매신청 승인, 해당 활성사용자 이상 가능
    @PutMapping("/confirm-offer/{itemId}")
    public String confirmOffer(
            @RequestParam("offerId")
            Long offerId,
            @PathVariable("itemId")
            Long itemId,
            Authentication authentication
    ){
        return marketService.confirmOffer(itemId, offerId, authentication.getName());
    }
    // 구매신청을 승인 받았을 떄 구매자가 구매확정을 누름, 해당 활성사용자 이상 가능
    @PutMapping("/confirm-item/{itemId}")
    public UsedItemDto confirmItem(
            @PathVariable("itemId")
            Long itemId,
            Authentication authentication
    ){
        return marketService.confirmItem(itemId, authentication.getName());
    }
}
