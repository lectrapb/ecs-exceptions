package co.com.bancolombia.api.response;

import co.com.bancolombia.model.utils.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private Meta meta;
    private Object data;
    private List<Error> errors;

    public static Mono<ApiResponse> createOnSuccess(Object response, String messageId) {
        return Mono.just(ApiResponse.builder()
                .meta(setMeta(messageId))
                .data(response)
                .build());
    }

    public static Mono<ApiResponse> createOnError(Error error, String messageId) {
        return Mono.just(ApiResponse.builder().meta(setMeta(messageId)).errors(setErrors(error)).build());
    }

    private static Meta setMeta(String messageId) {
        var date = LocalDateTime.now(ZoneOffset.of("-05:00"));
        var dateString = date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
        return Meta.builder().messageId(messageId).requestDate(dateString).build();
    }

    private static List<Error> setErrors(Error errors) {
        return List.of(errors);
    }
}
