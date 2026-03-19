package com.kaidianmiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.kaidianmiao.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("`order`")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 关联任务ID
     */
    private Long taskId;
    
    /**
     * 产品类型：1-基础版9.9, 2-完整版29.9
     */
    private Integer productType;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 订单状态
     */
    private OrderStatus status;
    
    /**
     * 用户确认支付时间
     */
    private LocalDateTime userConfirmTime;
    
    /**
     * 管理员审核时间
     */
    private LocalDateTime adminVerifyTime;
    
    /**
     * 审核管理员ID
     */
    private Long adminId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}