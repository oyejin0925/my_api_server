package com.example.my_api_server.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@Retryable(maxRetries = 3) //알림 실패 시 재시도 3번(근데 상대방 서버 이슈이기 때문에 크게의미는 없다)
public class MemberSignUpListener {

    //알림 전송
    //아직 일꾼1이 진행(새 일꾼없음)-스레드1번이 데베의 안정성(커밋)을 확인하고 나서 로직 수행 => 시간그대로, 안정성 높임
    @Async //일꾼추가
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) //db commmit이 된 후에 이 로직 수행
    public void sendNotification(MemberSignUpEvent event) {
        log.info("member ID = {}", event.getId());
        log.info("member Email = {}", event.getEmail());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            //그래서 실패한 것들을 db에 저장했다가 나중에(특정 시간 이후에) 한번에 모아서 대량 알림 발송 -> 트랜잭션 아웃박스 패턴(실무)
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
    }
}
