package xyz.devlxl.chaos.support.domainevents.delivery;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.rabbitmq.host=hyperv-lxl",
    "spring.cloud.stream.default.group=mock-delevery-confirm",
    "spring.cloud.stream.bindings.mock1.destination=mock.Mock1Event",
    "spring.cloud.stream.bindings.mock2.destination=mock.Mock2Event"})
@Ignore("This use case needs to connect to a real MQ server and cannot participate in automated testing.")
// 手动测试时，配置好RabbitMQ服务器，注释掉@Ignore，并在TestApplication中切换@SpringBootApplication的注释
public class DeliveryServiceRealServerTest {
    @Autowired
    private DeliveryService service;
    @Autowired
    private JpaStoredDomainEventRepository eventStoreRepo;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    @Transactional
    @Rollback
    public final void testDeliverUndelivered_realMq() throws InterruptedException {
        appendNewEvents("mock.Mock1Event");
        appendNewEvents("mock.Mock1Event");
        appendNewEvents("mock.Mock1Event");
        appendNewEvents("mock.Mock1Event");
        appendNewEvents("mock.Mock2Event");
        appendNewEvents("mock.Mock2Event");
        appendNewEvents("mock.Mock2Event");
        appendNewEvents("mock.Mock2Event");
        appendNewEvents("mock.Mock3Event");

        service.deliverUndelivered();
        Thread.sleep(1000);

        assertThat(capture.toString(), allOf(containsString("Mock1 handled"), containsString("Mock2 handled")));
    }

    private JpaStoredDomainEvent appendNewEvents(String className) {
        return eventStoreRepo.save(new JpaStoredDomainEvent()
            .eventBody("eventBody: " + className)
            .occurredOn(new Date())
            .className(className));
    }
}
