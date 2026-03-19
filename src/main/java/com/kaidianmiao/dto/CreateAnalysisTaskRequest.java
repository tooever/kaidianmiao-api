package com.kaidianmiao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 创建分析任务请求
 */
@Data
public class CreateAnalysisTaskRequest {
    
    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;
    
    /**
     * 区/县
     */
    @NotBlank(message = "区/县不能为空")
    private String district;
    
    /**
     * 商圈/区域
     */
    @NotBlank(message = "商圈/区域不能为空")
    private String area;
    
    /**
     * 预算范围
     */
    private String budget;
    
    /**
     * 经营品类
     */
    @NotBlank(message = "经营品类不能为空")
    private String category;
    
    /**
     * 目标客群
     */
    private List<String> targetCustomers;
    
    /**
     * 客单价
     */
    private String avgPrice;
    
    /**
     * 营业时段
     */
    private List<String> businessHours;
    
    /**
     * 是否有竞争对手信息
     */
    private Boolean hasCompetitorInfo;
    
    /**
     * 竞争对手信息
     */
    private String competitors;
    
    /**
     * 自身优势
     */
    private List<String> advantages;
    
    /**
     * 补充信息
     */
    private String additionalInfo;
}