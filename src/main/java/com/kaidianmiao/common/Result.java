package com.kaidianmiao.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应格式
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码：0 表示成功，非 0 表示错误
     */
    private Integer code;
    
    /**
     * 响应消息（技术描述）
     */
    private String message;
    
    /**
     * 用户友好提示
     */
    private String userMessage;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 请求追踪ID
     */
    private String traceId;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带消息）
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（带用户提示）
     */
    public static <T> Result<T> error(Integer code, String message, String userMessage) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setUserMessage(userMessage);
        return result;
    }

    /**
     * 失败响应（带追踪ID）
     */
    public static <T> Result<T> error(Integer code, String message, String userMessage, String traceId) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setUserMessage(userMessage);
        result.setTraceId(traceId);
        return result;
    }
}