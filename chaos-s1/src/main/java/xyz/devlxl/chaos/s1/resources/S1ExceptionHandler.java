package xyz.devlxl.chaos.s1.resources;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.base.ErrorBody;
import xyz.devlxl.chaos.base.GlobalErrorCode;
import xyz.devlxl.chaos.s1.base.S1GeneralException;

/**
 * 微服务的统一异常处理
 * <p>
 * TODO 为公共内容提取支撑项目
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@ControllerAdvice
@Slf4j
public class S1ExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Object> errorHandler(Exception ex, WebRequest request) {
        log.error("Call rest api not success", ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        ErrorBody<Object> errorBody = getErrorBody(ex, status, request);
        return super.handleExceptionInternal(ex, errorBody.toMap(), headers, status, request);
    }

    protected ErrorBody<Object> getErrorBody(Exception ex, HttpStatus status, WebRequest request) {
        ErrorBody<Object> errorBody = new ErrorBody<>();
        boolean isMessageNecessary = false;
        boolean isCauseByApi = false;

        errorBody.setTimestamp(new Date());
        errorBody.setHttpStatus(status.value());
        errorBody.setHttpError(status.getReasonPhrase());

        if (ex instanceof S1GeneralException) {
            errorBody.setErrorCode(((S1GeneralException)ex).getErrorCode().toStandardString());
        } else if (isHandleByDefault(ex)) {
            errorBody.setErrorCode(GlobalErrorCode.IGNORABLE.toStandardString());
        } else if (ex instanceof RestClientResponseException) {
            errorBody.setErrorCode(GlobalErrorCode.CAUSE_BY_API.toStandardString());
            isCauseByApi = true;
        } else {
            errorBody.setErrorCode(GlobalErrorCode.UNCERTAIN.toStandardString());
            isMessageNecessary = true;
        }

        Object message = request.getAttribute(ErrorBody.ATTR_KEY_MSG, WebRequest.SCOPE_REQUEST);
        if (message != null) {
            errorBody.setMessage((String)message);
        } else {
            if (isMessageNecessary) {
                errorBody.setMessage(ex.toString());
            } else {
                errorBody.setMessage(ex.getMessage());
            }
        }

        Object data = request.getAttribute(ErrorBody.ATTR_KEY_DATA, WebRequest.SCOPE_REQUEST);
        if (data != null) {
            errorBody.setData(data);
        }

        if (isCauseByApi) {
            // 能够按json解析的，转换成Map后再设置，否则，直接设置成response body
            RestClientResponseException clientEx = (RestClientResponseException)ex;
            String restResponseBody = clientEx.getResponseBodyAsString();
            if (clientEx.getResponseHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)) {
                ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
                try {
                    Map<?, ?> restResponseJsonMap = objectMapper.readValue(restResponseBody, Map.class);
                    errorBody.setErrorByApi(restResponseJsonMap);
                } catch (IOException e) {
                    errorBody.setErrorByApi(restResponseBody);
                }
            } else {
                errorBody.setErrorByApi(restResponseBody);
            }
        }

        return errorBody;
    }

    /**
     * 确定某个异常是否是被默认行为处理的
     * <p>
     * 依据{@link ResponseEntityExceptionHandler#handleException(Exception, WebRequest)}的@ExceptionHandler进行判断。
     * 因此，spring版本升级时，需要注意该方法。
     * 
     * @param ex
     */
    protected boolean isHandleByDefault(Exception ex) {
        return ex instanceof HttpRequestMethodNotSupportedException
            || ex instanceof HttpMediaTypeNotSupportedException
            || ex instanceof HttpMediaTypeNotAcceptableException
            || ex instanceof MissingPathVariableException
            || ex instanceof MissingServletRequestParameterException
            || ex instanceof ServletRequestBindingException
            || ex instanceof ConversionNotSupportedException
            || ex instanceof TypeMismatchException
            || ex instanceof HttpMessageNotReadableException
            || ex instanceof HttpMessageNotWritableException
            || ex instanceof MethodArgumentNotValidException
            || ex instanceof MissingServletRequestPartException
            || ex instanceof BindException
            || ex instanceof NoHandlerFoundException
            || ex instanceof AsyncRequestTimeoutException;
    }
}
