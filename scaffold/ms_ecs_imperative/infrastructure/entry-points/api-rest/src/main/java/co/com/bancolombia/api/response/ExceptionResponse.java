package co.com.bancolombia.api.response;

import co.com.bancolombia.model.exception.AppException;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.ConstantBusinessException;
import co.com.bancolombia.model.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;
import java.util.regex.Pattern;

@Log4j2
@ControllerAdvice
public class ExceptionResponse extends ResponseEntityExceptionHandler {
    private static final Pattern UUID_REGULAR_EXPRESSION = Pattern.compile(Constants.REGEX_UUID);


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ApiResponse> handleConflict(Exception ex, WebRequest request) {
        request.setAttribute("handledException", ex, RequestAttributes.SCOPE_REQUEST);
        var getMessageId = request.getHeader(Constants.MESSAGE_ID_HEADER);
        var generateMessageIdIfItIsInvalid = getMessageId != null && UUID_REGULAR_EXPRESSION
                .asMatchPredicate().test(getMessageId)
                ? getMessageId
                : UUID.randomUUID().toString();
        AppException appException;
        if (ex instanceof AppException appEx) {
            appException = appEx;
        } else {
            appException = new AppException(
                ConstantBusinessException.DEFAULT_EXCEPTION.getErrorCode(),
                ConstantBusinessException.DEFAULT_EXCEPTION.getMessage(), ex.getMessage());
        }
        return new HandlerResponse().createErrorResponse(appException, generateMessageIdIfItIsInvalid);
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ApiResponse> handleBusinessConflict(BusinessException ex, WebRequest request) {
        request.setAttribute("handledException", ex, RequestAttributes.SCOPE_REQUEST);
        var getMessageId = request.getHeader(Constants.MESSAGE_ID_HEADER);
        var generateMessageIdIfItIsInvalid = getMessageId != null && UUID_REGULAR_EXPRESSION
                .asMatchPredicate().test(getMessageId)
                ? getMessageId
                : UUID.randomUUID().toString();
        return new HandlerResponse().createErrorResponse(ex, generateMessageIdIfItIsInvalid);
    }

}
