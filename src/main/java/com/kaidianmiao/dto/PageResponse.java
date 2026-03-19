package com.kaidianmiao.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 分页响应包装类
 */
@Data
@Builder
public class PageResponse<T> {
    
    /**
     * 数据列表
     */
    private List<T> items;
    
    /**
     * 总数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 创建分页响应
     */
    public static <T> PageResponse<T> of(List<T> items, Long total, Integer page, Integer pageSize) {
        return PageResponse.<T>builder()
                .items(items)
                .total(total)
                .page(page)
                .pageSize(pageSize)
                .build();
    }
}