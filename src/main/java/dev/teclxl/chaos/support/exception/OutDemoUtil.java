package dev.teclxl.chaos.support.exception;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public class OutDemoUtil {

    /**
     * 转换异常为OutDemoException。
     * 
     * <p>
     * 包装方式：
     * <ul>
     * <li>如果ori类型是OutDemoException，则使用ori。
     * <li>如果ori类型是ServiceException，则复制cause和原始消息、转换errorInfo形成新的OutDemoException。
     * errorInfo按级别和名称自动适配到OutDemoException体系，级别、名字完全相同的做镜像转换，否则全部适配到默认值。
     * <li>否则，将ori作为cause包装为新的OutDemoException。
     * </ul>
     * 
     * @param ori 原始异常
     * @param errorInfoClazz 即将生成的OutDemoException中，可能的接口级别和方法级别错误信息对象对应的具体枚举类型。 按照接口-方法的顺序设置参数，按需设置，
     *        但如果原始异常对应的级别在该参数中未设置，将产生参数无效异常。
     * @return 包装后的OutDemoException
     */
    public static OutDemoException convertException(Throwable ori, Class<?>... errorInfoClazz) {
        if (ori instanceof OutDemoException) {
            return (OutDemoException)ori;
        } else if (ori instanceof ServiceException) {
            return toOutDemoException((ServiceException)ori, errorInfoClazz);
        } else {
            return new OutDemoException(ori);
        }
    }

    private static final OutDemoException toOutDemoException(ServiceException ori, Class<?>... errorInfoClazz) {
        ServiceErrorInfo oriErrorInfo = ori.getErrorInfo();
        Enum<?> oriErrorInfoEnumValue = (Enum<?>)oriErrorInfo;
        OutDemoErrorInfo newErrorInfo = null;
        switch (oriErrorInfo.level()) {
            case GLOBAL:
                // 通用级别目前只有ServiceGlobalError，不再按照类型区分。
                for (Enum<?> oneGlobalLevelEnumValue : OutDemoGlobalError.class.getEnumConstants()) {
                    if (oneGlobalLevelEnumValue.name().equals(oriErrorInfoEnumValue.name())) {
                        newErrorInfo = (OutDemoErrorInfo)oneGlobalLevelEnumValue;
                        break;
                    }
                }
                break;
            case INTERFACE:
                checkArgument(errorInfoClazz.length >= 1);
                checkNotNull(errorInfoClazz[0]);
                checkArgument(errorInfoClazz[0].isEnum() && OutDemoErrorInfo.class.isAssignableFrom(errorInfoClazz[0]));
                @SuppressWarnings("unchecked")
                Class<Enum<?>> interfaceLevelEnumClz = (Class<Enum<?>>)errorInfoClazz[0];

                for (Enum<?> oneInterfaceLevelEnumValue : interfaceLevelEnumClz.getEnumConstants()) {
                    if (oneInterfaceLevelEnumValue.name().equals(oriErrorInfoEnumValue.name())) {
                        newErrorInfo = (OutDemoErrorInfo)oneInterfaceLevelEnumValue;
                        break;
                    }
                }
                break;
            case METHOD:
                checkArgument(errorInfoClazz.length >= 2);
                checkNotNull(errorInfoClazz[1]);
                checkArgument(errorInfoClazz[1].isEnum() && OutDemoErrorInfo.class.isAssignableFrom(errorInfoClazz[1]));
                @SuppressWarnings("unchecked")
                Class<Enum<?>> methodLevelEnumClz = (Class<Enum<?>>)errorInfoClazz[1];
                for (Enum<?> oneMethodLevelEnumValue : methodLevelEnumClz.getEnumConstants()) {
                    if (oneMethodLevelEnumValue.name().equals(oriErrorInfoEnumValue.name())) {
                        newErrorInfo = (OutDemoErrorInfo)oneMethodLevelEnumValue;
                        break;
                    }
                }
                break;
            default:
                break;
        }

        if (ori.getCause() == null && ori.getOriginalMessage() == null) {
            return new OutDemoException(newErrorInfo);
        } else if (ori.getCause() != null) {
            return new OutDemoException(newErrorInfo, ori.getCause());
        } else if (ori.getOriginalMessage() != null) {
            return new OutDemoException(newErrorInfo, ori.getOriginalMessage());
        } else {
            return new OutDemoException(newErrorInfo, ori.getOriginalMessage(), ori.getCause());
        }
    }
}
