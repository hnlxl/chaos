package xyz.devlxl.chaos.base.test;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public class BeforeAfterClassNonStaticSpringRunner extends SpringJUnit4ClassRunner {
    private BeforeAfterClassNonStatic beforeAfterClassNonStaticTest;

    public BeforeAfterClassNonStaticSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        if (test instanceof BeforeAfterClassNonStatic && beforeAfterClassNonStaticTest == null) {
            beforeAfterClassNonStaticTest = (BeforeAfterClassNonStatic)test;
            beforeAfterClassNonStaticTest.setUpBeforeClass();
        }
        return test;
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        if (beforeAfterClassNonStaticTest != null) {
            beforeAfterClassNonStaticTest.tearDownAfterClass();
        }
    }
}
