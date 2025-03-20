package com.app.authorization_reactive.shared.helpers.ecs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogMetrics<T, R>  extends LogRecord<T, R>{

    private String consumer;
    private AdditionalInfo<T, R> additionalInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdditionalInfo<T, R>{
        private String method;
        private String uri;
        private Map<T, R> requestBody;
        private Map<T, R> responseBody;
        private String responseResult;
        private String responseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LogMetrics<?, ?> that = (LogMetrics<?, ?>) o;
        return Objects.equals(consumer, that.consumer) && Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), consumer, additionalInfo);
    }
}