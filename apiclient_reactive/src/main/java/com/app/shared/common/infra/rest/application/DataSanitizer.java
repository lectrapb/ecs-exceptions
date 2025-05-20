package com.app.shared.common.infra.rest.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@UtilityClass
public class DataSanitizer {
    public static final String REPLACEMENT = "XXXXXX";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<Pattern> PII_PATTERNS = Arrays.asList(
            Pattern.compile("\\b\\d{10,11}\\b"),
            Pattern.compile("\\b\\d{16}\\b"),
            Pattern.compile("\\b\\d{9}\\b"),
            Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    );

    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "accesskey", "secretkey", "password", "pwd", "pass", "secret", "token", "key", "access_token",
            "refresh_token", "api_key", "cc"
    );

    public static String sanitize(String body) {
        if (body == null || body.isEmpty()) return body;

        try {
            Object jsonObject = objectMapper.readValue(body, Object.class);
            if (jsonObject instanceof Map) {
                sanitizeJsonMap((Map<String, Object>) jsonObject);
            }
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return sanitizeRegex(body);
        }
    }

    private static void sanitizeJsonMap(Map<String, Object> jsonMap) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (SENSITIVE_FIELDS.contains(key.toLowerCase())) {
                jsonMap.put(key, REPLACEMENT);
            } else if (value instanceof Map) {
                sanitizeJsonMap((Map<String, Object>) value);
            } else if (value instanceof List) {
                sanitizeJsonList((List<Object>) value);
            } else if (value instanceof String) {
                jsonMap.put(key, sanitizeRegex((String) value));
            }
        }
    }

    private static void sanitizeJsonList(List<Object> jsonList) {
        for (int i = 0; i < jsonList.size(); i++) {
            Object item = jsonList.get(i);
            if (item instanceof Map) {
                sanitizeJsonMap((Map<String, Object>) item);
            } else if (item instanceof String) {
                jsonList.set(i, sanitizeRegex((String) item));
            }
        }
    }

    private static String sanitizeRegex(String text) {
        String sanitized = text;
        for (Pattern pattern : PII_PATTERNS) {
            sanitized = pattern.matcher(sanitized).replaceAll(REPLACEMENT);
        }
        return sanitized;
    }
}
