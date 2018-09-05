package xyz.devlxl.chaos.s1.application.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentNameChanged;

/**
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
@Slf4j
@Service
public class StudentListeners {
    @Autowired
    private DummyService dummyService;

    @TransactionalEventListener
    @Async
    public void listenStudentNameChanged(StudentNameChanged event) throws InterruptedException {
        log.info("Listener of StudentNameChanged has started! Student id is: " + event.getStudentId().getUuid());
        Thread.sleep(500);
        dummyService.exe();
        log.info("Listener of StudentNameChanged has executed!");
    }

    @Service
    public static class DummyService {
        public void exe() {}
    }
}
