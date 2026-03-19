package com.kaidianmiao.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理后台数据统计响应
 */
@Data
@Builder
public class DashboardResponse {
    
    /**
     * 总订单数
     */
    private Long totalOrders;
    
    /**
     * 待审核订单数
     */
    private Long pendingOrders;
    
    /**
     * 已支付订单数
     */
    private Long paidOrders;
    
    /**
     * 总收入
     */
    private BigDecimal totalRevenue;
    
    /**
     * 总用户数
     */
    private Long totalUsers;
    
    /**
     * 总报告数（已完成的分析任务）
     */
    private Long totalReports;
    
    /**
     * 今日订单数
     */
    private Long todayOrders;
    
    /**
     * 今日收入
     */
    private BigDecimal todayRevenue;
}