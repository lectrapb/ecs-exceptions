package co.com.bancolombia.api;
import co.com.bancolombia.api.response.ApiResponse;
import co.com.bancolombia.api.response.DataResponse;
import co.com.bancolombia.api.response.HandlerResponse;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.ConstantBusinessException;
import co.com.bancolombia.model.utils.Constants;
import co.com.bancolombia.usecase.ExampleUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * API Rest controller.
 * 
 * Example of how to declare and use a use case:
 * <pre>
 * private final MyUseCase useCase;
 * 
 * public String commandName() {
 *     return useCase.execute();
 * }
 * </pre>
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private final ExampleUseCase useCase;

    @GetMapping(path = "/usecase/path")
    public ResponseEntity<ApiResponse> commandPath(@RequestHeader Map<String, String> headers) {
        var dataResponse = DataResponse.builder()
            .status("200")
            .messageId(headers.get(Constants.MESSAGE_ID_HEADER))
            .build();
        var result = useCase.getItems();
        return new HandlerResponse().createSuccessResponse(result, dataResponse);
    }

    @PostMapping(path = "/usecase/otherpath")
    public ResponseEntity<ApiResponse> commandOtherPath(@RequestHeader Map<String, String> headers) {
        var dataResponse = DataResponse.builder()
            .status("200")
            .messageId(headers.get(Constants.MESSAGE_ID_HEADER))
            .build();
        useCase.throwException();
        return new HandlerResponse().createSuccessResponse(null, dataResponse);
    }

    @GetMapping(path = "/otherusecase/path")
    public ResponseEntity<ApiResponse> commandOtherUseCase(@RequestHeader Map<String, String> headers) {
        var dataResponse = DataResponse.builder()
            .status("200")
            .messageId(headers.get(Constants.MESSAGE_ID_HEADER))
            .build();
        var result = useCase.getMap();
        return new HandlerResponse().createSuccessResponse(result, dataResponse);
    }
}
