package com.example.my_api_server.service;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//비지니스 로직이 구성됨
@Service //Bean으로 등록
@RequiredArgsConstructor //생성자 주입 DI
@Slf4j //로그
public class MemberService {

    private final MemberRepo memberRepo;

    //회원가입
    public Long signUp(String email, String password) {
        //회원 저장 후 알림 전송
        Long memberID = memberRepo.saveMember(email, password);
        log.info("회원가입 한 member ID = {}", memberID);

        //알림전송
        sendNotification();

        return memberID;
    }

    //알림: 외부 api 호출(TCP <-> HTTP 통신)
    public void sendNotification() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("알림전송완료! ");
    }


    //회원조회
    public Member findMember(Long id) {
        Member member = memberRepo.findMember(id);
        return member;
    }
}
