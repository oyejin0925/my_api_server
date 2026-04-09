package com.example.my_api_server;

import org.springframework.stereotype.Component;

//실제 스프링에게 Bean(객체)으로 등록하게 해주는 설정
//이 Bean은 IOC 컨테이너에 등록됨 (객체=물건, 단 하나만 생성 후 재사용 : 싱글톤 패턴)
@Component //Class 단위 등록
public class IOC {

    //@Bean //method 단위로 등록 (method의 return 타입, IOC_TEST를 ioc 컨테이너에 등록해준다)
    public void func1() {
        System.out.println("func1 실행");
    }

    ;

     /* public static void main() {
        //객체생성 - 메모리(RAM), JVM HEAP 메모리에 사용 - OOM(out of heap memory) 생성가능
        //스프링한테 우리가 IOC라는 객체를 만들어줄테니, 대신 하나만 만들어서 재사용해줘
        //개발자가 객체생성x 스프링 프레임워크가 관리, 필요시 스프링이 주입해줌(DI)
        IOC ioc = new IOC();

        //객체의 메서드 호출
        ioc.func1();
    } */
}
