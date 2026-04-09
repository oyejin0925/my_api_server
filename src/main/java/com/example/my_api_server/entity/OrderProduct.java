package com.example.my_api_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //주문(1) <-> 주문상품(N) <-> 상품(1)

    //싱품
    @ManyToOne(fetch = FetchType.LAZY) //Foreign Key
    private Product product;

    //주문
    @ManyToOne(fetch = FetchType.LAZY) //fk
    private Order order;

    //주문수량
    private Long number;

}
