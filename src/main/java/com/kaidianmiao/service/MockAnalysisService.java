package com.kaidianmiao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaidianmiao.dto.ReportResponse;
import com.kaidianmiao.entity.AnalysisTask;
import com.kaidianmiao.enums.RiskLevel;
import com.kaidianmiao.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Mock 分析服务
 * 用于模拟 AI 分析过程，延迟 30 秒后返回 Mock 结果
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MockAnalysisService {
    
    private final AnalysisTaskService analysisTaskService;
    private final ObjectMapper objectMapper;
    
    /**
     * 异步执行 Mock 分析
     * @param taskId 任务ID
     */
    @Async("analysisTaskExecutor")
    public void executeMockAnalysis(Long taskId) {
        log.info("开始 Mock 分析任务, taskId={}", taskId);
        
        try {
            // 延迟 30 秒模拟分析过程
            Thread.sleep(30000);
            
            // 生成 Mock 结果
            ReportResponse mockResult = generateMockResult(taskId);
            
            // 更新任务状态
            AnalysisTask task = analysisTaskService.findById(taskId);
            if (task == null) {
                log.error("任务不存在, taskId={}", taskId);
                return;
            }
            
            // 将状态更新为 analyzing
            task.setStatus(TaskStatus.ANALYZING);
            analysisTaskService.updateTask(task);
            
            // 填充结果
            task.setResultJson(objectMapper.writeValueAsString(mockResult));
            task.setScore(mockResult.getScore());
            task.setRiskLevel(RiskLevel.MEDIUM);
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            
            analysisTaskService.updateTask(task);
            
            log.info("Mock 分析任务完成, taskId={}, score={}", taskId, mockResult.getScore());
            
        } catch (InterruptedException e) {
            log.error("Mock 分析任务被中断, taskId={}", taskId, e);
            Thread.currentThread().interrupt();
            
            // 更新为失败状态
            AnalysisTask task = analysisTaskService.findById(taskId);
            if (task != null) {
                task.setStatus(TaskStatus.FAILED);
                analysisTaskService.updateTask(task);
            }
        } catch (JsonProcessingException e) {
            log.error("序列化 Mock 结果失败, taskId={}", taskId, e);
        }
    }
    
    /**
     * 生成 Mock 分析结果
     */
    private ReportResponse generateMockResult(Long taskId) {
        return ReportResponse.builder()
                .taskId(taskId)
                .score(72)
                .riskLevel("medium")
                .summary("该区域竞争适中，有一定发展空间，但需注意差异化经营")
                .dimensions(Arrays.asList(
                        ReportResponse.DimensionAnalysis.builder()
                                .name("竞争饱和度")
                                .score(65)
                                .analysis("周边500米内有8家同类餐饮店，竞争程度中等。瑞幸咖啡和星巴克占据主要市场份额，但中高端特色茶饮仍有空间。")
                                .build(),
                        ReportResponse.DimensionAnalysis.builder()
                                .name("客流潜力")
                                .score(78)
                                .analysis("该区域靠近商业中心，工作日日均客流量约2万人次，周末可达5万人次。目标客群集中，适合发展午间和下午茶时段业务。")
                                .build(),
                        ReportResponse.DimensionAnalysis.builder()
                                .name("成本结构")
                                .score(70)
                                .analysis("该区域租金处于中等水平，预计月租金150-200元/㎡。人工成本适中，建议配置3-5名员工。总体投入在预算范围内。")
                                .build(),
                        ReportResponse.DimensionAnalysis.builder()
                                .name("差异化空间")
                                .score(75)
                                .analysis("该品类在该区域尚未饱和，特色茶饮品牌较少。建议主打健康、低糖、特色口味，与现有连锁品牌形成差异化竞争。")
                                .build()
                ))
                .suggestions(Arrays.asList(
                        ReportResponse.Suggestion.builder()
                                .priority(1)
                                .content("建议选择靠近写字楼的位置，主攻工作日午餐时段")
                                .build(),
                        ReportResponse.Suggestion.builder()
                                .priority(2)
                                .content("避开与现有连锁品牌的正面竞争，主打差异化产品")
                                .build(),
                        ReportResponse.Suggestion.builder()
                                .priority(3)
                                .content("控制初期投入在预算的70%以内，预留运营周转资金")
                                .build()
                ))
                .risks(Arrays.asList(
                        ReportResponse.RiskWarning.builder()
                                .level("high")
                                .content("该区域租金有上涨趋势")
                                .mitigation("建议签订长期租约锁定价格")
                                .build(),
                        ReportResponse.RiskWarning.builder()
                                .level("medium")
                                .content("周边新商业体即将开业，可能分流客源")
                                .mitigation("关注新商业体的招商进展")
                                .build()
                ))
                .build();
    }
}