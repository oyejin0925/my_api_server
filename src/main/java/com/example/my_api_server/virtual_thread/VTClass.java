package com.example.my_api_server.virtual_thread;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Slf4j
public class VTClass {

    static final int TASK_COUNT = 250;

    static final Duration ID_DURATION = Duration.ofSeconds(1); //i/o 작업 시뮬레이션을 위한 시간

    public static void main(String[] args) {

//        Thread vt1 = Thread.ofVirtual()
//                .name("가상스레드1")
//                .start(VTClass::ioRun); //static값이라 오류


//        // i/o의 경우 메모리를 조금 더 쓰더라도 성능 자체가 극도하게 차이남
//        log.info("[i/o]플랫폼 스레드 시작!");
//        ioRun(Executors.newFixedThreadPool(200)); //플랫폼 스레드 n개 생성
//
//        log.info("[i/o]가상 스레드 시작!");
//        ioRun(Executors.newVirtualThreadPerTaskExecutor()); //가상 스레드로 필요개수만큼 필요할 때마다 생성
//
//
//        // 가상스레드는 힙에 생성됩니다. 그로 인해서 JVM힙메모리가 많이 사용되고, GC가 더 많은 일을 해야함 (Stop The World)
//        // 결국에는 사용자가 많아질수록 cpu연산의 차이는 크게 안나고 메모리만 많이 사용하게 됨 -> 비효율적
//        log.info("[cpu]플랫폼 스레드 시작!");
//        cpuRun(Executors.newFixedThreadPool(200)); //플랫폼 스레드 n개 생성
//
//        log.info("[cpu]가상 스레드 시작!");
//        cpuRun(Executors.newVirtualThreadPerTaskExecutor()); //가상 스레드로 필요개수만큼 필요할 때마다 생성


//        log.info("[io]플랫폼 스레드 핀닝 테스트 시작!");
//        ioRunPinning(Executors.newFixedThreadPool(200));
//
//        log.info("[io]가상 스레드 핀닝 테스트 시작!");
//        ioRunPinning(Executors.newVirtualThreadPerTaskExecutor());

        log.info("[io]플랫폼 스레드 reentrantLock 핀닝 테스트 시작!");
        ioRunPinningRL(Executors.newFixedThreadPool(200));

        log.info("[io]가상 스레드 reentrantLock 핀닝 테스트 시작!");
        ioRunPinningRL(Executors.newVirtualThreadPerTaskExecutor());
    }

    public static void ioRun(ExecutorService es) {

        Instant start = Instant.now(); //실행시간 측정

        try (es) {
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(() -> {
                    try {
                        //io바운드 작업 (i/o bound)
                        //실제 외부 API 및 DB 연동 코드
                        Thread.sleep(ID_DURATION);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } //try-resource 자동으로 리소스 해제(es.close())

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void cpuRun(ExecutorService es) {

        Instant start = Instant.now(); //실행시간 측정

        try (es) {
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(() -> {
                    //cpu 연산 (cpu bound)
                    for (int i = 0; i < 1000000; i++) {
                        int a = 1;
                        int b = 2;
                        int c = a + b;
                    }
                });
            });
        } //try-resource 자동으로 리소스 해제(es.close())

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void ioRunPinning(ExecutorService es) {
        Instant start = Instant.now(); //실행시간 측정

        try (es) {
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(() -> {
                    //내부적으로 락을 사용한다고 가정
                    //synchronized 커널의 세마포어/뮤텍스 객체 동시성을 제어, SystemCall
                    // -> 플랫폼스레드가 Blocked -> 가상스레드도 일 못함
                    synchronized (es) {
                        try {
                            //io바운드 작업 (i/o bound)
                            //실제 외부 API 및 DB 연동 코드
                            Thread.sleep(ID_DURATION);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            });
        } //try-resource 자동으로 리소스 해제(es.close())

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void ioRunPinningRL(ExecutorService es) { //ReentrantLock 적용
        Instant start = Instant.now(); //실행시간 측정

        try (es) {
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(() -> {
                    ReentrantLock lock = new ReentrantLock();
                    lock.lock();
                    try {
                        //io바운드 작업 (i/o bound)
                        //실제 외부 API 및 DB 연동 코드
                        Thread.sleep(ID_DURATION);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                });
            });
        } //try-resource 자동으로 리소스 해제(es.close())

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }
}