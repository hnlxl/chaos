package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.devlxl.chaos.support.domain.AbstractDomainEvent;
import xyz.devlxl.chaos.support.domain.DomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.rabbitmq.host=hyperv-lxl",
    "spring.cloud.stream.default.group=mock-delevery-confirm",
    "spring.cloud.stream.bindings.mock1.destination=xyz.devlxl.chaos.support.domainevents.notification.delivery.DeliveryServiceRealServerTest$Mock1Event",
    "spring.cloud.stream.bindings.mock2.destination=xyz.devlxl.chaos.support.domainevents.notification.delivery.DeliveryServiceRealServerTest$Mock2Event"})
@Ignore("This use case needs to connect to a real MQ server and cannot participate in automated testing.")
// 手动测试时，配置好RabbitMQ服务器，注释掉@Ignore，并在TestApplication中切换@SpringBootApplication的注释
public class DeliveryServiceRealServerTest {
    @Autowired
    private DeliveryService service;
    @Autowired
    private JpaStoredDomainEventRepository eventStoreRepo;
    @Qualifier("objectMapperOfDomainEventsSupport")
    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    @Transactional
    @Rollback
    public final void testDeliverUndelivered_realMq() throws InterruptedException, JsonProcessingException {
        appendNewEvents(1);
        appendNewEvents(1);
        appendNewEvents(1);
        appendNewEvents(1);
        appendNewEvents(2);
        appendNewEvents(2);
        appendNewEvents(2);
        appendNewEvents(3);
        appendNewEvents(3);

        service.deliverUndeliveredStroedEvent();
        Thread.sleep(1000);

        assertThat(capture.toString(), allOf(containsString("Mock1 handled"), containsString("Mock2 handled")));
    }

    private JpaStoredDomainEvent appendNewEvents(int type) throws JsonProcessingException {
        DomainEvent mockEvent = null;
        switch (type) {
            case 1:
                mockEvent = new Mock1Event();
                break;
            case 2:
                mockEvent = new Mock2Event();
                break;
            case 3:
                mockEvent = new Mock3Event();
                break;
            default:
                break;
        }
        return eventStoreRepo.save(new JpaStoredDomainEvent()
            .eventBody(objectMapper.writeValueAsString(mockEvent))
            .occurredOn(mockEvent.occurredOn())
            .className(mockEvent.getClass().getName()));
    }

    public static class Mock1Event extends AbstractDomainEvent {}

    public static class Mock2Event extends AbstractDomainEvent {}

    public static class Mock3Event extends AbstractDomainEvent {}
}
