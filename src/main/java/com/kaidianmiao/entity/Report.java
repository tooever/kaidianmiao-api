package com.kaidianmiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报告实体
 */
@Data
@TableName("report")
public class Report implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 关联任务ID
     */
    private Long taskId;
    
    /**
     * PDF报告URL
     */
    private String pdfUrl;
    
    /**
     * 分享图片URL
     */
    private String shareImageUrl;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}