package dev.teclxl.chaos.testsupport;

/**
 * 在测试类整体执行的前后的处理是非静态的
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
