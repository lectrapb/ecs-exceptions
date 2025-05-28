package co.com.bancolombia.api.response;

import co.com.bancolombia.model.utils.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@lombok.Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    public static final String OFFSET_ID = "-05:00";
    private Meta meta;
    private Object data;
    private Integer status;
    private String title;
    private List<Error> errors;


    public ApiResponse createOnSuccess(Object response, DataResponse dataResponse) {
        return new ApiResponse()
                .setMeta(dataResponse)
                .setData(response);
    }

    public ApiResponse createOnError(Error error, Integer status, String messageId) {
        return new ApiResponse()
                .setMeta(messageId)
                .setStatus(status)
                .setTitle(status)
                .setErrors(error);
    }

    private ApiResponse setMeta(DataResponse dataResponse) {
        var localDateTime = LocalDateTime.now();
        OffsetDateTime utcDateTime = localDateTime.atOffset(ZoneOffset.of(OFFSET_ID));
        var formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        String formattedDate = utcDateTime.format(formatter);
        this.meta = Meta.builder().messageId(dataResponse.getMessageId())
                .requestDate(formattedDate).build();
        return this;
    }

    private ApiResponse setMeta(String messageId) {
        var localDateTime = LocalDateTime.now();
        OffsetDateTime utcDateTime = localDateTime.atOffset(ZoneOffset.of(OFFSET_ID));
        var formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        String formattedDate = utcDateTime.format(formatter);
        this.meta = Meta.builder().messageId(messageId).requestDate(formattedDate).build();
        return this;
    }

    private ApiResponse setData(Object result) {
        this.data = result;
        return this;
    }

    private ApiResponse setErrors(Error errors) {
        this.errors = List.of(errors);
        return this;
    }

    private ApiResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    private ApiResponse setTitle(Integer code) {
        this.title = HttpStatus.valueOf(code).getReasonPhrase();
        return this;
    }
}
