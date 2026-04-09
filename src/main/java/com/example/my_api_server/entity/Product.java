package com.example.my_api_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Getter
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    private String productName; //상품명

    // @Column 생략가능하긴함, 보통 옵션설정할때 믾이 사용함
    private String productNumber; //상품번호(shirt-red-s-001)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType; //상품타입(의류, 음식 ..)

    private Long price; //가격(실무에서는 불변객체 만들어서 관리)

    private Long stock; //재고수량

    @Version
    private Long version; //버전

    //필요한 부분만 바꿀 수 있도록 특정한 의미있는 메서드 생성
    public void changeProductName(String changeProductName) {
        this.productName = changeProductName;
    }

    //재고
    public void increaseStock(Long addStock) {
        this.stock += addStock;
    }

    public void decreaseStock(Long subStock) {
        this.stock -= subStock;
    }

    //구매 가능 여부 확인
    //캡슐화를 하게되면 변경지점이 되게 작아진다. 코드의 유지보수(변화)가 적어지게됨
    public void buyProductWithStock(Long orderCount) {
        if (this.getStock() - orderCount < 0) {
            throw new RuntimeException("재고가 음수이니 주문 할 수 없습니다!");
        }
        this.decreaseStock(orderCount);
    }

}
