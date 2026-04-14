package com.example.my_api_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    // i/o bound
    @Bean("ioExecutor")
    public ExecutorService ioExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }


    // 스레드 개수는 context switch cost에 비례하기 때문에 막 넣으면 안됨 
    // cpu bound
    @Bean("cpuExecutor")
    public ExecutorService cpuExecutor() {
        //cpu 개수 확인
        //맥 인텔칩(하이퍼스레딩 O), 애플 칩(하이퍼스레딩 X) -> 인텔쓰면 코어 *2
        int coreCount = Runtime.getRuntime().availableProcessors();

        //그래서 general한 경우 intel은 coreCount로 설정하는게 조음
        return Executors.newFixedThreadPool(100);

    }

}
