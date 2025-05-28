package co.com.bancolombia.api.response;

import co.com.bancolombia.model.exception.AppException;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HandlerResponse {
    public ResponseEntity<ApiResponse> createSuccessResponse(Object bodyResponse,
                                                             DataResponse dataResponse) {
        var apiResponse = new ApiResponse().createOnSuccess(bodyResponse, dataResponse);
        return ResponseEntity
                .status(Integer.parseInt(dataResponse.getStatus()))
                .headers(buildHeaderResponse(apiResponse))
                .body(apiResponse);
    }

    public ResponseEntity<ApiResponse> createErrorResponse(BusinessException exception,
                                                           String messageId) {
        var apiResponse = new ApiResponse().createOnError(Error.builder()
                .code(exception.getConstantBusinessException().getErrorCode())
                .detail(exception.getConstantBusinessException().getMessage())
                .build(), exception.getConstantBusinessException().getStatus(), messageId);
        return ResponseEntity
                .status(exception.getConstantBusinessException().getStatus())
                .headers(buildHeaderResponse(apiResponse))
                .body(apiResponse);
    }

    public ResponseEntity<ApiResponse> createErrorResponse(AppException exception, String messageId) {
        var apiResponse = new ApiResponse().createOnError(Error.builder()
                .code(exception.getCode())
                .detail(exception.getMessage())
                .build(), 500, messageId);
        return ResponseEntity
                .status(500)
                .headers(buildHeaderResponse(apiResponse))
                .body(apiResponse);
    }

    private HttpHeaders buildHeaderResponse(ApiResponse apiResponse) {
        var headers = new HttpHeaders();
        headers.add(Constants.MESSAGE_ID_HEADER, apiResponse.getMeta().getMessageId());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.bancolombia.v4+json; charset=utf-8");
        headers.add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add(HttpHeaders.ALLOW, "GET, POST, OPTIONS");
        headers.add(HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, max-age=0, no-transform");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add("Content-Security-Policy", "default-src 'self'; object-src 'none'; " +
                "script-src 'self' 'unsafe-eval'; script-src-elem 'self'; frame-ancestors 'none'");

        return headers;
    }
}
