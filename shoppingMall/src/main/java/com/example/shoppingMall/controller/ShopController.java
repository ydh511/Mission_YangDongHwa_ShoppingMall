package com.example.shoppingMall.controller;

import com.example.shoppingMall.service.ShopService;
import com.example.shoppingMall.shoppingMall.dto.*;

import com.example.shoppingMall.shoppingMall.entity.ShopItemOffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 쇼핑몰 쇼핑몰품
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shoppingmall")
public class ShopController {
    private final ShopService shopService;
    // 쇼핑몰 카테고리 보기
    @GetMapping("view-shopcate")
    public List<ShopCateDto> viewShopCate(){
        return shopService.viewShopCate();
    }

    // 쇼핑몰 개설신청하기
    @PutMapping("opening-offer")
    public ShoppingMallDto openOffer(
            @RequestBody
            ShoppingMallDto dto,
            Authentication authentication
    ){
        return shopService.openOffer(dto, authentication.getName());
    }

    // 개설신청한 쇼핑몰 보기, 관리자만 가능
    @GetMapping("/view-offered-shop")
    public List<ShoppingMallDto> viewOfferedShop(){
        return shopService.viewOfferedShop();
    }

    // 개설신청 거절하기, 관리자만 가능
    @PutMapping("/cancel-offer/{shopId}")
    public ShoppingMallDto cancelOffer(
            @PathVariable("shopId")
            Long shopId,
            @RequestParam
            String reason
    ){
        return shopService.cancelOffer(shopId, reason);
    }
    // 개설신청 승인하기, 관리자만 가능
    @PutMapping("/confirm-offer/{shopId}")
    public ShoppingMallDto confirmOffer(
            @PathVariable("shopId")
            Long shopId
    ){
        return shopService.confirmOffer(shopId);
    }
    // 쇼핑몰 폐쇄요청, 해당 사업자만 가능
    @PostMapping("{shopId}/close-offer")
    public ShopCloseDto closeOffer(
            Authentication authentication,
            @PathVariable("shopId")
            Long shopId,
            @RequestBody
            ShopCloseDto dto
    ){
        return shopService.closeOffer(shopId, dto, authentication.getName());
    }
    // 쇼핑몰 폐쇄요청 보기, 관리자만 가능
    @PutMapping("/view-close-offer")
    public List<ShopCloseDto> viewCloseOffer(){
        return shopService.viewCloseOffer();
    }

    // 쇼핑몰 폐쇄하기, 관리자만 가능
    @PutMapping("{shopId}/confirm-close")
    public ShopCloseDto confirmClose(
            @PathVariable("shopId")
            Long shopId,
            Authentication authentication
    ){
        return shopService.confirmClose(shopId, authentication.getName());
    }
    // 쇼핑몰 리스트 보기(최근 거래된 순으로), 전체가능
    @GetMapping("/view-shoplist")
    public List<ShoppingMallDto> viewShopList(){
        return shopService.viewShopList();
    }

    // 아이템 카테고리 대분류 보기, 전체가능
    @GetMapping("/view-itemcate")
    public List<ItemCateDto> viewBigItemCate(){
        return viewBigItemCate();
    }

    // 아이템 카테고리 대분류의 소분류 보기, 전체가능
    @GetMapping("/view-itemcate/{cateCode}")
    public List<ItemCateDto> viewSmallItemCate(
            @PathVariable("cateCode")
            Long cateCode
    ){
        return viewSmallItemCate(cateCode);
    }
    // 쇼핑몰에 아이템 추가하기, 해당 사업자만 가능
    @PostMapping("/{shopId}/create-item")
    public ShopItemDto createItem(
            @PathVariable("shopId")
            Long shopId,
            @RequestParam
            ShopItemDto dto,
            Authentication authentication
    ){
        return shopService.createItem(shopId,dto, authentication.getName());
    }

    //쇼핑몰의 아이템 리스트 보기, 전체가능
    @GetMapping("/{shopId}/items")
    public List<ShopItemDto> viewItemList(
            @PathVariable("shopId")
            Long shopId
    ){
        return  shopService.viewItemList(shopId);
    }

    // 쇼핑몰 아이템 리스트 카테고리 검색으로 보기, 전체가능
    @GetMapping("/{shopId}/cate/{cateCode}")
    public List<ShopItemDto> viewItemListByCate(
            @PathVariable("shopId")
            Long shopId,
            @PathVariable("cateCode")
            Long cateCode
    ){
        return  shopService.viewItemListByCate(shopId,cateCode);
    }
    // 쇼핑몰 아이템 리스트 가격 검색으로 보기, 전체가능
    @GetMapping("/{shopId}/price")
    public List<ShopItemDto> viewItemListByPrice(
            @PathVariable("shopId")
            Long shopId,
            @RequestParam("minPrice")
            Integer minPrice,
            @RequestParam("maxPrice")
            Integer maxPrice
    ){
        return  shopService.viewItemListByPrice(shopId,minPrice,maxPrice);
    }


    // 아이템 수정하기, 해당 사업자만 가능
    @PostMapping("/{shopId}/update-item/{itemId}")
    public ShopItemDto createItem(
            @PathVariable("shopId")
            Long shopId,
            @PathVariable("itemId")
            Long itemId,
            @RequestParam
            ShopItemDto dto,
            Authentication authentication
    ){
        return shopService.updateItem(shopId,dto, authentication.getName(),itemId);
    }
    // 아이템 지우기, 해당 사업자만 가능
    @DeleteMapping("/{shopId}/delete-item/{itemId}")
    public String deleteItem(
            @PathVariable("shopId")
            Long shopId,
            @PathVariable("itemId")
            Long itemId,
            Authentication authentication
    ){
        return shopService.deleteItem(shopId,authentication.getName(),itemId);
    }

    // 아이템 구매요청하기, 활성 사용자 이상 가능
    @PostMapping("/offer/{itemId}")
    public ShopItemOfferDto offerItem(
            @PathVariable("itemId")
            Long itemId,
            @RequestBody
            ShopItemOfferDto dto,
            Authentication authentication
    ){
        return shopService.offerItem(itemId, dto,authentication.getName());
    }

    // 아이템에 걸려있는 구매요청상태들(0~4번) 보기, 해당 사업자 가능
    @GetMapping("/offered-item/{itemId}")
    public List<ShopItemOfferDto> offeredItem(
            @RequestParam
            Integer status,
            @PathVariable("itemId")
            Long itemId,
            Authentication authentication
    ){
        return shopService.offeredItem(status, itemId, authentication.getName());
    }

    // 아이템 구매 승인, 해당 사업자 가능
    @PutMapping("/confirm-item/{itemOfferId}")
    public ShopItemOfferDto confirmItem(
            @PathVariable("itemOfferId")
            Long itemOfferId,
            Authentication authentication
    ){
        return shopService.confirmItem(itemOfferId, authentication.getName());
    }

    // 아이템 구매 취소요청하기, 해당 활성사용자 이상 가능
    @PutMapping("/cancel-itemoffer/{itemOfferId}")
    public ShopItemOfferDto cancelItemOffer(
            @PathVariable("itemOfferId")
            Long itemOfferId,
            Authentication authentication
    ){
        return shopService.cancelItemOffer(itemOfferId, authentication.getName());
    }
    // 구매취소,환불요청 사업자가 승인, 해당 사업자 가능
    @PutMapping("/reject-item/{itemOfferId}")
    public ShopItemOfferDto rejectItem(
            @PathVariable("itemOfferId")
            Long itemOfferId,
            Authentication authentication
    ){
        return shopService.rejectItem(itemOfferId, authentication.getName());
    }
}
