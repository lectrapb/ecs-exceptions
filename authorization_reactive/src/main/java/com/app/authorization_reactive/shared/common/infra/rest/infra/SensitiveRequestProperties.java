package com.app.authorization_reactive.shared.common.infra.rest.infra;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sensitive.request")
public class SensitiveRequestProperties {
    private String headers;
    private String fields;
    private String patterns;
    private String replacement;
}
