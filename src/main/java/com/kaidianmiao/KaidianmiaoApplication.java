package com.kaidianmiao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 开店喵 - 餐饮选址评估平台
 */
@SpringBootApplication
@MapperScan("com.kaidianmiao.mapper")
public class KaidianmiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaidianmiaoApplication.class, args);
    }
}