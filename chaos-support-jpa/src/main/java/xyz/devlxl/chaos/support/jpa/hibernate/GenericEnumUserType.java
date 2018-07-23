package xyz.devlxl.chaos.support.jpa.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.BasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import xyz.devlxl.chaos.base.ValueDefinable;
import xyz.devlxl.chaos.base.ValueDefinableEnums;

/**
 * 通用的枚举用户定制类型，映射Java枚举与数据库普通列。
 * <h1>参数</h1>
 * <ul>
 * <li>enumClass，必须。枚举的类型，需要满足以下条件中的一个：
 * <ul>
 * <li>实现了{@link ValueDefinable}。后续将以<quote>可定义值枚举</quote>进行表示。
 * <li>有一个方法<code>public V persistentValueMethodName()</code>，其中V的类型要符合Hibernate基本映射类型。该方法用于获取枚举的持久化值，通常方法名是value或getValue。
 * 有一个静态方法<code>public static E rebuildMethodName(V)</code>，其中V的含义同上，E是enumClass的类型。该方法用于从持久化值重建枚举，通常方法名是valueOf。
 * 后续将以<quote>普通枚举</quote>进行表示。
 * </ul>
 * <li>persistentValueMethod，非必须。可定义值枚举忽略该参数，普通枚举默认getValue。获取枚举的持久化值的方法。
 * <li>rebuildMethod，非必须。可定义值枚举忽略该参数，普通枚举默认valueOf。从持久化值重建枚举的方法。
 * <li>
 * </ul>
 * <p>
 * 配置完成后，枚举将关联上一个Hibernate基本映射类型（由ValueDefinable的泛型参数类型或者persistentValueMethod的返回类型自动得出），通过该基本映射类型来做持久化和反持久化。
 * 
 * <h1>Example one, an enum type represented by an Integer value with using ValueDefinable</h1>
 * 
 * <code><pre>
 * public enum SimpleValueDefinableNumber implements ValueDefinable&lt;Integer&gt;{ 
 *     Zero(0), One(1), Two(2), Three(3); 
 *     Integer value; 
 *     protected SimpleNumber(int value) { 
 *         this.value = value; 
 *     } 
 *     
 *     public Integer getValue() { return value; } 
 * }
 * 
 * public class SomeEntity{
 *     ...
 *     
 *     &#64;Type(type = ".....GenericEnumUserType",
        parameters = {
            &#64;Parameter(name = "enumClass", value = ".....SimpleValueDefinableNumber")
        })
 *     protected SimpleValueDefinableNumber someAttribute;
 * }
 * </pre></code>
 * 
 * <h1>Example two, an enum type represented by an Integer value without using ValueDefinable</h1>
 * 
 * <h2>Enum</h2> <code><pre>
 * public enum SimpleNumber { 
 *     Unknown(-1), Zero(0), One(1), Two(2), Three(3); 
 *     Integer value; 
 *     protected SimpleNumber(int value) { 
 *         this.value = value; 
 *     } 
 *     
 *     public Integer toInt() { return value; } 
 *     
 *     public static SimpleNumber fromInt (Integer value) { 
 *          switch(value) { 
 *              case 0: return Zero; 
 *              case 1: return One; 
 *              case 2: return Two; 
 *              case 3: return Three; 
 *              default: return Unknown;// or return null, or throw Exception 
 *     }
 * }
 * 
 * public class SomeEntity{
 *     ...
 *     
 *     &#64;Type(type = ".....GenericEnumUserType",
        parameters = {
            &#64;Parameter(name = "enumClass", value = ".....SimpleNumber"),
            &#64;Parameter(name = "persistentValueMethod", value = "toInt"),
            &#64;Parameter(name = "rebuildMethod", value = "fromInt")
        })
 *     protected SimpleNumber someAttribute;
 * }
 * </pre></code>
 * 
 * 
 * <h1>代码来源</h1>
 * <p>
 * <a href="https://developer.jboss.org/wiki/Java5EnumUserType">https://developer.jboss.org/wiki/Java5EnumUserType</a>
 * <p>
 * 修改点：
 * <ul>
 * <li>调整了变量命名。
 * <li>匹配新版本Hibernate。
 * <li>使用Java 8。
 * <li>额外支持{@link ValueDefinable}。
 * <ul>
 * <li>对于实现ValueDefinable的枚举，定义时不用提供valueOf方法，配置时不用配置value、valueOf的方法名
 * <li>nullSafeSet将正常调用{@link ValueDefinable#getValue()}，
 * <li>nullSafeGet将反射调用{@link ValueDefinableEnums#valueOf(Class, Comparable)}。
 * <blockquote>反射调用的原因是：该方法第一个参数要求传入的变量是Class&lt;? extends Enum & ValueDefinable&gt;类型，但不能定义出来一个该类型的变量。
 * 只有明确的参数——例如SomeClass.class、Class&lt;SomeClass&gt;类型的变量——才有可能满足要求。
 * <strong>Object、Class&lt;?&gt;、Class&lt;Enum&gt;、Class&lt;ValueDefinable&gt;、Class&lt;? extends Enum&gt;</strong>
 * <strong>或者Class&lt;?extendsValueDefinable&gt;</strong>都不满足要求——类型即继承了Enum又实现了ValueDefinable。
 * enmuClass是通过Class.forName在运行时获取的变量，因为是变量所以无法满足方法调用要求，只能在运行时反射调用方法。</blockquote>
 * </ul>
 * </ul>
 * 
 * <h1>问题</h1>
 * <ul>
 * <li>hibernate新版本中，sqlTypes的长度不再明确地是1，nullSafeSet可能出问题，详见该方法的注释。
 * <li>实现ValueDefinable的枚举，源码中implements时必须首先写ValueDefinable
 * </ul>
 * 
 * <h1>计划</h1>
 * <ul>
 * <li>如果可能的话，取消enumClass参数，该参数的值与属性的Java类型是完全一样的。这种实现需要@Type的解析器的支持，也就是需要Hibernate框架提供支持。
 * <li>如果缩小一下支持的范围——Java端是实现了ValueDefinable特定子接口（例如IntegerValueDefinableEnum）的类型，数据库端是单列并且类型确定，则可以使用另外一种更方便的方式。
 * 继承AbstractSingleColumnStandardBasicType，注册为基本类型，在包上添加注解：@TypeDef(name = "xx-value-definable-enum", defaultForType =
 * xxValueDefinableEnume.class, typeClass = xxValueDefinableEnumType.class )。完成上述配置后，则实体中就不需要加任何注释了。
 * </ul>
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public class GenericEnumUserType implements UserType, ParameterizedType {
    private static final String DEFAULT_PERSISTENT_VALUE_METHOD_NAME = "getValue";
    private static final String DEFAULT_REBIND_METHOD_NAME = "valueOf";
    private static final String UTIL_REBUILD_METHOD_NAME = "valueOf";

    private Class<?> originalEnumClass;
    @SuppressWarnings("rawtypes")
    private Class<? extends Enum> classSubOfEnum;
    @SuppressWarnings("rawtypes")
    private Class<? extends ValueDefinable> classSubOfValueDefinable;

    private boolean isValueDefinableEnum = false;
    private BasicType persistentValueType;

    private Method persistentValueMethod;
    private Method rebuildMethod;
    private Method utilRebuildMethod;

    @Override
    public void setParameterValues(Properties parameters) {
        // 设置类型描述，包括父类型为枚举的类型，和可能有的、父类型为ValueDefinable的类型
        String enumClassName = parameters.getProperty("enumClass");
        try {
            originalEnumClass = Class.forName(enumClassName);
            classSubOfEnum = originalEnumClass.asSubclass(Enum.class);
            if (ValueDefinable.class.isAssignableFrom(originalEnumClass)) {
                isValueDefinableEnum = true;
                classSubOfValueDefinable = originalEnumClass.asSubclass(ValueDefinable.class);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new HibernateException("Enum class " + enumClassName + " is not found or cast error", e);
        }

        // 设置持久化值的类型，如果枚举不是ValueDefinable，则额外设置“获取持久化值”的方法
        Class<?> persistentValueClass = null;
        if (isValueDefinableEnum) {
            // 在第几个位置声明实现了ValueDefinable
            // 将来有可能扩展成可配置的，但目前无需理会。绝大多数情况下，枚举值只会实现一个接口，就是ValueDefinable
            int whichPositionToDeclareImplements = 1;
            persistentValueClass
                = (Class<?>)((java.lang.reflect.ParameterizedType)classSubOfValueDefinable
                    .getGenericInterfaces()[whichPositionToDeclareImplements - 1])
                        .getActualTypeArguments()[0];
        } else {
            String persistentMethodName
                = parameters.getProperty("persistentValueMethod", DEFAULT_PERSISTENT_VALUE_METHOD_NAME);
            try {
                persistentValueMethod = originalEnumClass.getMethod(persistentMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new HibernateException(
                    "Failed to obtain method of get persistent value, when enum is not ValueDefinable",
                    e);
            }
            persistentValueClass = persistentValueMethod.getReturnType();
        }

        // 设置持久化值的Hibernate映射类型
        persistentValueType = new TypeResolver().basic(persistentValueClass.getName());
        if (persistentValueType == null) {
            throw new HibernateException("Unsupported value type " + persistentValueClass.getName());
        }

        // 提前设置“从持久化值得到枚举常量”的方法，以便于在服务器启动时就能发现问题。
        if (isValueDefinableEnum) {
            try {
                utilRebuildMethod
                    = ValueDefinableEnums.class.getMethod(UTIL_REBUILD_METHOD_NAME, Class.class, Comparable.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new HibernateException("Failed to obtain rebuild method of ValueDefinableEnums", e);
            }
        } else {
            String rebuildMethodName = parameters.getProperty("rebuildMethod", DEFAULT_REBIND_METHOD_NAME);
            try {
                rebuildMethod = classSubOfEnum.getMethod(rebuildMethodName, persistentValueClass);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new HibernateException("Failed to obtain rebuild method, when enum is not ValueDefinable", e);
            }
        }
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
        throws HibernateException, SQLException {
        Object valueFromDb = persistentValueType.nullSafeGet(rs, names, session, owner);
        if (rs.wasNull()) {
            return null;
        }

        if (isValueDefinableEnum) {
            try {
                // 在设置参数时，已经确定了originalEnumClass是符合要求的。
                // valueFromDb是通过persistentValueType.nullSafeGet获取的，如果能走到这里，就也是符合要求的。
                return utilRebuildMethod.invoke(null, originalEnumClass, valueFromDb);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // 发布后，该段代码应当是永远跑不到的，即使是测试。
                throw new HibernateException("Failed to invoke rebuild method of ValueDefinableEnums", e);
            }
        } else {
            try {
                // valueFromDb的合法性同上
                return rebuildMethod.invoke(null, valueFromDb);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new HibernateException(
                    "Failed to invoke rebuild method [" + rebuildMethod.getName() + "] "
                        + "of enumeration class [" + classSubOfEnum.getName() + "]",
                    e);
            }
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object objectInEntity, int index,
        SharedSessionContractImplementor session)
        throws HibernateException, SQLException {
        if (objectInEntity == null) {
            // 已确认数据库是MySql时没问题（mysql忽略第二个参数），其他数据库未知。
            // 抛出SQLException也不一定是因为第二个参数定义无效。
            // 目前无法找到完美方法。
            st.setNull(index, persistentValueType.sqlTypes(null)[0]);
        } else {
            if (isValueDefinableEnum) {
                persistentValueType.nullSafeSet(st, ((ValueDefinable<?>)objectInEntity).getValue(), index, session);
            } else {
                try {
                    Object valueToDb = persistentValueMethod.invoke(objectInEntity);
                    persistentValueType.nullSafeSet(st, valueToDb, index, session);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new HibernateException(
                        "Failed to invoke get persistent value method [" + persistentValueMethod.getName() + "] "
                            + "of enumeration class [" + classSubOfEnum.getName() + "]",
                        e);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class returnedClass() {
        return originalEnumClass;
    }

    @Override
    public int[] sqlTypes() {
        return persistentValueType.sqlTypes(null);
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
