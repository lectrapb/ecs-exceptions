package com.app.ecs.infra.config;

import com.app.ecs.infra.config.EcsPropertiesConfig;
import com.app.ecs.infra.config.sensitive.SensitiveRequestProperties;
import com.app.ecs.infra.config.sensitive.SensitiveResponseProperties;
import com.app.ecs.infra.config.service.ServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcsPropertiesConfigTest {

    private EcsPropertiesConfig config;

    @BeforeEach
    void setUp() {
        ServiceProperties serviceProps = new ServiceProperties();
        serviceProps.setName("ecs-test");

        SensitiveRequestProperties reqProps = new SensitiveRequestProperties();
        reqProps.setDelimiter(",");
        reqProps.setShow(true);
        reqProps.setAllowHeaders("Authorization,Content-Type");
        reqProps.setFields("password,secret");
        reqProps.setPatterns(".*secret.*");
        reqProps.setReplacement("***");
        reqProps.setExcludedPaths("/excluded,/health");

        SensitiveResponseProperties resProps = new SensitiveResponseProperties();
        resProps.setDelimiter(";");
        resProps.setShow(true);
        resProps.setFields("token;sessionId");
        resProps.setPatterns(".*tokenValue.*");
        resProps.setReplacement("***");

        config = new EcsPropertiesConfig(serviceProps, reqProps, resProps);
    }

    @Test
    void testShouldParseRequestPropertiesCorrectly() {
        assertEquals("ecs-test", config.getServiceName());
        assertTrue(config.getSensitiveRequestFields().contains("password"));
        assertEquals(1, config.getSensitiveRequestPatterns().size());
        assertEquals("***", config.getSensitiveRequestReplacement());
        assertTrue(config.getExcludedPaths().contains("/excluded"));
    }

    @Test
    void testShouldParseResponsePropertiesCorrectly() {
        assertTrue(config.getSensitiveResponseFields().contains("token"));
        assertEquals(1, config.getSensitiveResponsePatterns().size());
        assertEquals("***", config.getSensitiveResponseReplacement());
    }
}