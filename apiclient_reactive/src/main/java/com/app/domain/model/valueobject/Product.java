package com.app.domain.model.valueobject;

public class Product {

    private String value;

    public Product(String value) {

        if(value == null){
            throw new  RuntimeException("this value is mandatory ");
        }
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
