package com.example.my_api_server.service;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.event.MemberSignUpEvent;
import com.example.my_api_server.repo.MemberDBRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor //생성자 주입 DI
@Slf4j
public class MemberDBService {

    private final MemberDBRepo memberDBRepo;
    private final MemberPointService memberPointService;
    private final ApplicationEventPublisher publisher; //이벤트를 보내줄 publisher

    //회원저장(DB에 저장)

    /* @Transactional
     * 1. @Transactional은 AOP로 돌아가서 begin tran() commit()
     * 2. DB에는 commit 명령어가 실행되어야 테이블에 반영됨 (redo, undo, log ... -> table에 저장)
     * 3. JPA의 구현체인 '하이버네이트 엔티티매니저, JDBC Driver <-> DB' 일련의 과정을 스프링이 자동으로 해줌
     */
    @Transactional
//            (rollbackFor = IOException.class, readOnly = true) //()에 쓴 예외는 런타임아니어도 롤백해줌
    public Long signUp(String email, String password) throws IOException {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        //동기 - 작업이 완료될때까지 기다림 <-> 비동기: 작업이 완료될 때까지 기다리지않음 -> 알림 전송하는 작업이 오래걸리는 경우 -> 비동기적으로 처리하는게 좋음

        //동기 & 블로킹
        Member savedMember = memberDBRepo.save(member);

        //sent noti

        //DB에 commit이 정상적으로 잘 되었을 때 메일 알림 발송하기 (실패 시 재시도 n번)
        //기능 안정성 + 예외 상황 + 알림이 몇초정도 안감 -> 전체 총 서버의 응답시간 단축?
        //비동기 & 논블로킹(Async때문)
        publisher.publishEvent(new MemberSignUpEvent(savedMember.getId(), savedMember.getEmail()));
//        sendNotification();
//        memberPointService.changeAllUserData();
//        throw new IOException("외부 API 호출하다가 I/O 예외가 터짐");
        //I/O 예외의 경우 우리문제가 아니라 상대방 문제 -> 재전송 로직 구성해야
        //DB에 저장하다가 뭔가 오류가 발생해서 예외가 터짐(Runtime 예외)
//        throw new RuntimeException("DB에 저장하다가 뭔가 오류가 발생해서 예외가 터짐");

        return savedMember.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) //requires_new: 새로운 트랜잭션 생성
    public void changeAllUserData() {
        List<Member> members = memberDBRepo.findAll();

        // 임의의 값을 변경했다고 가정

    }

    //알림 전송
    public void sendNotification() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
    }

    //Tx1 테스트 메서드
    @Transactional(propagation = Propagation.REQUIRED, timeout = 2)
    public void tx1() {
        List<Member> members = memberDBRepo.findAll();
        members.stream()
                .forEach((m) -> {
                    log.info("member id = {}", m.getId());
                    log.info("member email = {}", m.getEmail());
                });

        memberPointService.changeAllUserData(); //AOP

        memberPointService.timeout(); //timeout test
    }
}
