package xyz.devlxl.chaos.s1.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisEntityDemoTest {
    @Autowired
    private RedisEntityDemoRepository repo;

    @Test
    public final void test() {
        String idStr = "9ijr578i";
        RedisEntityDemoId id = new RedisEntityDemoId(idStr);
        String name = "965d;'/.,mnb";
        String newName = "qjpygfhj";

        RedisEntityDemo entity1 = new RedisEntityDemo().setId(id).setName(name);
        assertFalse(repo.existsById(id));

        repo.save(entity1);
        assertTrue(repo.existsById(id));

        RedisEntityDemo entity2 = repo.findById(id).get();
        assertEquals(entity1.getName(), entity2.getName());

        entity2.setName(newName);
        repo.save(entity2);
        assertEquals(repo.findById(id).get().getName(), newName);

        repo.deleteById(id);
        assertFalse(repo.existsById(id));
    }
}
