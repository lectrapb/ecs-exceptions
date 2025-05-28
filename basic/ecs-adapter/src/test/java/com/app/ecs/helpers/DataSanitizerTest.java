package com.app.ecs.helpers;

import com.app.ecs.helpers.DataSanitizer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class DataSanitizerTest {

    @Test
    void testSanitizeWithSensitiveFieldsShouldReplaceValues() {
        String json = "{\"password\":\"1234\",\"name\":\"test\"}";
        Set<String> sensitiveFields = Set.of("password");
        List<Pattern> patterns = List.of();
        String replacement = "****";

        String sanitized = DataSanitizer.sanitize(json, sensitiveFields, patterns, replacement);

        assertTrue(sanitized.contains("\"password\":\"****\""));
        assertTrue(sanitized.contains("\"name\":\"test\""));
    }

    @Test
    void testSanitizeWithRegexPatternShouldReplaceMatches() {
        String json = "{\"card\":\"4111111111111111\",\"name\":\"test\"}";
        Set<String> sensitiveFields = Set.of();
        List<Pattern> patterns = List.of(Pattern.compile("4111\\d{12}"));
        String replacement = "xxxx";

        String sanitized = DataSanitizer.sanitize(json, sensitiveFields, patterns, replacement);

        assertTrue(sanitized.contains("xxxx"));
    }

    @Test
    void testSanitizeJsonListWithShouldReplaceMatches() {
        String jsonList = """
        {
            "data": [
              { "card": "4111111111111111", "name": "John" },
              { "card": "4111222233334444", "name": "Jane" }
            ],
            "list": [
              "type"
            ]
        }
        """;

        Set<String> sensitiveFields = Set.of();
        List<Pattern> patterns = List.of();
        String replacement = "xxxx";

        String sanitized = DataSanitizer.sanitize(jsonList, sensitiveFields, patterns, replacement);

        assertTrue(sanitized.contains("4111111111111111"));
        assertFalse(sanitized.contains("xxxx"));
        assertTrue(sanitized.contains("Jane"));
    }


    @Test
    void testSanitizeHeadersShouldFilterAllowedHeaders() {
        Set<Map.Entry<String, List<String>>> headers = Set.of(
            Map.entry("Authorization", List.of("Bearer token")),
            Map.entry("X-Custom", List.of("123"))
        );
        Set<String> allowed = Set.of("Authorization");

        Map<String, String> result = DataSanitizer.sanitizeHeaders(headers, allowed);

        assertEquals(1, result.size());
        assertEquals("Bearer token", result.get("Authorization"));
    }

    @Test
    void testSanitizeInvalidJsonShouldReturnOriginal() {
        String body = "{invalid json}";
        Set<String> sensitiveFields = Set.of("password");
        List<Pattern> patterns = List.of();
        String replacement = "****";

        String sanitized = DataSanitizer.sanitize(body, sensitiveFields, patterns, replacement);

        assertEquals(body, sanitized);
    }
}