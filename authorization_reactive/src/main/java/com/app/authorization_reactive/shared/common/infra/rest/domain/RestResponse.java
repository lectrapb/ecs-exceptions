package com.app.authorization_reactive.shared.common.infra.rest.domain;


import com.app.authorization_reactive.shared.common.domain.exception.BusinessException;
import lombok.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class RestResponse {

    private static final String API_PATH = "/api/monetary_limit";
    private static final String YYYY_MM_DD = "yyyy/MM/dd";
    private Meta meta;
    private ErrorApi error;

   private RestResponse(Meta meta, ErrorApi error) {
        this.meta = meta;
        this.error = error;
    }

    private RestResponse(Meta meta) {
        this.meta = meta;
    }

    @SneakyThrows
    public static RestResponse okCommand()  {

        var meta = setMeta();
        return new RestResponse(meta);
    }


    @SneakyThrows
    public static RestResponse error(BusinessException exception)  {

        var meta = setMeta();
        var error = new ErrorApi();
        error.code = exception.getConstantBusinessException().getErrorCode();
        error.message = exception.getConstantBusinessException().getMessage();
        return new RestResponse(meta, error);
    }

    private static Meta setMeta() {
        var sdf = DateTimeFormatter.ofPattern(YYYY_MM_DD).withZone(ZoneId.systemDefault());
        return Meta.builder()
                .api(API_PATH)
                .date(sdf.format(Instant.now()))
                .build();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class Meta {
        private String date;
        private String api;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class ErrorApi {
       private String code;
       private String message;
    }
}
