package com.app.createtrustrelation.domain.value;

import com.app.shared.common.domain.exceptions.BusinessException;
import com.app.shared.common.domain.exceptions.ConstantBusinessException;

public class Product {

    private String value;

    public Product(String value) {

        if(value == null){
            throw new BusinessException(ConstantBusinessException.MISSING_PARAMS_CODE_PRODUCT);
        }
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
