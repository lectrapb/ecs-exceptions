package com.app.authorization_reactive.shared.helpers.ecs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogMetricRequest {
    private String messageId;
    private String consumer;
    private String responseResult;
    private Integer responseCode;
    private Map<String, String> requestBody;
    private Map<String, String> responseBody;
    private String method;
    private String url;
}
