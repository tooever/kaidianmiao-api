package com.kaidianmiao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核订单请求
 */
@Data
public class VerifyOrderRequest {
    
    /**
     * 是否通过
     */
    @NotNull(message = "审核结果不能为空")
    private Boolean approve;
    
    /**
     * 备注
     */
    private String remark;
}