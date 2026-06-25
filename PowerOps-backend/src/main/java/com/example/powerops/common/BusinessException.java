package com.example.powerops.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = Code.ERROR;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
