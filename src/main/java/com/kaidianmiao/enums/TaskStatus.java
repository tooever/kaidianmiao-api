package com.kaidianmiao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 分析任务状态枚举
 */
@Getter
public enum TaskStatus {
    PENDING("pending", "待分析"),
    ANALYZING("analyzing", "分析中"),
    COMPLETED("completed", "已完成"),
    FAILED("failed", "失败");

    @EnumValue
    private final String value;
    
    @JsonValue
    private final String label;

    TaskStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }
}