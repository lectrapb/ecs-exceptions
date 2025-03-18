package com.app.authorization_reactive.shared.common.domain.exception.ecs;


import com.app.authorization_reactive.shared.common.domain.exception.ConstantBusinessException;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BusinessExceptionECS extends RuntimeException {

    public static final String EMPTY_VALUE = "";
    public static final String OPTIONAL = "OPTIONAL";
    private final ConstantBusinessException constantBusinessException;
    private final Map<String, String> optionalInfo;
    private final MetaInfo metaInfo;



    public BusinessExceptionECS(String message) {
        super(message);
        this.metaInfo = MetaInfo.builder().build();
        this.optionalInfo = new HashMap<>();
        this.constantBusinessException = ConstantBusinessException.DEFAULT_EXCEPTION;

    }

    public BusinessExceptionECS(ConstantBusinessException message) {
        this(message, EMPTY_VALUE);
    }

    public BusinessExceptionECS(ConstantBusinessException message, String optionalInfo) {
        super(validateMessage(message).getLogCode());
        this.constantBusinessException = validateMessage(message);
        this.optionalInfo = fillMap(optionalInfo != null
               ? optionalInfo
               : EMPTY_VALUE);
        this.metaInfo = MetaInfo.builder().build();
    }

    public BusinessExceptionECS(ConstantBusinessException message, MetaInfo metaInfo) {
        super(validateMessage(message).getLogCode());
        this.constantBusinessException = validateMessage(message);
        this.optionalInfo = fillMap(EMPTY_VALUE);
        this.metaInfo = metaInfo;
    }

    public BusinessExceptionECS(ConstantBusinessException message, Map<String, String> optionalInfo) {
        super(validateMessage(message).getLogCode());
        this.constantBusinessException = validateMessage(message);
        this.optionalInfo = optionalInfo;
        this.metaInfo = MetaInfo.builder().build();
    }

    public BusinessExceptionECS(ConstantBusinessException message, String optionalInfo, MetaInfo metaInfo) {
        super(validateMessage(message).getLogCode());
        this.constantBusinessException = validateMessage(message);
        this.optionalInfo = fillMap(optionalInfo != null
                ? optionalInfo
                : EMPTY_VALUE);
        this.metaInfo = (metaInfo != null) ? metaInfo : MetaInfo.builder().build();
    }

    private Map<String, String> fillMap(String value) {
        Map<String, String> optionalInfoMap = new HashMap<>();
        optionalInfoMap.put(OPTIONAL, value);
        return optionalInfoMap;
    }


    public BusinessExceptionECS(ConstantBusinessException message,
                                Map<String, String> optionalInfo,
                                MetaInfo metaInfo) {
        super(validateMessage(message).getLogCode());
        this.constantBusinessException = validateMessage(message);
        this.optionalInfo = optionalInfo;
        this.metaInfo = (metaInfo != null) ? metaInfo : MetaInfo.builder().build();
    }

    private static ConstantBusinessException validateMessage(ConstantBusinessException message) {
        return (message == null)
                ? ConstantBusinessException.DEFAULT_EXCEPTION
                : message;
    }

    @Builder
    @Getter
    public static class MetaInfo implements Serializable {
        @Builder.Default
        private final String messageId = UUID.randomUUID().toString();
    }
}
