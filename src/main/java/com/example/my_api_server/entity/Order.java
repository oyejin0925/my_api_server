package com.example.my_api_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor //매개변수를 전부 받는 생성자 생성
@Table(name = "orders")
@Getter
@Builder
public class Order {

    //주문(1) <-> 주문상품(여러 상품) -> 1:N관계
    //주문(1) <-> 주문상품(N) <-> 상품(1)

    //Order가 저장되면 OrderProduct도 같이 저장된다. (생명주기를 동일하게하겠다) - CascadeType.ALL
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) //orphanRemoval: 부모-자식 관계 간 삭제옵션
            List<OrderProduct> orderProducts = new ArrayList<>(); //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    //구매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member buyer;

    //주문상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //주문시간
    @Column(nullable = false)
    private LocalDateTime orderTime;

    //정적 팩토리 패턴
    public static Order createOrder(Member member, LocalDateTime orderTime) {
        Order order = Order.builder()
                .buyer(member)
                .orderStatus(OrderStatus.PENDING)
                .orderTime(orderTime)
                .build();

        return order;
    }

    //루트 엔티티(에그리거트 루트) 내부 응집도 상승
    public OrderProduct createOrderProduct(Long orderCount, Product product) {
        return OrderProduct.builder()
                .order(this)
                .number(orderCount) //product에 맞는 주문개수를 찾는다!
                .product(product)
                .build();
    }

    //양방향 매핑
    public void addOrderProducts(List<OrderProduct> orderProduct) {
        this.orderProducts = orderProduct;
    }
}
