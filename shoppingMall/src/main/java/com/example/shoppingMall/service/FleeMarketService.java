package com.example.shoppingMall.service;

import com.example.shoppingMall.fleeMarket.dto.UsedItemDto;
import com.example.shoppingMall.fleeMarket.dto.UsedItemOfferDto;
import com.example.shoppingMall.fleeMarket.entity.UsedItem;
import com.example.shoppingMall.fleeMarket.entity.UsedItemOffer;
import com.example.shoppingMall.fleeMarket.repository.UsedItemOfferRepo;
import com.example.shoppingMall.fleeMarket.repository.UsedItemRepo;
import com.example.shoppingMall.user.entity.User;
import com.example.shoppingMall.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FleeMarketService {
    private final UserRepo userRepo;
    private final UsedItemRepo itemRepo;
    private final UsedItemOfferRepo itemOfferRepo;

    // 중고물품 등록, 활성사용자 이상 가능
    public UsedItemDto makeItem(UsedItemDto dto, String authName){
        User user = userRepo.findByUsername(authName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UsedItem usedItem = UsedItem.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .comment(dto.getComment())
                .price(dto.getPrice())
                .transStatus(0)
                .roadAddress(dto.getRoadAddress())
                .build();
        return UsedItemDto.fromEntity(itemRepo.save(usedItem));
    }

    // 내가 등록한 물품 수정, 활성 이상 해당 유저만 가능
    public UsedItemDto updateItem(UsedItemDto dto, String username, Long itemId){
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 아이템이 구매제안 받았거나, 구매제안 승락했거나, 거래 완료됐으면 수정 안됨
        if((usedItem.getTransStatus() == 1) || (usedItem.getTransStatus() == 2)
                || (usedItem.getTransStatus() == 3))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // authentication에서 받은 username으로 검색한 userId가 아이템의 userId와 다를경우 수정안됨
        if (usedItem.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        usedItem.updateItem(
                dto.getTitle(),
                dto.getComment(),
                dto.getPrice(),
                dto.getRoadAddress()
        );
        return UsedItemDto.fromEntity(itemRepo.save(usedItem));
    }
    // 내가 등록한 물품 삭제, 활성 이상 해당 유저만 가능
    public UsedItemDto deleteItem(String username, Long itemId){
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 아이템이 구매제안 받았거나, 구매제안 승락했거나, 거래 완료됐으면 수정 안됨
        if((usedItem.getTransStatus() == 1) || (usedItem.getTransStatus() == 2)
                || (usedItem.getTransStatus() == 3))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // authentication에서 받은 username으로 검색한 userId가 아이템의 userId와 다를경우 수정안됨
        if (usedItem.getUserId() != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        itemRepo.delete(usedItem);
        return UsedItemDto.builder()
                .userId(usedItem.getUserId())
                .title(usedItem.getTitle())
                .comment(usedItem.getComment())
                .price(usedItem.getPrice())
                .roadAddress(usedItem.getRoadAddress())
                .build();
    }


    // 모든 중고물품 보기, 비로그인도 가능
    public List<UsedItemDto> findAllItemDesc(){
        List<UsedItem> usedItems = itemRepo.findAllByOrderByIdDesc();
        if (usedItems.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<UsedItemDto> usedItemDtoList = new ArrayList<>();
        for (UsedItem item : usedItems) {
            usedItemDtoList.add(UsedItemDto.fromEntity(item));
        }
        return usedItemDtoList;
    }

    // 중고물품 하나 보기, 비로그인도 가능
    public UsedItemDto findItem(Long id){
        UsedItem usedItem = itemRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return UsedItemDto.fromEntity(usedItem);
    }

    @Transactional
    // 중고물품 구매신청하기, 활성사용자 이상 가능
    public UsedItemOfferDto offerItem(Long itemId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 해당 아이템이 거래 대기중이거나(2), 거래 완료(3)면 신청 못함
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 내가 신청한 구매제안이 거래 제안중(1)이거나 승인(2) 됐으면 다시 신청 못함
        if(!itemOfferRepo.existsByOfferIdAndOfferStatusOrOfferIdAndOfferStatus(
                user.getId(), 2, user.getId(), 3
        ))throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if ((usedItem.getTransStatus() == 2)||(usedItem.getTransStatus() == 3))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // 해당 아이템을 거래제안을 받은 상태로 만들고 저장
        usedItem.setTransStatus(1);
        itemRepo.save(usedItem);

        UsedItemOffer usedItemOffer = UsedItemOffer.builder()
                .offerId(user.getId())
                .offeredId(usedItem.getId())
                .itemId(itemId)
                .offerStatus(1)
                .build();

        return UsedItemOfferDto.fromEntity(itemOfferRepo.save(usedItemOffer));
    }
    // 내가 등록한 아이템들 보기, 해당 활성사용자 이상 가능
    public List<UsedItemDto> findMyItems(String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<UsedItem> usedItems = itemRepo.findAllByUserIdOrderByIdDesc(user.getId());
        if(usedItems.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<UsedItemDto> usedItemDtoList = new ArrayList<>();
        for (UsedItem item : usedItems) {
            usedItemDtoList.add(UsedItemDto.fromEntity(item));
        }
        return usedItemDtoList;
    }
    // 내가 구매신청한 물품 목록 보기, 해당 활성사용자 이상 가능
    public List<UsedItemOfferDto> findMyOffers(String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<UsedItemOffer> usedItemOffers = itemOfferRepo.findAllByOfferIdOrderByIdDesc(user.getId());
        if(usedItemOffers.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<UsedItemOfferDto> itemOfferDtos = new ArrayList<>();
        for (UsedItemOffer itemOffers : usedItemOffers) {
            itemOfferDtos.add(UsedItemOfferDto.fromEntity(itemOffers));
        }
        return itemOfferDtos;
    }
    //내가 등록한 아이템에 걸려있는 구매신청들 보기.
    public List<UsedItemOfferDto> findItemOffers(Long itemId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // authentication에서 받은 username으로 검색한 userId가 아이템의 userId와 다를경우 거부
        if(!usedItem.getUserId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        List<UsedItemOffer> usedItemOffers = itemOfferRepo.findAllByItemIdOrderByIdDesc(itemId);
        if(usedItemOffers.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<UsedItemOfferDto> itemOfferDtos = new ArrayList<>();
        for (UsedItemOffer itemOffers : usedItemOffers) {
            itemOfferDtos.add(UsedItemOfferDto.fromEntity(itemOffers));
        }
        return itemOfferDtos;
    }

    @Transactional
    // 아이템에 걸려있는 구매신청 승인
    public String confirmOffer(Long itemId, Long offerId, String username){
        boolean findUser = false;
        String msg = "";

        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // authentication에서 받은 username으로 검색한 userId가 아이템의 userId와 다를경우 거부
        if(!usedItem.getUserId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // 구매신청을 승인하면 신청한 사람의 제안은 수락(2)하고, 다른 모든 제안들은 거절(0) 됨
        List<UsedItemOffer> usedItemOffers = itemOfferRepo.findAllByItemIdOrderByIdDesc(itemId);
        if (usedItemOffers.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        for (UsedItemOffer usedItemOffer : usedItemOffers){
            if (usedItemOffer.getOfferId() == offerId){
                usedItemOffer.setOfferStatus(2);
                findUser = true;
                msg = usedItemOffer.getId().toString();
            }
            else usedItemOffer.setOfferStatus(1);
        }
        if (!findUser)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        itemOfferRepo.saveAll(usedItemOffers);
        return String.format(msg + " confirmed offer");
    }

    @Transactional
    // 구매신청을 승인 받았을 떄 구매자가 구매확정을 누름, 해당 활성사용자 이상 가능
    public UsedItemDto confirmItem(Long itemId, String username){
        User user = userRepo.findByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        UsedItem usedItem = itemRepo.findById(itemId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        // authentication에서 받은 username으로 검색한 userId가 아이템의 userId와 다를경우 거부
        if(!usedItem.getUserId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        usedItem.setTransStatus(3);
        itemRepo.save(usedItem);
        return UsedItemDto.fromEntity(usedItem);
    }
}
