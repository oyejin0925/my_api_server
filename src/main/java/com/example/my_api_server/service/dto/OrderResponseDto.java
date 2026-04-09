package com.example.my_api_server.service.dto;

import com.example.my_api_server.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of") //
@Builder
public class OrderResponseDto { //사용자에게 알려줄 정보

    //주문완료시간
    private LocalDateTime orderCompletedTime;

    //주문상태
    private OrderStatus orderStatus;

    //주문성공여부
    private boolean inSuccess;


}
