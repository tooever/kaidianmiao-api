package com.kaidianmiao.common;

/**
 * 错误码定义
 */
public final class ErrorCode {
    
    private ErrorCode() {}
    
    // 成功
    public static final int SUCCESS = 0;
    
    // 系统错误 (10001-19999)
    public static final int INTERNAL_ERROR = 10001;
    public static final int DATABASE_ERROR = 10002;
    public static final int SERVICE_UNAVAILABLE = 10003;
    
    // 业务错误 (20001-29999)
    public static final int DUPLICATE_ENTRY = 20001;
    public static final int DUPLICATE_PHONE = 20002;
    public static final int INVALID_STATE_TRANSITION = 20003;
    public static final int ORDER_CANNOT_CANCEL = 20004;
    public static final int RESOURCE_REFERENCED = 20005;
    public static final int TASK_NOT_FOUND = 20010;
    public static final int ORDER_NOT_FOUND = 20011;
    public static final int USER_NOT_FOUND = 20012;
    
    // 参数错误 (30001-39999)
    public static final int INVALID_PARAMETER = 30001;
    public static final int MISSING_REQUIRED_FIELD = 30002;
    public static final int INVALID_FORMAT = 30003;
    
    // 认证错误 (40001-40099)
    public static final int UNAUTHORIZED = 40001;
    public static final int TOKEN_EXPIRED = 40002;
    public static final int TOKEN_INVALID = 40003;
    public static final int LOGIN_FAILED = 40004;
    
    // 权限错误 (40101-40199)
    public static final int FORBIDDEN = 40101;
    public static final int ADMIN_REQUIRED = 40102;
    
    // 资源不存在 (40401-40499)
    public static final int NOT_FOUND = 40401;
    public static final int RESOURCE_NOT_FOUND = 40402;
}