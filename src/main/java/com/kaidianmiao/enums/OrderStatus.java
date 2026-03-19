package com.kaidianmiao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatus {
    UNPAID("unpaid", "待支付"),
    PENDING_VERIFY("pending_verify", "待审核"),
    PAID("paid", "已支付"),
    REFUNDED("refunded", "已退款");

    @EnumValue
    private final String value;
    
    @JsonValue
    private final String label;

    OrderStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }
}