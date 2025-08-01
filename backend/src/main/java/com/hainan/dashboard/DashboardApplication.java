package com.hainan.dashboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 海南18个市县一张图后端应用启动类
 * 
 * @author Hainan Dashboard Team
 * @since 2024-01-20
 */
@SpringBootApplication
@MapperScan("com.hainan.dashboard.mapper")
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
@EnableAsync
public class DashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
        System.out.println("""
            
            ==========================================
            🎉 海南18个市县一张图后端服务启动成功！
            ==========================================
            📊 API文档地址: http://localhost:8080/doc.html
            🔍 数据库监控: http://localhost:8080/druid
            🏥 健康检查: http://localhost:8080/actuator/health
            ==========================================
            
            """);
    }
}