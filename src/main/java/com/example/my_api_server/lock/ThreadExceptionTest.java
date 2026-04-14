package com.example.my_api_server.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExceptionTest {

    private int count = 0;

    public static void main(String[] args) {

        ThreadExceptionTest t = new ThreadExceptionTest();
        int threadCount = 10000;
        //n개를 커널로부터 생성한 뒤, 반납하지 않음 //미리 만들어 둔 후 재사용하자~
        ExecutorService es = Executors.newFixedThreadPool(threadCount); //n개의 플랫폼 스레드 생성

        //unable to create native thread: possibly out of memory or process/resource limits reached
        //os는 프로세스마다 자원(메모리, 스레드, 소켓 등등)의 제한량(limit)을 둔다
        for (int i = 0; i < threadCount; i++) {
            es.submit(t::increase);
        }

        es.shutdown();

        System.out.println("실행완료!");
    }

    public void increase() {
        count++;
    }
}
