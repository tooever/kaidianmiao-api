package com.kaidianmiao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaidianmiao.dto.CreateAnalysisTaskRequest;
import com.kaidianmiao.dto.ReportResponse;
import com.kaidianmiao.dto.TaskStatusResponse;
import com.kaidianmiao.dto.UserReportListItem;
import com.kaidianmiao.entity.AnalysisTask;
import com.kaidianmiao.enums.RiskLevel;
import com.kaidianmiao.enums.TaskStatus;
import com.kaidianmiao.mapper.AnalysisTaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分析任务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisTaskServiceImpl implements AnalysisTaskService {
    
    private final AnalysisTaskMapper analysisTaskMapper;
    private final ObjectMapper objectMapper;
    
    @Override
    public Long createTask(Long userId, CreateAnalysisTaskRequest request) {
        AnalysisTask task = new AnalysisTask();
        task.setUserId(userId);
        task.setTaskType(1); // 选址评估
        task.setStatus(TaskStatus.PENDING);
        
        try {
            task.setInputJson(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            log.error("序列化输入数据失败", e);
            throw new RuntimeException("创建任务失败");
        }
        
        analysisTaskMapper.insert(task);
        log.info("创建分析任务成功, taskId={}, userId={}", task.getId(), userId);
        
        return task.getId();
    }
    
    @Override
    public TaskStatusResponse getTaskStatus(Long taskId) {
        AnalysisTask task = analysisTaskMapper.selectById(taskId);
        if (task == null) {
            return null;
        }
        
        return TaskStatusResponse.builder()
                .taskId(task.getId())
                .status(task.getStatus())
                .score(task.getScore())
                .riskLevel(task.getRiskLevel())
                .completedAt(task.getCompletedAt())
                .build();
    }
    
    @Override
    public ReportResponse getReport(Long taskId) {
        AnalysisTask task = analysisTaskMapper.selectById(taskId);
        if (task == null || task.getStatus() != TaskStatus.COMPLETED) {
            return null;
        }
        
        if (task.getResultJson() == null) {
            return null;
        }
        
        try {
            // 解析 resultJson
            return objectMapper.readValue(task.getResultJson(), ReportResponse.class);
        } catch (JsonProcessingException e) {
            log.error("解析报告数据失败, taskId={}", taskId, e);
            return null;
        }
    }
    
    @Override
    public List<UserReportListItem> getUserReports(Long userId) {
        List<AnalysisTask> tasks = analysisTaskMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AnalysisTask>()
                .eq(AnalysisTask::getUserId, userId)
                .orderByDesc(AnalysisTask::getCreatedAt)
        );
        
        return tasks.stream().map(task -> {
            // 从 inputJson 提取城市、区域、品类信息
            String city = "";
            String district = "";
            String category = "";
            try {
                if (task.getInputJson() != null) {
                    CreateAnalysisTaskRequest input = objectMapper.readValue(
                        task.getInputJson(), CreateAnalysisTaskRequest.class);
                    city = input.getCity();
                    district = input.getDistrict();
                    category = input.getCategory();
                }
            } catch (JsonProcessingException e) {
                log.warn("解析输入数据失败, taskId={}", task.getId());
            }
            
            return UserReportListItem.builder()
                    .taskId(task.getId())
                    .score(task.getScore())
                    .riskLevel(task.getRiskLevel())
                    .city(city)
                    .district(district)
                    .category(category)
                    .createdAt(task.getCreatedAt())
                    .status(task.getStatus())
                    .build();
        }).collect(Collectors.toList());
    }
    
    @Override
    public AnalysisTask findById(Long taskId) {
        return analysisTaskMapper.selectById(taskId);
    }
    
    @Override
    public void updateTask(AnalysisTask task) {
        analysisTaskMapper.updateById(task);
    }
}