package com.kaidianmiao.common;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final Integer code;
    private final String userMessage;
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.userMessage = message;
    }
    
    public BusinessException(Integer code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.userMessage = userMessage;
    }
    
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }
    
    public static BusinessException of(Integer code, String message, String userMessage) {
        return new BusinessException(code, message, userMessage);
    }
    
    public static BusinessException notFound(String message) {
        return new BusinessException(ErrorCode.NOT_FOUND, message);
    }
    
    public static BusinessException unauthorized(String message) {
        return new BusinessException(ErrorCode.UNAUTHORIZED, message);
    }
    
    public static BusinessException forbidden(String message) {
        return new BusinessException(ErrorCode.FORBIDDEN, message);
    }
    
    public static BusinessException invalidParameter(String message) {
        return new BusinessException(ErrorCode.INVALID_PARAMETER, message);
    }
}