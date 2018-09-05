package xyz.devlxl.chaos.s1.application.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.assertj.core.util.Throwables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import xyz.devlxl.chaos.s1.application.listener.StudentListeners;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.Student;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentId;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentNameChanged;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentNamerRereshed;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentRepository;
import xyz.devlxl.chaos.support.domain.DomainSupportException;
import xyz.devlxl.chaos.support.jpa.domain.DomainEventJpaStoreListener;
import xyz.devlxl.chaos.support.jpa.domain.JpaStoredDomainEventHelper;

/*
 * @formatter:off
 * 原子性黑盒测试结果： 
 *     >正常分支，二者都提交
 *     >聚合成功、存储失败，二者都回滚
 *     >结合save并且存储完成后发生异常，二者都回滚
 *     >未显式调用save时，二者都不提交
 * 事务追踪，断点追踪，不一定是真实情况：
 *     > 正常分支：test.before、findById（聚合重建）、store.save、aggregate.save、事务最外层、findById（结果验证）
 *     > 存储失败beforeSave分支：commit(test.before、findById)，rollback(aggregate.save、事务最外层)
 *     > 存储失败afterSave分支：commit(test.before、findById、store.save)，rollback(aggregate.save、事务最外层)
 *     > failAftersave分支：commit(test.before、findById、store.save、aggregate.save)，rollback(事务最外层)
 *     > 未save分支：test.before、findById、事务最外层（因为异常没有完成）、findById（结果验证）
 * 断点位置：AbstractPlatformTransactionManager [line: 823] - processRollback(DefaultTransactionStatus, boolean) 
 * 已确认的问题，未save时，只是提交不成功，但并没有回滚。应当在单元测试时排除所有这类型的错误。
 * @formatter:on
 */

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE",
    "logging.level.org.hibernate.type.descriptor.sql.BasicExtractor=TRACE",
    "spring.jpa.properties.hibernate.format_sql=true",
    "spring.jpa.properties.hibernate.show_sql=true",
    "spring.jpa.properties.hibernate.use_sql_comments=true"})
public class StudentTestingServiceTest {
    private final static StudentId STUDENT_ID = new StudentId(UUID.randomUUID());
    private final static String ORI_NAME = "hahahah";

    @Autowired
    private StudentRepository repo;
    @Autowired
    private StudentTestingService studentTestingService;
    @Autowired
    private JpaStoredDomainEventHelper jpaStoredDomainEventHelper;

    @SpyBean
    private StudentListeners.DummyService dummyServiceOfListener;
    @SpyBean
    private DomainEventJpaStoreListener.DummyService dummyServiceOfStore;

    @Rule
    public OutputCapture capture = new OutputCapture();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);

        studentTestingService.changeName(STUDENT_ID.getUuid(), newName);
        Thread.sleep(50);

        assertEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertTrue(jpaStoredDomainEventHelper.eventStored(expectEventBase, 500));

        assertThat(capture.toString(), containsString("Listener of StudentNameChanged has started!"));
        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has executed!")));
        Thread.sleep(600);
        assertThat(capture.toString(), containsString("Listener of StudentNameChanged has executed!"));
    }

    @Test
    public final void testChangeName_storeFail1() throws InterruptedException {
        String newName = "testChangeName_storeFail1";
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);
        doThrow(NegativeArraySizeException.class).when(dummyServiceOfStore).beforeSave();

        thrown.expect(NegativeArraySizeException.class);
        studentTestingService.changeName(STUDENT_ID.getUuid(), newName);

        verify(dummyServiceOfStore).beforeSave();

        assertNotEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertFalse(jpaStoredDomainEventHelper.eventStored(expectEventBase, 1000));

        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has started!")));
    }

    @Test
    public final void testChangeName_storeFail2() throws InterruptedException {
        String newName = "testChangeName_storeFail2";
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);
        doThrow(ArrayStoreException.class).when(dummyServiceOfStore).afterSave();

        thrown.expect(ArrayStoreException.class);
        studentTestingService.changeName(STUDENT_ID.getUuid(), newName);

        verify(dummyServiceOfStore).afterSave();

        assertNotEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertFalse(jpaStoredDomainEventHelper.eventStored(expectEventBase, 1000));

        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has started!")));
    }

    @Test
    public final void testChangeName_listenerFail() throws InterruptedException {
        String newName = "testChangeNamelistenerFail";
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);
        doThrow(NullPointerException.class).when(dummyServiceOfListener).exe();

        studentTestingService.changeName(STUDENT_ID.getUuid(), newName);
        Thread.sleep(100);

        assertEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertTrue(jpaStoredDomainEventHelper.eventStored(expectEventBase, 500));

        assertThat(capture.toString(), containsString("Listener of StudentNameChanged has started!"));
        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has executed!")));
        Thread.sleep(600);
        verify(dummyServiceOfListener).exe();
        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has executed!")));
    }

    @Test
    public final void testChangeNameNotSaveExplicitly() throws InterruptedException {
        String newName = "testChangeNameNotSaveExplicitly";
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);

        boolean isThrown = false;
        try {
            studentTestingService.changeNameNotSaveExplicitly(STUDENT_ID.getUuid(), newName);
        } catch (Exception e) {
            isThrown = true;
            assertThat(Throwables.getRootCause(e), instanceOf(DomainSupportException.class));
            assertThat(Throwables.getRootCause(e).getMessage(), containsString("Aggregates has unpublished events!"));
        }
        assertTrue(isThrown);

        assertNotEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertFalse(jpaStoredDomainEventHelper.eventStored(expectEventBase, 1000));

        assertThat(capture.toString(), not(containsString("Listener of StudentNameChanged has started!")));
    }

    @Test
    public final void testChangeNameFailAfterSave() throws InterruptedException {
        String newName = "testChangeNameFailAfterSave";
        StudentNameChanged expectEventBase = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);

        thrown.expect(ArrayIndexOutOfBoundsException.class);
        studentTestingService.changeNameFailAfterSave(STUDENT_ID.getUuid(), newName);
        Thread.sleep(100);

        assertNotEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertFalse(jpaStoredDomainEventHelper.eventStored(expectEventBase, 1000));

        assertThat(capture.toString(), not(containsString("Listener of conventional domain event has started!")));
    }

    @Test
    public final void testChangeNameAndTwoEventDemo() {
        String newName = "testChangeNameAndTwoEventDemo";
        StudentNameChanged expectEventBase1 = new StudentNameChanged(STUDENT_ID, ORI_NAME, newName);
        StudentNamerRereshed expectEventBase2 = new StudentNamerRereshed(STUDENT_ID, newName);

        studentTestingService.changeNameAndTwoEventDemo(STUDENT_ID.getUuid(), newName);

        assertEquals(repo.findById(STUDENT_ID).get().getName(), newName);
        assertTrue(jpaStoredDomainEventHelper.eventStored(expectEventBase1, 500));
        assertTrue(jpaStoredDomainEventHelper.eventStored(expectEventBase2, 500));
    }

}
