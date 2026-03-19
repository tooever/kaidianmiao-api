package com.kaidianmiao.service;

import com.kaidianmiao.dto.CreateAnalysisTaskRequest;
import com.kaidianmiao.dto.ReportResponse;
import com.kaidianmiao.dto.TaskStatusResponse;
import com.kaidianmiao.dto.UserReportListItem;
import com.kaidianmiao.entity.AnalysisTask;

import java.util.List;

/**
 * 分析任务服务接口
 */
public interface AnalysisTaskService {
    
    /**
     * 创建分析任务
     * @param userId 用户ID
     * @param request 创建请求
     * @return 任务ID
     */
    Long createTask(Long userId, CreateAnalysisTaskRequest request);
    
    /**
     * 获取任务状态
     * @param taskId 任务ID
     * @return 任务状态响应
     */
    TaskStatusResponse getTaskStatus(Long taskId);
    
    /**
     * 获取报告详情
     * @param taskId 任务ID
     * @return 报告响应
     */
    ReportResponse getReport(Long taskId);
    
    /**
     * 获取用户报告列表
     * @param userId 用户ID
     * @return 报告列表
     */
    List<UserReportListItem> getUserReports(Long userId);
    
    /**
     * 根据ID获取任务
     * @param taskId 任务ID
     * @return 任务实体
     */
    AnalysisTask findById(Long taskId);
    
    /**
     * 更新任务
     * @param task 任务实体
     */
    void updateTask(AnalysisTask task);
}