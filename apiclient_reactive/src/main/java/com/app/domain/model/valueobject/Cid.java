package com.app.domain.model.valueobject;

import com.app.domain.shared.exception.BusinessException;
import com.app.domain.shared.exception.ConstantSystemException;

public class Cid {

    private String value;

    public Cid(String value) {

        if(value == null){
            throw new BusinessException(ConstantSystemException.MISSING_PARAMS_CODE_CID);
        }
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
