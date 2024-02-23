# Mission_YangDongHwa_ShoppingMall

## DB 구조도
 ![db설계1](https://github.com/ydh511/Mission_YangDongHwa_ShoppingMall/assets/70869505/a434d837-8e04-4f8d-92f9-8388358be8c4)

- businessGrate : 0~3까지 0이 제일 낮은거 / 0: 비활성, 1: 일반, 2: 사업자, 3: 관리자
- transactionStatus : 0~3까지 / 0: 판매중, 1: 구매제안중
- mallStatus : 0~1까지 / 0: 폐쇄, 1: 허가 안남, 2 허가 남
- itemCategory
  - cateCode : ** , ** 으로 앞 두자리는 대분류 뒤 두자리는 소분류
    - 예를들어 식품 대분류를 1, 식품대분류의의 신선식품소분류 1, 냉동식품소분류를 2
    - 가구 대분류를 2, 가구 대분류의 의자소분류1, 책상소분류를2 라고 하면 카테고리는 다음과 같다.
      - 01 01 : 식품 -> 신선식품 / 01 02 : 식품 -> 냉동식품  
      - 02 01 : 가구 -> 의자 / 02 02 : 가구 -> 의자
  - cateParent : 대분류 2자리 + 00
    - 식품 대분류에 속한 물품의 경우 cateParent는 0100
  - tire : 대분류에 속한 카테고리일 경우 1, 소분류는 2
    - 예를들어 식품 대분류 카테고리는 1, 식품에서 신선신품 소분류는 2
