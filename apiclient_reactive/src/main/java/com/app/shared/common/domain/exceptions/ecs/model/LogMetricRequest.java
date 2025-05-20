package com.app.shared.common.domain.exceptions.ecs.model;


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

    private String method;
    private String url;
    private Map<String, String> body;
    private String consumer;
    private String messageId;
}
