package com.app.ecs.domain.model;

import com.app.ecs.domain.model.LogRecord;
import com.app.ecs.domain.model.LoggerEcs;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LoggerEcsTest {

    public static final String SERVICE = "Service";
    public static final String SYSTEM_ERROR = "SystemError";
    public static final String CONNECTION_FAILED = "Connection failed";
    public static final String METHOD = "POST";
    public static final String URI = "/api/v1/logs";

    LogRecord<String, String> logRecord;

    @BeforeEach
    void setup() {
        logRecord = new LogRecord<>();
        logRecord.setService(SERVICE);
        logRecord.setLevel(LogRecord.Level.ERROR);
        Map<String, String> map  = new HashMap<>();
        map.put("key", "value");
        logRecord.setError(LogRecord.ErrorLog.<String, String>builder()
                        .type(SYSTEM_ERROR)
                        .message(CONNECTION_FAILED)
                        .description("Database connection timeout")
                        .optionalInfo(map)
                .build()
        );
        logRecord.setMessageId("message-id");
        logRecord.setConsumer("consumer");
        Map<String, String> headers  = new HashMap<>();
        headers.put("key", "value");
        Map<String, String> request  = new HashMap<>();
        request.put("key", "value");
        Map<String, String> response  = new HashMap<>();
        response.put("key", "value");
        logRecord.setAdditionalInfo(LogRecord.AdditionalInfo.<String, String>builder()
            .method(METHOD)
            .uri(URI)
            .headers(headers)
            .requestBody(request)
            .responseBody(response)
            .responseCode("200")
            .responseResult("OK")
            .build()
        );
    }

    @Test
    void testLogPrivateConstructor() throws Exception {
        Constructor<LoggerEcs> constructor = LoggerEcs.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var state = constructor.newInstance();

        assertNotNull(state);
    }

    @Test
    void testGetRecordValue() {
        assertNotNull(logRecord.getMessageId());
        assertNotNull(logRecord.getDate());
        assertEquals(SERVICE, logRecord.getService());
        assertEquals(LogRecord.Level.ERROR.value(), logRecord.getLevel().value());

        String jsonLog = logRecord.toJson();
        assertTrue(jsonLog.contains(SYSTEM_ERROR));
        assertTrue(jsonLog.contains(CONNECTION_FAILED));
        assertTrue(jsonLog.contains(URI));
        assertTrue(jsonLog.contains(METHOD));
    }

    @Test
    void testPrintAllLogLevels() {
        Logger mockLogger = mock(Logger.class);

        try (MockedStatic<LoggerEcs> mockedLoggerEcs = Mockito.mockStatic(
                LoggerEcs.class, Mockito.CALLS_REAL_METHODS)) {
            mockedLoggerEcs.when(() -> {
                LoggerEcs.print(logRecord);
            }).thenAnswer(invocation -> {
                switch (logRecord.getLevel()) {
                    case DEBUG -> verify(mockLogger).debug(logRecord.toJson());
                    case INFO -> verify(mockLogger).info(logRecord.toJson());
                    case WARNING -> verify(mockLogger).warn(logRecord.toJson());
                    case ERROR -> verify(mockLogger).error(logRecord.toJson());
                    case FATAL -> verify(mockLogger).fatal(logRecord.toJson());
                }
                return null;
            });

            for (LogRecord.Level level : LogRecord.Level.values()) {
                logRecord = LogRecord.<String, String>builder()
                        .level(level)
                        .build();
                LoggerEcs.print(logRecord);
            }
        }
    }

}