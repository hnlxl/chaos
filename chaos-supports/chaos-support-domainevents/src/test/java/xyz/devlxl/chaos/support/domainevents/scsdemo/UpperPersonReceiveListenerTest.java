package xyz.devlxl.chaos.support.domainevents.scsdemo;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpperPersonReceiveListenerTest {
    @Autowired
    private UpperPersonBinding upperPersonBinding;

    @Rule
    public OutputCapture out = new OutputCapture();

    @Test
    public final void testHandle() {
        Message<Person> message = new GenericMessage<>(new Person().setName("XYZ"));
        upperPersonBinding.upperPersonInput().send(message);
        assertThat(out.toString(), containsString("XYZ"));
    }
}
