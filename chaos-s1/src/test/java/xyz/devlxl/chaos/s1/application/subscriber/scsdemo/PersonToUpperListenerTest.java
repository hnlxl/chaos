package xyz.devlxl.chaos.s1.application.subscriber.scsdemo;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonToUpperListenerTest {
    @Autowired
    private PersonBinding personBinding;

    @Autowired
    private MessageCollector messageCollector;

    @Test
    public final void testToUpper() throws JsonParseException, JsonMappingException, IOException {
        Message<Person> message = new GenericMessage<>(new Person().setName("xyz"));
        personBinding.personInput().send(message);
        @SuppressWarnings("unchecked")
        Message<String> received = (Message<String>)messageCollector.forChannel(personBinding.personOutput()).poll();
        assertThat(new ObjectMapper().readValue(received.getPayload(), Person.class).getName(), equalTo("XYZ"));
    }

}
