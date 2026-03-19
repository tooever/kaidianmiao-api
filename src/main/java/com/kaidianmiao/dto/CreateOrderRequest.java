package com.kaidianmiao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建订单请求
 */
@Data
public class CreateOrderRequest {
    
    /**
     * 关联任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    /**
     * 产品类型：1-基础版9.9, 2-完整版29.9
     */
    @NotNull(message = "产品类型不能为空")
    private Integer productType;
}