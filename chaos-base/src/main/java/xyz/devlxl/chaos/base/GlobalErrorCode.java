package xyz.devlxl.chaos.base;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
public enum GlobalErrorCode implements ErrorCodeEnumerable {
    /** 可以忽略该错误代码（通常，前置的HTTP状态码已经明确了是什么错误，不再需要错误码） */
    IGNORABLE,
    /** 不确定的（包括不需要细分的错误和未知的错误，此时需要通过附加的错误信息才能确定是哪种错误。 */
    UNCERTAIN,
    /** 因为调用外部接口产生的错误 */
    CAUSE_BY_API,
    ;

    @Override
    public String parentPath() {
        return "/G";
    }
}
