package com.kaidianmiao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单列表项响应
 */
@Data
@Builder
public class OrderListItemResponse {
    
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 产品类型：1-基础版, 2-完整版
     */
    private Integer productType;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusLabel;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}