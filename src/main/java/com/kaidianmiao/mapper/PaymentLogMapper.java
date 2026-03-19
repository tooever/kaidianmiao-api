package com.kaidianmiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaidianmiao.entity.PaymentLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付流水 Mapper
 */
@Mapper
public interface PaymentLogMapper extends BaseMapper<PaymentLog> {
}