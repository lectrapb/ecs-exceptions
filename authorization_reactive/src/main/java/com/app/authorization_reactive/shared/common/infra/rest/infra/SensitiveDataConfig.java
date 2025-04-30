package com.app.authorization_reactive.shared.common.infra.rest.infra;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Component
public class SensitiveDataConfig {
    private static final String SANITIZE_DELIMITER = "\\|";
    public static final String HEADER_DELIMITER = ",";

    // Request
    private final Set<String> sensitiveRequestHeaders;
    private final Set<String> sensitiveRequestFields;
    private final List<Pattern> sensitiveRequestPatterns;
    private final String sensitiveRequestReplacement;

    // Response
    private final Set<String> sensitiveResponseFields;
    private final List<Pattern> sensitiveResponsePatterns;
    private final String sensitiveResponseReplacement;

    public SensitiveDataConfig(SensitiveRequestProperties requestProps, SensitiveResponseProperties responseProps) {
        // Request
        this.sensitiveRequestHeaders = splitToSet(requestProps.getHeaders());
        this.sensitiveRequestFields = splitToSet(requestProps.getFields()).stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        this.sensitiveRequestPatterns = splitToList(requestProps.getPatterns()).stream()
            .map(Pattern::compile)
            .toList();
        this.sensitiveRequestReplacement = requestProps.getReplacement();

        // Response
        this.sensitiveResponseFields = splitToSet(responseProps.getFields()).stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        this.sensitiveResponsePatterns = splitToList(responseProps.getPatterns()).stream()
            .map(Pattern::compile)
            .toList();
        this.sensitiveResponseReplacement = responseProps.getReplacement();
    }

    private Set<String> splitToSet(String input) {
        return Stream.of(input.split(SANITIZE_DELIMITER))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }

    private List<String> splitToList(String input) {
        return Stream.of(input.split(SANITIZE_DELIMITER))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
    }
}
