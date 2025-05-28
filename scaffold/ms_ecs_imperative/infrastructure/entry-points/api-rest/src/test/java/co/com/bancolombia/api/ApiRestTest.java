package co.com.bancolombia.api;

import co.com.bancolombia.usecase.ExampleUseCase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiRestTest {

    ApiRest apiRest = new ApiRest(new ExampleUseCase());

    @Test
    void apiRestTest1() {
        var response = apiRest.commandPath(Map.of());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void apiRestTest2() {
        var response = apiRest.commandOtherPath(Map.of());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void apiRestTest3() {
        var response = apiRest.commandOtherUseCase(Map.of());
        assertEquals(200, response.getStatusCode().value());
    }
}
