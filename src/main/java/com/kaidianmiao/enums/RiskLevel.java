package com.kaidianmiao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 风险等级枚举
 */
@Getter
public enum RiskLevel {
    LOW("low", "低风险"),
    MEDIUM("medium", "中风险"),
    HIGH("high", "高风险");

    @EnumValue
    private final String value;
    
    @JsonValue
    private final String label;

    RiskLevel(String value, String label) {
        this.value = value;
        this.label = label;
    }
}