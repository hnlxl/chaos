package dev.teclxl.chaos.support.exception;

/**
 * 使用接口定义的特殊虚拟基类：业务服务的错误信息。
 * 
 * <h1>类型限定：</h1>
 * 
 * <ul>
 * <li><strong>当前java版本，枚举不能继承，所以不能直接定义为虚拟基类，而是使用了带默认方法的接口。</strong>接口默认方法也有局限性，所以有些虚拟类的特性还是用不了。
 * <li><strong>除了实现该接口外，还必须是枚举</strong>。
 * <li>请优先使用<strong>E extends Enum&lt?&gt & ServiceErrorInfo</strong>进行限定，其次才是<strong>ServiceErrorInfo</strong>。
 * <li>不能使用Enum&lt?&gt进行类型限定。如果有需要，可以使用下面的方式:
 * 
 * <pre>
 *     if (sumClass instanse of ServiceErrorInfo) {
 *         Enum&lt;?&gt; sumEnum = (Enum&lt;?&gt;)sumClass;
 *         .....
 *         // OR
 *         Enum&lt;CaseEnum&gt; switchableEnum = (Enum&lt;CaseEnum&gt;)sumClass;
 *         switch (switchableEnum) {
 *             ...
 *         }
 *         .....
 *     }
 * </pre>
 * </ul>
 * 
 * <h1>错误信息枚举的设计原则</h1>
 * <ul>
 * <li>具体错误信息不在单个地方专门定义，而是具体方法具体定义。
 * <li>枚举的“类”和“常量值”构成错误信息的唯一标识，这个是系统（或独立服务）内甚至系统外都是通用的。除此之外的其他内容，应当仅在系统内有效，不应当将错误信息对象原封不动的传递到系统外。
 * <li>由于上条原因，如果系统采用抛异常的方式对外输出错误信息，那么不能直接抛出（包含错误信息对象的）内部通用异常，而是捕获并转换成新异常后再抛出，新异常中不包含原来的错误信息对象。
 * <li>错误信息分段规则，请参见下文。
 * </ul>
 * <h1>错误信息分段规则</h1>
 * <ul>
 * <li>每个代码层可以定义零到多个通用级别（层内公用）错误枚举，在层级别异常旁边定义，命名为{层名称}{分组名称}Error，例如ServiceGlobalError、ServiceSystemError。
 * <li>每个接口定义可以定义零到一个接口级别（接口内公用）错误枚举，在接口内定义，命名为Error，类型是XXXService.Error。
 * <li>每个方法可以定义零到一个方法级别（方法内专用）错误枚举，也在接口内定义，命名不限，但必须在方法的javadoc中明确写出。
 * <li>三者有逻辑上的<strong>继承（但只能扩展不能覆盖）关系</strong>，通用<|--接口<|--方法。
 * <li>每个方法抛出错误信息时，只能从通用枚举、所属接口的枚举和自己定义的枚举中进行选择。如无特别说明，则通用枚举、所属接口的枚举中的所有常量都可能会被抛出。
 * </ul>
 * <h1>分段规则其他说明</h1>
 * <ul>
 * <li>该规则还收到以下限制的影响：枚举不能继承；异常不能使用泛型。
 * <li>上层捕获后进行判断时，需要先向下造型，按照方法->接口->通用的顺序。虽然理论上顺序是无所谓的（因为三个级别的继承关系是逻辑的而不是物理的），但不建议这么做。
 * <li>向下造型成具体的枚举对象后，才可以在switch语句中使用。
 * </ul>
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public interface ServiceErrorInfo {
    /**
     * 错误信息枚举的级别
     * 
     * @author Liu Xiaolei
     * @date 2018/03/26
     */
    enum Level {
        /** 通用级别 */
        GLOBAL,
        /** 接口级别 */
        INTERFACE,
        /** 方法级别 */
        METHOD
    }

    /**
     * 指示当前错误信息的级别
     * 
     * @return
     */
    Level level();

    /**
     * 指示当前具体错误信息的、未正规化的、内部使用的唯一标示。
     * 
     * 默认情况下，由以下信息构成：{错误信息的枚举常量名}-{级别的枚举常量名}-{枚举类的类名}。 例如：SOME_ERROR-INTERFACE-dev.teclxl.chaos.xxx.XxxService$Error
     * 
     * @return
     */
    default String unformalizedId() {
        Enum<?> enumThis = (Enum<?>)this;
        StringBuilder sb = new StringBuilder()
            .append(enumThis.name())
            .append("-")
            .append(this.level().name())
            .append("-")
            .append(enumThis.getDeclaringClass().getName());

        return sb.toString();
    }
}
