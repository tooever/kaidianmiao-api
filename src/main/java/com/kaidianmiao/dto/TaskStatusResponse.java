package com.kaidianmiao.dto;

import com.kaidianmiao.enums.RiskLevel;
import com.kaidianmiao.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务状态响应
 */
@Data
@Builder
public class TaskStatusResponse {
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 任务状态
     */
    private TaskStatus status;
    
    /**
     * 评分 0-100
     */
    private Integer score;
    
    /**
     * 风险等级
     */
    private RiskLevel riskLevel;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedAt;
}