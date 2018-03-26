package dev.teclxl.chaos.support;

/**
 * 表示枚举或普通类是可以定义值的。这个值可以代表枚举的某个常量、或者代表类的某个实例对象——即使是在系统外部。换句话说：该接口的实例能够通过一个值进行持久化。
 * <p>
 * 一般用于表示枚举，普通类通常有更方便的持久化方式。
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 * @param <V> 代表值的类型，需要是能比较的
 */
public interface ValueDefinable<V extends Comparable<V>> {
    /**
     * 得到代表值
     * 
     * @return 代表值
     */
    public V getValue();

    /**
     * 得到附加描述或含义，非必须
     * 
     * @return 附加描述或含义
     */
    default public String getDescription() {
        return null;
    }

}
