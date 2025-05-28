package co.com.bancolombia.usecase;

import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.ConstantBusinessException;
import co.com.bancolombia.model.item.Item;

import java.util.List;
import java.util.Map;

public class ExampleUseCase {

    public List<Item> getItems() {
        return List.of(
            new Item("1", "Apple"),
            new Item("2", "Banana"),
            new Item("3", "Cherry")
        );
    }

    public Map<String, String> getMap() {
        return Map.of(
            "status", "OK",
            "code", "200"
        );
    }

    public void throwException() {
        throw new BusinessException(ConstantBusinessException.BAD_REQUEST_EXCEPTION);
    }
}
