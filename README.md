# Mission_YangDongHwa_ShoppingMall
## Property
src/main/resources 의 application.yaml 설정에있는 spring.profiles.active: home으로 된걸 git으로 바꾸고 application-git.yaml에 키값을 넣고 실행한다.

## DB 구조도
 ![db설계1](https://github.com/ydh511/Mission_YangDongHwa_ShoppingMall/assets/70869505/a434d837-8e04-4f8d-92f9-8388358be8c4)

- businessGrate : 0~3까지 0이 제일 낮은거 / 0: 비활성, 1: 일반, 2: 사업자, 3: 관리자
- transactionStatus : 0~3까지 / 0: 판매중, 1: 구매제안중
- mallStatus : 0~1까지 / 0: 폐쇄, 1: 허가 안남, 2 허가 남
- itemCategory :
  - cateCode : ** , ** 으로 앞 두자리는 대분류 뒤 두자리는 소분류
    - 예를들어 대분류 "식품" 을 1, "식품" 대분류의 소분류 "신선식품" 은 1, 소분류 "냉동식품" 을 2
    - 대분류 "가구" 를 2, "가구" 대분류의 소분류 "의자" 를1, 소분류 "책상" 을 2 라고 하면 카테고리는 다음과 같다.
      - 01 00 : 식품 / 01 01 : 식품 -> 신선식품 / 01 02 : 식품 -> 냉동식품  
      - 02 00 : 가구 / 02 01 : 가구 -> 의자 / 02 02 : 가구 -> 책상
  - cateParent : 대분류 2자리 + 00
    - "식품" 대분류에 속한 소분류 물품(0101, 0102)의 경우 cateParent는 0100
    - "식품" 대분류의 cateParent는 없다(null)
  - tire : 대분류에 속한 카테고리일 경우 1, 소분류는 2
    - 예를들어 "식품" 과 "가구" 대분류는 1. "신선신품" , "냉동식품" , "의자" , "책상" 소분류는 2
