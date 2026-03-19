package com.kaidianmiao.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 报告详情响应
 */
@Data
@Builder
public class ReportResponse {
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 总评分 0-100
     */
    private Integer score;
    
    /**
     * 风险等级：low/medium/high
     */
    private String riskLevel;
    
    /**
     * 综合分析摘要
     */
    private String summary;
    
    /**
     * 维度分析
     */
    private List<DimensionAnalysis> dimensions;
    
    /**
     * 建议列表
     */
    private List<Suggestion> suggestions;
    
    /**
     * 风险提示
     */
    private List<RiskWarning> risks;
    
    /**
     * 维度分析
     */
    @Data
    @Builder
    public static class DimensionAnalysis {
        /**
         * 维度名称
         */
        private String name;
        
        /**
         * 维度评分
         */
        private Integer score;
        
        /**
         * 维度分析说明
         */
        private String analysis;
    }
    
    /**
     * 建议
     */
    @Data
    @Builder
    public static class Suggestion {
        /**
         * 优先级 1-5
         */
        private Integer priority;
        
        /**
         * 建议内容
         */
        private String content;
    }
    
    /**
     * 风险提示
     */
    @Data
    @Builder
    public static class RiskWarning {
        /**
         * 风险等级：high/medium/low
         */
        private String level;
        
        /**
         * 风险描述
         */
        private String content;
        
        /**
         * 应对措施
         */
        private String mitigation;
    }
}