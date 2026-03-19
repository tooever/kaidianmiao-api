package com.kaidianmiao.dto;

import com.kaidianmiao.enums.RiskLevel;
import com.kaidianmiao.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户报告列表项
 */
@Data
@Builder
public class UserReportListItem {
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 评分
     */
    private Integer score;
    
    /**
     * 风险等级
     */
    private RiskLevel riskLevel;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区/县
     */
    private String district;
    
    /**
     * 经营品类
     */
    private String category;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 任务状态
     */
    private TaskStatus status;
}