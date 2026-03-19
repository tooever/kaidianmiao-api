package com.kaidianmiao.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台订单列表项
 */
@Data
@Builder
public class AdminOrderListItem {
    
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String userNickname;
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 产品类型：1-基础版, 2-完整版
     */
    private Integer productType;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 状态值
     */
    private String status;
    
    /**
     * 状态标签
     */
    private String statusLabel;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 用户确认支付时间
     */
    private LocalDateTime userConfirmTime;
    
    /**
     * 管理员审核时间
     */
    private LocalDateTime adminVerifyTime;
}