package com.jovan.usercenter.exception;

import com.jovan.usercenter.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author jovan
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        code = errorCode.getCode();
        description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

}
