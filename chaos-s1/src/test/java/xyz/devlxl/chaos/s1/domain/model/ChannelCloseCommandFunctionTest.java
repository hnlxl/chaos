package xyz.devlxl.chaos.s1.domain.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.netty.channel.embedded.EmbeddedChannel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelCloseCommandFunctionTest {
    @Autowired
    private ChannelCloseCommandRepository repo;

    @Test
    public final void test() throws InterruptedException {
        EmbeddedChannel channel = new EmbeddedChannel();
        assertTrue(channel.isOpen());

        repo.save(new ChannelCloseCommand(channel).confirm());
        repo.save(new ChannelCloseCommand(channel).confirm());
        Thread.sleep(50);

        assertFalse(channel.isOpen());
    }

}
