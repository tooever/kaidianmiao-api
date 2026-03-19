package com.kaidianmiao.controller;

import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.CreateAnalysisTaskRequest;
import com.kaidianmiao.dto.ReportResponse;
import com.kaidianmiao.dto.TaskStatusResponse;
import com.kaidianmiao.service.AnalysisTaskService;
import com.kaidianmiao.service.MockAnalysisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 分析任务控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/analysis-task")
@RequiredArgsConstructor
public class AnalysisTaskController {
    
    private final AnalysisTaskService analysisTaskService;
    private final MockAnalysisService mockAnalysisService;
    
    /**
     * 创建分析任务
     * POST /api/analysis-task
     */
    @PostMapping
    public Result<Map<String, Long>> createTask(
            HttpServletRequest request,
            @Valid @RequestBody CreateAnalysisTaskRequest createRequest) {
        
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        Long taskId = analysisTaskService.createTask(userId, createRequest);
        
        // 触发异步 Mock 分析
        mockAnalysisService.executeMockAnalysis(taskId);
        
        Map<String, Long> data = new HashMap<>();
        data.put("taskId", taskId);
        
        return Result.success(data);
    }
    
    /**
     * 查询任务状态
     * GET /api/analysis-task/{id}/status
     */
    @GetMapping("/{id}/status")
    public Result<TaskStatusResponse> getTaskStatus(@PathVariable("id") Long taskId) {
        TaskStatusResponse response = analysisTaskService.getTaskStatus(taskId);
        
        if (response == null) {
            return Result.error(ErrorCode.TASK_NOT_FOUND, "任务不存在", "任务不存在");
        }
        
        return Result.success(response);
    }
    
    /**
     * 获取报告详情
     * GET /api/analysis-task/{id}/report
     */
    @GetMapping("/{id}/report")
    public Result<ReportResponse> getReport(@PathVariable("id") Long taskId) {
        // 先检查任务是否存在
        TaskStatusResponse status = analysisTaskService.getTaskStatus(taskId);
        if (status == null) {
            return Result.error(ErrorCode.TASK_NOT_FOUND, "任务不存在", "任务不存在");
        }
        
        // 只有完成的任务才返回报告
        ReportResponse report = analysisTaskService.getReport(taskId);
        if (report == null) {
            return Result.error(ErrorCode.INVALID_STATE_TRANSITION, "报告未生成", "分析尚未完成，请稍后再试");
        }
        
        return Result.success(report);
    }
}