package com.app.createtrustrelation.domain.value;

import com.app.shared.common.domain.exceptions.BusinessException;
import com.app.shared.common.domain.exceptions.ConstantSystemException;

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
