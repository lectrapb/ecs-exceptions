package com.app.ecs.infra.config;

import com.app.ecs.infra.config.sensitive.SensitiveRequestProperties;
import com.app.ecs.infra.config.sensitive.SensitiveResponseProperties;
import com.app.ecs.infra.config.service.ServiceProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Component
public class EcsPropertiesConfig {
    // Service
    private final String serviceName;

    // Request
    private final Set<String> allowRequestHeaders;
    private final Set<String> sensitiveRequestFields;
    private final Set<String> excludedPaths;
    private final List<Pattern> sensitiveRequestPatterns;
    private final String sensitiveRequestReplacement;
    private final String delimiterRequest;
    private final Boolean showRequestLogs;

    // Response
    private final Set<String> sensitiveResponseFields;
    private final List<Pattern> sensitiveResponsePatterns;
    private final String sensitiveResponseReplacement;
    private final Boolean showResponseLogs;
    private final String delimiterResponse;

    public EcsPropertiesConfig(ServiceProperties serviceProperties, SensitiveRequestProperties requestProps,
                               SensitiveResponseProperties responseProps) {

        // Service
        this.serviceName = serviceProperties.getName();

        // Request
        this.delimiterRequest = requestProps.getDelimiter();
        this.showRequestLogs = requestProps.getShow();
        this.allowRequestHeaders = splitToSet(requestProps.getAllowHeaders(), delimiterRequest);
        this.sensitiveRequestFields = splitToSet(requestProps.getFields(), delimiterRequest);
        this.excludedPaths = splitToSet(requestProps.getExcludedPaths(), delimiterRequest);
        this.sensitiveRequestPatterns = splitToList(requestProps.getPatterns(), delimiterRequest);
        this.sensitiveRequestReplacement = requestProps.getReplacement();

        // Response
        this.showResponseLogs = responseProps.getShow();
        this.delimiterResponse = responseProps.getDelimiter();
        this.sensitiveResponseFields = splitToSet(responseProps.getFields(), delimiterResponse);
        this.sensitiveResponsePatterns = splitToList(responseProps.getPatterns(), delimiterResponse);
        this.sensitiveResponseReplacement = responseProps.getReplacement();
    }

    private Set<String> splitToSet(String input, String delimiter) {
        return Stream.of(input.split(delimiter))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    private List<Pattern> splitToList(String input, String delimiter) {
        return Stream.of(input.split(delimiter))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Pattern::compile)
            .toList();
    }
}
