package co.com.bancolombia.usecase;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.ConstantBusinessException;
import co.com.bancolombia.model.item.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ExampleUseCase {

    public Flux<Item> getItems() {
        return Flux.just(
            new Item("1", "Apple"),
            new Item("2", "Banana"),
            new Item("3", "Cherry")
        );
    }

    public Mono<Map<String, String>> getMap() {
        return Mono.just(Map.of(
            "status", "OK",
            "code", "200"
        ));
    }

    public Mono<Void> throwException() {
        return Mono.error(new BusinessException(ConstantBusinessException.BAD_REQUEST_EXCEPTION));
    }
}

