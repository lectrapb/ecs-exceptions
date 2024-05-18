package com.app.domain.model.valueobject;

public class ProductId {

    private String value;

    public ProductId(String value) {
        if(value == null){
            throw new RuntimeException("This value is required");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
