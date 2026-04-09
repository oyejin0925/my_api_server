package com.example.my_api_server.repo;

import com.example.my_api_server.entity.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//db 통신 없이 간단하게 인메모리 db를 사용해서 crud를 간단하게 해보기
//DAO: DB와 통신하는 객체
@Component //Bean으로 등록
public class MemberRepo {

    Map<Long, Member> members = new HashMap<>(); //JVM Heap Memory에 올라감

    //<연산(저장, 수정, 삭제, 조회)>

    //저장
    public Long saveMember(String email, String password) {
        //db에 insert
        Random random = new Random();
        long id = random.nextLong();
        Member member = Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .build();

        members.put(id, member);
        return id;
    }

    public Member findMember(Long id) {
        return members.get(id);
    }
}