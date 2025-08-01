package com.hainan.dashboard.controller;

import com.hainan.dashboard.common.Result;
import com.hainan.dashboard.dto.DashboardConfigDTO;
import com.hainan.dashboard.dto.DashboardParamsDTO;
import com.hainan.dashboard.dto.WidgetDataDTO;
import com.hainan.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 仪表板控制器
 * 
 * @author Hainan Dashboard Team
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Validated
@Tag(name = "仪表板管理", description = "仪表板配置和数据获取相关接口")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取仪表板配置
     */
    @GetMapping("/config")
    @Operation(summary = "获取仪表板配置", description = "根据市县和产品获取完整的仪表板配置信息")
    public Result<DashboardConfigDTO> getDashboardConfig(
            @Valid DashboardParamsDTO params) {
        log.info("获取仪表板配置: {}", params);
        
        DashboardConfigDTO config = dashboardService.getDashboardConfig(
                params.getCityCode(), 
                params.getProductCode(), 
                params.getLayoutVersion()
        );
        
        return Result.success(config);
    }

    /**
     * 获取组件数据
     */
    @GetMapping("/data/{widgetId}")
    @Operation(summary = "获取组件数据", description = "获取指定组件的数据内容")
    public Result<WidgetDataDTO> getWidgetData(
            @Parameter(description = "组件ID") @PathVariable @NotBlank String widgetId,
            @Valid DashboardParamsDTO params) {
        log.info("获取组件数据: widgetId={}, params={}", widgetId, params);
        
        WidgetDataDTO data = dashboardService.getWidgetData(
                widgetId, 
                params.getCityCode(), 
                params.getProductCode()
        );
        
        return Result.success(data);
    }

    /**
     * 刷新组件数据
     */
    @PostMapping("/data/{widgetId}/refresh")
    @Operation(summary = "刷新组件数据", description = "强制刷新指定组件的缓存数据")
    public Result<WidgetDataDTO> refreshWidgetData(
            @Parameter(description = "组件ID") @PathVariable @NotBlank String widgetId,
            @RequestBody @Valid DashboardParamsDTO params) {
        log.info("刷新组件数据: widgetId={}, params={}", widgetId, params);
        
        WidgetDataDTO data = dashboardService.refreshWidgetData(
                widgetId, 
                params.getCityCode(), 
                params.getProductCode()
        );
        
        return Result.success(data);
    }

    /**
     * 批量获取组件数据
     */
    @PostMapping("/data/batch")
    @Operation(summary = "批量获取组件数据", description = "一次性获取多个组件的数据")
    public Result<Object> getBatchWidgetData(
            @RequestBody @Valid BatchWidgetDataRequest request) {
        log.info("批量获取组件数据: {}", request);
        
        Object data = dashboardService.getBatchWidgetData(
                request.getWidgetIds(),
                request.getCityCode(),
                request.getProductCode()
        );
        
        return Result.success(data);
    }

    /**
     * 获取仪表板概览信息
     */
    @GetMapping("/overview")
    @Operation(summary = "获取仪表板概览", description = "获取仪表板的基本概览信息和统计数据")
    public Result<Object> getDashboardOverview(
            @RequestParam @NotBlank String cityCode,
            @RequestParam(required = false) String productCode) {
        log.info("获取仪表板概览: cityCode={}, productCode={}", cityCode, productCode);
        
        Object overview = dashboardService.getDashboardOverview(cityCode, productCode);
        
        return Result.success(overview);
    }

    /**
     * 检查数据更新状态
     */
    @GetMapping("/data-status")
    @Operation(summary = "检查数据更新状态", description = "检查各个数据源的最后更新时间和状态")
    public Result<Object> getDataUpdateStatus(
            @RequestParam @NotBlank String cityCode,
            @RequestParam(required = false) String productCode) {
        log.info("检查数据更新状态: cityCode={}, productCode={}", cityCode, productCode);
        
        Object status = dashboardService.getDataUpdateStatus(cityCode, productCode);
        
        return Result.success(status);
    }

    /**
     * 批量获取组件数据请求体
     */
    @lombok.Data
    public static class BatchWidgetDataRequest {
        @Parameter(description = "组件ID列表")
        private java.util.List<String> widgetIds;
        
        @Parameter(description = "市县代码")
        @NotBlank
        private String cityCode;
        
        @Parameter(description = "产品代码")
        private String productCode;
    }
}