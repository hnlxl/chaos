/**
 * POJO区域，这里存放的是原始的“简单Java对象”，参见https://en.wikipedia.org/wiki/Plain_old_Java_object 。
 * <p>
 * 这个区域的类，通常是用来封装数据的。有时候甚至只是为了编码方便。它们可能是：
 * <ul>
 * <li>实例是系统上下文（例如HttpSession、Netty Channel）的属性，表示某一类/组数据的类。<strong>重要：不能将领域实体对象放到上下文属性中</strong>。
 * <li>多个系统之间有同一种模式的数据类，例如对应某个消息/事件的类、对应某个接口参数/返回的类。特别说明一：这些类有可能转移到独立的jar包中进行定义。
 * 特别说明二：不管在内部定义，还是外部独立的jar包中定义，它们都不是必需的；它们只是为了序列化/反序列化的方便；<strong>决不能让业务逻辑对这些类产生依赖</strong>。
 * </ul>
 * <hr>
 * The area about POJO, this area store the original "simple java object". About POJO, see
 * https://en.wikipedia.org/wiki/Plain_old_Java_object.
 * <p>
 * The classes of this area, usually used to package data. Sometimes it's just for coding convenience. They may be:
 * <ul>
 * <li>The class, what's instance is attribute of system content(e.g HttpSession, Netty Channel) ,and what indicate a
 * class/group of data. <strong>Impotent: cannot put domain entity's instance into system content's attribute</strong>
 * <li>The data class with the same pattern between multiple systems, e.g a class corresponding to a message/event, a
 * class corresponding to an interface's parameter/return. Special note one: these classes may be move to a external jar
 * for definition. Special note two: They are not required, either internally defined or externally defined in the jar ;
 * they are just for the convenience of serialization/deserialization; <strong>Never let business logic depend on these
 * classes</strong>
 * </ul>
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
package xyz.devlxl.chaos.s1.domain.pojo;