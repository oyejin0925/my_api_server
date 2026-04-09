package com.example.my_api_server.service;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.repo.MemberDBRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberPointService {
    private final MemberDBRepo memberDBRepo;

    //AOP가 실행되면서 ()안 옵션값들을 설정해서 트랜잭션 생성(begin)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    //requires_new: 새로운 트랜잭션 생성
    public void changeAllUserData() {
        List<Member> members = memberDBRepo.findAll();

        // 임의의 값을 변경했다고 가정
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void supportTxTest() {
        //
        memberDBRepo.findAll();
    }


    //timeout은 트랜잭션의 총 실행시간을 제한, 시간 범위 내에서 총 실행시간이 걸린다면 예외 발생
    @Transactional(timeout = 2)
    public void timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        memberDBRepo.findAll();
    }

}
