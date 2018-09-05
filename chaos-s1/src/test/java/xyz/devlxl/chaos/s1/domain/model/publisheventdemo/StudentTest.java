package xyz.devlxl.chaos.s1.domain.model.publisheventdemo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Liu Xiaolei
 * @date 2018/09/01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
    private final static StudentId STUDENT_ID = new StudentId(UUID.randomUUID());
    private final static String ORI_NAME = "haha";

    @Autowired
    private StudentRepository repo;

    @Before
    public void before() {
        repo.save(new Student().setId(STUDENT_ID).setName(ORI_NAME));
    }

    public void after() {
        repo.deleteById(STUDENT_ID);
    }

    @Test
    public final void testChangeName() throws InterruptedException {
        String newName = "testChangeName";
        Student student = repo.findById(STUDENT_ID).get();
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);

        Thread.sleep(50);
        student.changeName(newName);

        assertFalse(student.hasCapturedEvent(expectEventBase, 0));
        assertTrue(student.hasCapturedEvent(expectEventBase, 200));
    }

    @Test
    public final void testChangeNameAndTwoEventDemo() throws InterruptedException {
        String newName = "testRefreshName";
        Student student = repo.findById(STUDENT_ID).get();
        StudentNameChanged expectEventBaseChanged = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);
        StudentNamerRereshed expectEventBaseRefreshed = new StudentNamerRereshed(STUDENT_ID, newName);

        Thread.sleep(50);
        student.changeNameAndTwoEventDemo(newName);

        assertFalse(student.hasCapturedEvent(expectEventBaseChanged, 0));
        assertTrue(student.hasCapturedEvent(expectEventBaseChanged, 200));
        assertFalse(student.hasCapturedEvent(expectEventBaseRefreshed, 0));
        assertTrue(student.hasCapturedEvent(expectEventBaseRefreshed, 200));
    }
}
