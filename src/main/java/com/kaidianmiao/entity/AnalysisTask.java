package com.kaidianmiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.kaidianmiao.enums.RiskLevel;
import com.kaidianmiao.enums.TaskStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分析任务实体
 */
@Data
@TableName("analysis_task")
public class AnalysisTask implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 任务类型：1-选址评估
     */
    private Integer taskType;
    
    /**
     * 任务状态
     */
    private TaskStatus status;
    
    /**
     * 用户输入JSON
     */
    private String inputJson;
    
    /**
     * AI分析结果JSON
     */
    private String resultJson;
    
    /**
     * 评分 0-100
     */
    private Integer score;
    
    /**
     * 风险等级
     */
    private RiskLevel riskLevel;
    
    /**
     * 关联订单ID
     */
    private Long orderId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedAt;
}