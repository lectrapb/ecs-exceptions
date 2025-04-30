package com.app.authorization_reactive.shared.common.infra.rest.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.regex.Matcher;

@UtilityClass
public class DataSanitizer {
	private static final Integer MASK_VALUE = 4;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String sanitize(String body, Set<String> sensitiveFields,
								  List<Pattern> piiPatterns, String replacement) {
		if (body == null || body.isEmpty()) return body;

		try {
			Object parsed = objectMapper.readValue(body, Object.class);
			if (parsed instanceof Map) {
				sanitizeJsonMap((Map<String, Object>) parsed, toLowerSet(sensitiveFields), piiPatterns, replacement);
			}
			return objectMapper.writeValueAsString(parsed);
		} catch (Exception e) {
			return sanitizeRegex(body, piiPatterns, replacement);
		}
	}

	public static Map<String, String> sanitizeHeaders(
		HttpHeaders requestHeaders, Set<String> sensitiveHeaders, String headerDelimiter, String replacement) {
		Map<String, String> headers = new HashMap<>();
		requestHeaders.forEach((key, value) -> {
			if (!sensitiveHeaders.contains(key)) {
				headers.put(key, String.join(headerDelimiter, value));
			} else {
				headers.put(key, replacement);
			}
		});
		return headers;
	}

	private static void sanitizeJsonMap(Map<String, Object> map, Set<String> sensitiveFields,
										List<Pattern> piiPatterns, String replacement) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (sensitiveFields.contains(key.toLowerCase())) {
				map.put(key, replacement);
			} else if (value instanceof Map) {
				sanitizeJsonMap((Map<String, Object>) value, sensitiveFields, piiPatterns, replacement);
			} else if (value instanceof List) {
				sanitizeJsonList((List<Object>) value, sensitiveFields, piiPatterns, replacement);
			} else if (value instanceof String) {
				map.put(key, sanitizeRegex((String) value, piiPatterns, replacement));
			}
		}
	}

	private static void sanitizeJsonList(List<Object> list, Set<String> sensitiveFields,
										 List<Pattern> piiPatterns, String replacement) {
		for (int i = 0; i < list.size(); i++) {
			Object item = list.get(i);
			if (item instanceof Map) {
				sanitizeJsonMap((Map<String, Object>) item, sensitiveFields, piiPatterns, replacement);
			} else if (item instanceof String) {
				list.set(i, sanitizeRegex((String) item, piiPatterns, replacement));
			}
		}
	}

	private static String sanitizeRegex(String text, List<Pattern> piiPatterns, String replacement) {
		String result = text;
		for (Pattern pattern : piiPatterns) {
			Matcher matcher = pattern.matcher(result);
			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String match = matcher.group();
				String masked = maskValue(match, replacement);
				matcher.appendReplacement(sb, masked);
			}
			matcher.appendTail(sb);
			result = sb.toString();
		}
		return result;
	}

	private static String maskValue(String match, String replacement) {
		if (match.length() > MASK_VALUE && match.chars().allMatch(Character::isDigit)) {
			return "*".repeat(match.length() - MASK_VALUE) + match.substring(
				match.length() - MASK_VALUE);
		}
		return replacement;
	}

	private static Set<String> toLowerSet(Set<String> input) {
		Set<String> result = new HashSet<>();
		for (String item : input) {
			result.add(item.toLowerCase());
		}
		return result;
	}
}