package dev.teclxl.chaos.support.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.contains;

import javax.xml.ws.WebServiceException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OutDemoUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConvertException_原异常不用转换() {
        thrown.expect(OutDemoException.class);
        thrown.expectCause(is(instanceOf(OutDemoException.class)));
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(new OutDemoException(new OutDemoException()));
    }

    @Test
    public void testConvertException_普通异常仅包装() {
        thrown.expect(OutDemoException.class);
        thrown.expectCause(is(instanceOf(NullPointerException.class)));
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(new NullPointerException());
    }

    @Test
    public void testConvertException_复制属性_各重载方法属性复制_1() {
        thrown.expect(OutDemoException.class);
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        thrown.expectCause(is(nullValue(Throwable.class)));
        thrown.expectMessage(contains("original message is null"));
        throw OutDemoUtil.convertException(new ServiceException());
    }

    @Test
    public void testConvertException_复制属性_各重载方法属性复制_2() {
        thrown.expect(OutDemoException.class);
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        thrown.expectCause(is(instanceOf(ArrayIndexOutOfBoundsException.class)));
        thrown.expectMessage(contains("original message is null"));
        throw OutDemoUtil.convertException(new ServiceException(new ArrayIndexOutOfBoundsException()));
    }

    @Test
    public void testConvertException_复制属性_各重载方法属性复制_3() {
        thrown.expect(OutDemoException.class);
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        thrown.expectCause(is(nullValue(Throwable.class)));
        thrown.expectMessage(contains("original message is : constructorOverloaded_3"));
        throw OutDemoUtil.convertException(new ServiceException("constructorOverloaded_3"));
    }

    @Test
    public void testConvertException_复制属性_各重载方法属性复制_4() {
        thrown.expect(OutDemoException.class);
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        thrown.expectCause(is(instanceOf(WebServiceException.class)));
        thrown.expectMessage(contains("original message is : constructorOverloaded_4"));
        throw OutDemoUtil.convertException(new ServiceException("constructorOverloaded_4", new WebServiceException()));
    }

    @Test
    public void testConvertException_适配错误信息_通用级别_能适配_l() {
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(new ServiceException(ServiceGlobalError.NO_CARE));
    }

    @Test
    public void testConvertException_适配错误信息_通用级别_不能适配() {
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(new ServiceException(ForTestGlobalError.ERROR_S));
    }

    @Test
    public void testConvertException_适配错误信息_接口级别_能适配_1() {
        thrown.expect(hasProperty("errorInfo", equalTo(ForTestOutDemoService.Error.I_1)));
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.Error.I_1),
            ForTestOutDemoService.Error.class);
    }

    @Test
    public void testConvertException_适配错误信息_接口级别_不能适配() {
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.Error.I_2),
            ForTestOutDemoService.Error.class);
    }

    @Test
    public void testConvertException_适配错误信息_方法级别_能适配() {
        thrown.expect(hasProperty("errorInfo", equalTo(ForTestOutDemoService.ErrorMethod1.M_1)));
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.ErrorMethod1.M_1),
            null,
            ForTestOutDemoService.ErrorMethod1.class);
    }

    @Test
    public void testConvertException_适配错误信息_方法级别_不能适配() {
        thrown.expect(hasProperty("errorInfo", equalTo(OutDemoGlobalError.NO_CARE)));
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.ErrorMethod1.M_2),
            null,
            ForTestOutDemoService.ErrorMethod1.class);
    }

    @Test
    public void testConvertException_适配错误信息_参数无效_1() {
        thrown.expect(IllegalArgumentException.class);
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.Error.I_1),
            String.class);
    }

    @Test
    public void testConvertException_适配错误信息_参数无效_2() {
        thrown.expect(IllegalArgumentException.class);
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.Error.I_1),
            ServiceErrorInfo.Level.class);
    }

    @Test
    public void testConvertException__适配错误信息_参数无效_3() {
        thrown.expect(IllegalArgumentException.class);
        throw OutDemoUtil.convertException(
            new ServiceException(ForTestService.ErrorMethod1.M_1),
            ServiceErrorInfo.Level.class,
            ServiceErrorInfo.Level.class);
    }

    public static enum ForTestGlobalError implements ServiceErrorInfo {
        ERROR_S;

        @Override
        public Level level() {
            return ServiceErrorInfo.Level.GLOBAL;
        }
    }

    public static interface ForTestService {
        enum Error implements ServiceErrorInfo {
            I_1, I_2;

            @Override
            public Level level() {
                return ServiceErrorInfo.Level.INTERFACE;
            }

        }

        enum ErrorMethod1 implements ServiceErrorInfo {
            M_1, M_2;

            @Override
            public Level level() {
                return ServiceErrorInfo.Level.METHOD;
            }

        }
    }

    public static interface ForTestOutDemoService {
        enum Error implements OutDemoErrorInfo {
            I_1;

            @Override
            public Level level() {
                return OutDemoErrorInfo.Level.INTERFACE;
            }

        }

        enum ErrorMethod1 implements OutDemoErrorInfo {
            M_1;

            @Override
            public Level level() {
                return OutDemoErrorInfo.Level.METHOD;
            }

        }
    }

}
