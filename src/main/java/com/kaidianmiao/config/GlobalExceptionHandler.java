package com.kaidianmiao.config;

import com.kaidianmiao.common.BusinessException;
import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business exception: {} at {}", e.getMessage(), request.getRequestURI());
        return Result.error(e.getCode(), e.getMessage(), e.getUserMessage(), request.getRequestId());
    }
    
    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {} at {}", message, request.getRequestURI());
        return Result.error(ErrorCode.INVALID_PARAMETER, message, "请检查输入内容", request.getRequestId());
    }
    
    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("Constraint violation: {} at {}", message, request.getRequestURI());
        return Result.error(ErrorCode.INVALID_PARAMETER, message, "请检查输入内容", request.getRequestId());
    }
    
    /**
     * 处理未授权异常
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleSecurityException(SecurityException e, HttpServletRequest request) {
        log.warn("Security exception: {} at {}", e.getMessage(), request.getRequestURI());
        return Result.error(ErrorCode.UNAUTHORIZED, e.getMessage(), "请重新登录", request.getRequestId());
    }
    
    /**
     * 处理所有未捕获异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error at {}", request.getRequestURI(), e);
        return Result.error(
            ErrorCode.INTERNAL_ERROR, 
            e.getMessage(), 
            "系统繁忙，请稍后重试", 
            request.getRequestId()
        );
    }
}