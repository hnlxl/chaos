package xyz.devlxl.chaos.base.test;

/**
 * 在测试类整体执行的前后的处理是非静态的
 * 
 * @TODO 2018/3/29发现问题：实际执行时，每个测试方法都有自己的宿主对象，在前置方法中初始化普通成员，只有第一个执行的方法的对象被设置了，这是个无效操作，该方式需要重新命名。 <br>
 *       该方式仍然能够支持依赖注入和静态成员变量，只要它们在测试方法中不会被改变。<br>
 *       用例之间不能产生交叉影响：给多个用例共享的变量，不能在用例执行时被改变；用例共用的外部数据空间，需要进行合理的隔离措施。<br>
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public interface BeforeAfterClassNonStatic {
    /**
     * 类的所有测试方法执行前要执行一次的设置方法
     * 
     * @throws Exception
     */
    default void setUpBeforeClass() throws Exception {
        /* no-op */
    }

    /**
     * 类的所有测试方法执行后要执行的清理方法。
     * <p>
     * <strong>注意：该方法有限制，不能声明throws Exception。这是架构上的限制，暂时无法解决。</strong>
     */
    default void tearDownAfterClass() {
        /* no-op */
    }

}
