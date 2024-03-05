package com.example.shoppingMall.service;

import com.example.shoppingMall.shoppingMall.dto.*;
import com.example.shoppingMall.shoppingMall.entity.*;
import com.example.shoppingMall.shoppingMall.repository.*;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final UserRepo userRepo;
    private final ShoppingMallRepo shopRepo;
    private final ShopItemRepo itemRepo;
    private final ItemCateRepo itemCateRepo;
    private final ShopCloseRepo closeRepo;
    private final ShopCateRepo shopCateRepo;
    private final ItemCateMapperRepo itemCateMapperRepo;
    private final ShopItemOfferRepo itemOfferRepo;

    // 쇼핑몰 카테고리 보기, 전체가능
    public List<ShopCateDto> viewShopCate(){
        List<ShopCate> shopCates = shopCateRepo.findAll();
        if(shopCates.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ShopCateDto> shopCateDtos = new ArrayList<>();
        for (ShopCate cates : shopCates){
            shopCateDtos.add(ShopCateDto.fromEntity(cates));
        }
        return shopCateDtos;
    }

    // 준비중이거나 폐쇄된 쇼핑몰 개설신청, 해당 사업자만 가능
    public ShoppingMallDto openOffer(ShoppingMallDto dto, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<ShoppingMall> shop = shopRepo.findByUserId(user.getId());
        if (shop.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ShoppingMall shopEntity = shop.get();
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        if(shopEntity.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        shopEntity.openOffer(dto.getShopName(), dto.getShopIntroduce() ,dto.getShopCateId(), 2);
        shopRepo.save(shopEntity);
        return ShoppingMallDto.fromEntity(shopEntity);
    }

    // 개설신청한 쇼핑몰 보기, 관리자만 가능
    public List<ShoppingMallDto> viewOfferedShop(){
        List<ShoppingMall> shoppingMalls = shopRepo.findAllByShopStatus(2);
        if (shoppingMalls.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ShoppingMallDto> shoppingMallDtos = new ArrayList<>();
        for (ShoppingMall shop : shoppingMalls){
            shoppingMallDtos.add(ShoppingMallDto.fromEntity(shop));
        }
        return shoppingMallDtos;
    }

    // 개설신청 거절하기, 관리자만 가능
    public ShoppingMallDto cancelOffer(Long shopId, String reason){
        ShoppingMall shoppingMall = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 개설신청중을 준비중으로 바꾸고 거절 이유를 붙임
        shoppingMall.setShopStatus(1);
        shoppingMall.setCancelReason(reason);
        return ShoppingMallDto.fromEntity(shopRepo.save(shoppingMall));
    }

    // 개설신청 승인하기, 관리자만 가능
    public ShoppingMallDto confirmOffer(Long shopId){
        ShoppingMall shoppingMall = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 개설신청중을 개설로 바꾸고 거절 이유를 없엠
        shoppingMall.setShopStatus(3);
        shoppingMall.setCancelReason("");
        return ShoppingMallDto.fromEntity(shopRepo.save(shoppingMall));
    }

    // 쇼핑몰 폐쇄요청, 해당 사업자만 가능
    public ShopCloseDto closeOffer(Long shopId, ShopCloseDto dto, String username){
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(shop.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // 신청을 했는데 처리되지않았으면 다시 신청 못함
        if(closeRepo.existsByShopIdAndStatus(shop.getId(), 1))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // 쇼핑몰이 폐쇄중일경우 다시 신청 못함
        if(shop.getShopStatus() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ShopClose shopClose = ShopClose.builder()
                .shopId(shopId)
                .closeReason(dto.getCloseReason())
                .status(1)
                .build();
        return ShopCloseDto.fromEntity(closeRepo.save(shopClose));
    }

    // 쇼핑몰 폐쇄요청 보기, 관리자만 가능
    public List<ShopCloseDto> viewCloseOffer(){
        List<ShopClose> shopCloses = closeRepo.findAllByStatus(1);
        if(shopCloses.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ShopCloseDto> shopCloseDtos = new ArrayList<>();

        for (ShopClose close : shopCloses){
            shopCloseDtos.add(ShopCloseDto.fromEntity(close));
        }
        return shopCloseDtos;
    }

    // 쇼핑몰 폐쇄하기, 관리자만 가능
    public ShopCloseDto confirmClose(Long shopId, String username){
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(shop.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<ShopClose> optionalShopClose = closeRepo.findByShopIdAndStatus(shop.getId(), shop.getShopStatus());
        if (optionalShopClose.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ShopClose shopClose = optionalShopClose.get();
        // 쇼핑몰 폐쇄신청 승인
        shopClose.setStatus(2);
        // 쇼핑몰 폐쇄
        shop.setShopStatus(0);
        shopRepo.save(shop);
        closeRepo.save(shopClose);
        return ShopCloseDto.fromEntity(shopClose);
    }

    // 쇼핑몰 리스트 보기, 전체가능
    public List<ShoppingMallDto> viewShopList(){
        // 개설된 쇼핑몰만 가져옴, 최근 거래된 순으로
        List<ShoppingMall> shoppingMalls = shopRepo.findAllByShopStatusOrderByLastTransDesc(3);
        if (shoppingMalls.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ShoppingMallDto> shoppingMallDtos = new ArrayList<>();
        for (ShoppingMall shop : shoppingMalls){
            shoppingMallDtos.add(ShoppingMallDto.fromEntity(shop));
        }
        return shoppingMallDtos;
    }

    // 아이템 카테고리 대분류 보기, 전체가능
    public List<ItemCateDto> viewBigItemCate(){
        List<ItemCate> itemCates = itemCateRepo.findByTier(1);
        List<ItemCateDto> itemCateDtos = new ArrayList<>();
        for (ItemCate cate : itemCates){
            itemCateDtos.add(ItemCateDto.fromEntity(cate));
        }
        return itemCateDtos;
    }

    // 아이템 카테고리 소분류 보기
    public List<ItemCateDto> viewSmallItemCate(Long cateCode){
        List<ItemCate> itemCates = itemCateRepo.findByCateParent(cateCode);
        List<ItemCateDto> itemCateDtos = new ArrayList<>();
        for (ItemCate cate : itemCates){
            itemCateDtos.add(ItemCateDto.fromEntity(cate));
        }
        return itemCateDtos;
    }

    // 쇼핑몰에 아이템 추가하기, 해당 사업자만 가능
    public ShopItemDto createItem(Long shopId, ShopItemDto dto, String username){
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(shop.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ShopItem shopItem = ShopItem.builder()
                .shopId(shopId)
                .stock(dto.getStock())
                .itemName(dto.getItemName())
                .comment(dto.getComment())
                .price(dto.getPrice())
                .build();
        shopItem = itemRepo.save(shopItem);
        List<ItemCateMapper> itemCateMappers = new ArrayList<>();
        for (int i = 0; i < dto.getCateId().size(); i++){
            itemCateMappers.add(
                    ItemCateMapper.builder()
                            .itemId(shopItem.getId())
                            .cateId(dto.getCateId().get(i))
                            .shopId(shopId)
                            .build()
            );
        }
        itemCateMapperRepo.saveAll(itemCateMappers);
        return ShopItemDto.fromEntity(itemRepo.save(shopItem));
    }

    // 쇼핑몰 아이템 리스트 보기, 전체가능
    public List<ShopItemDto> viewItemList(Long shopId){
        // 개설된 쇼핑몰만 보여줌
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (shop.getShopStatus() != 3)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 아이템 리스트를 가져옴
        List<ShopItem> shopItems = itemRepo.findAllById(shopId);
        if (shopItems.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ShopItemDto> shopItemDtos = new ArrayList<>();
        for (ShopItem item : shopItems){
            shopItemDtos.add(ShopItemDto.fromEntity(item));
        }
        return shopItemDtos;
    }

    // 쇼핑몰 아이템리스트 카테고리검색으로 보기, 전체가능
    public List<ShopItemDto> viewItemListByCate(Long shopId, Long cateCode){
        // 개설된 쇼핑몰만 보여줌
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (shop.getShopStatus() != 3)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 아이템 mapper 검색해서 set에 넣어줌
        List<ItemCateMapper> itemCateMappers = itemCateMapperRepo.findAllByShopIdAndCateId(shopId, cateCode);
        if (itemCateMappers.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        HashSet<Long> items = new HashSet<>();
        for (ItemCateMapper mapper : itemCateMappers){
            items.add(mapper.getItemId());
        }

        // set에서 아이템 검색
        List<ShopItem> shopItems = itemRepo.findAllById(items);
        List<ShopItemDto> dtos = new ArrayList<>();
        for (ShopItem item : shopItems){
            dtos.add(ShopItemDto.fromEntity(item));
        }
        return dtos;
    }
    // 쇼핑몰 아이템리스트 가격범위검색으로 보기, 전체가능
    public List<ShopItemDto> viewItemListByPrice(Long shopId, Integer minPrice, Integer maxPrice){
        // 개설된 쇼핑몰만 보여줌
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (shop.getShopStatus() != 3)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<ShopItem> shopItems = itemRepo.findAllByShopIdAndPriceBetween(shopId,minPrice,maxPrice);
        List<ShopItemDto> dtos = new ArrayList<>();
        for (ShopItem item : shopItems){
            dtos.add(ShopItemDto.fromEntity(item));
        }
        return dtos;
    }

    // 쇼핑몰 물건 수정하기, 해당 사업자만 가능
    public ShopItemDto updateItem(
            Long shopId, ShopItemDto dto, String username,Long itemId){
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(shop.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ShopItem shopItem = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        shopItem.updateItem(dto.getStock(),dto.getItemName(),
                dto.getComment(), dto.getPrice());
        // 아이템 카테고리 수정
        itemCateMapperRepo.deleteAllByItemId(itemId);
        List<ItemCateMapper> itemCateMappers = new ArrayList<>();
        for (int i = 0; i < dto.getCateId().size(); i++){
            itemCateMappers.add(
                    ItemCateMapper.builder()
                            .itemId(shopItem.getId())
                            .cateId(dto.getCateId().get(i))
                            .shopId(shopId)
                            .build()
            );
        }
        itemCateMapperRepo.saveAll(itemCateMappers);
        return ShopItemDto.fromEntity(itemRepo.save(shopItem));
    }

    // 쇼핑몰 물건 삭제하기, 해당 사업자만 가능
    public String deleteItem(Long shopId,String username,Long itemId){
        // authentication에서 받은 username으로 검색한 userId가 쇼핑몰의 userId와 다를경우 거부
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(shopId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(shop.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        itemRepo.deleteById(itemId);
        // 아이템에 붙어있던 mapper도 삭제
        itemCateMapperRepo.deleteAllByItemId(itemId);
        return "delete done";
    }

    // 아이템 (결제 후)구매요청하기, 활성 사용자 이상
    @Transactional
    public ShopItemOfferDto offerItem(Long itemId, ShopItemOfferDto dto, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShopItem item = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(item.getShopId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 사려는 아이템 개수가 요청하는것보다 더 적으면 오류
        if(item.getStock() < dto.getStock())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        ShopItemOffer itemOffer = ShopItemOffer.builder()
                .offerId(user.getId())
                .offeredId(shop.getUserId())
                .itemId(item.getId())
                .stock(dto.getStock())
                .price(dto.getPrice())
                .status(0)
                .build();

        itemRepo.save(item);
        return ShopItemOfferDto.fromEntity(itemOfferRepo.save(itemOffer));
    }

    // 아이템에 걸려있는 구매요청상태들(0~4번) 보기, 해당 사업자 가능
    public List<ShopItemOfferDto> offeredItem(Integer status, Long itemId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShopItem item = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShoppingMall shop = shopRepo.findById(item.getShopId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        //아이템의id로 찾은 shop의 userId와 authentication의 username으로 검색한 유저의 userId가 일치 해야 통과
        if(!shop.getUserId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        List<ShopItemOffer> itemOffers = itemOfferRepo.findAllByStatusAndItemId(status, itemId);
        if(itemOffers.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        List<ShopItemOfferDto> itemOfferDtos = new ArrayList<>();
        for(ShopItemOffer itemOffer : itemOffers){
            itemOfferDtos.add(ShopItemOfferDto.fromEntity(itemOffer));
        }
        return itemOfferDtos;
    }
    // 상품 구매 승인, 해당 사업자 가능
    @Transactional
    public ShopItemOfferDto confirmItem(Long itemOfferId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShopItemOffer itemOffer = itemOfferRepo.findById(itemOfferId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 구매신청에 걸린 offeredId 와 authentication의 username으로 검색한
        // 유저의 userId가 일치 해야 통과
        if(!itemOffer.getOfferedId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ShopItem item = itemRepo.findById(itemOffer.getItemId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 사려는 아이템 개수가 요청하는것보다 더 적으면 오류
        if(item.getStock() < itemOffer.getStock())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        item.updateStock(item.getStock() - itemOffer.getStock());
        // 거래완료 상태로 만듬
        itemOffer.setStatus(1);
        itemRepo.save(item);
        // 마지막 거래완료 시간 쇼핑몰에 저장
        ShoppingMall shop = shopRepo.findByUserId(user.getId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        shop.setLastTrans(LocalDateTime.now());
        shopRepo.save(shop);
        return ShopItemOfferDto.fromEntity(itemOffer);
    }

    @Transactional
    // 구매자가 거래 승인전 취소신청, 구매자가 구매 완료후 환불신청, 해당 활성사용자 이상 가능
    public ShopItemOfferDto cancelItemOffer(Long itemOfferId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShopItemOffer itemOffer = itemOfferRepo.findById(itemOfferId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 구매신청에 걸린 offerId 와 authentication의 username으로 검색한
        // 유저의 userId가 일치 해야 통과
        if(!itemOffer.getOfferId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // 거래 대기중상태일때 취소
        if(itemOffer.getStatus() == 0)
            itemOffer.setStatus(2);
        // 거래 완료 후 환불 신청
        else if(itemOffer.getStatus() == 1)
            itemOffer.setStatus(3);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ShopItemOfferDto.fromEntity(itemOfferRepo.save(itemOffer));
    }
    // 0, 2, 3, 번상태 4번상태로 만듬(취소하고 환불), 해당 사업자 가능
    @Transactional
    public ShopItemOfferDto rejectItem(Long itemOfferId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShopItemOffer itemOffer = itemOfferRepo.findById(itemOfferId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 구매신청에 걸린 offeredId 와 authentication의 username으로 검색한
        // 유저의 userId가 일치 해야 통과
        if(!itemOffer.getOfferedId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // 거래취소 상태로 만듬
        // 환불 요청중인경우 재고를 다시 증가시킴
        if(itemOffer.getStatus() == 3){
            ShopItem item = itemRepo.findById(itemOffer.getItemId()).orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));
            item.updateStock(item.getStock() + itemOffer.getStock());
            itemRepo.save(item);
            itemOffer.setStatus(4);
            return ShopItemOfferDto.fromEntity(itemOfferRepo.save(itemOffer));
        }
        else if((itemOffer.getStatus() == 0) ||(itemOffer.getStatus() == 2)){
            itemOffer.setStatus(4);
            return ShopItemOfferDto.fromEntity(itemOfferRepo.save(itemOffer));
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


}

