import request from '@/utils/request'
import type { 
  ApiResponse, 
  DashboardConfig, 
  DashboardParams,
  WidgetDataResponse,
  RegionOption,
  ProductOption,
  LayoutTemplate,
  WidgetTemplate,
  DataSource
} from '@/types/dashboard'

/**
 * 仪表板相关API
 */

// 获取仪表板配置
export function getDashboardConfig(params: DashboardParams): Promise<ApiResponse<DashboardConfig>> {
  return request.get('/api/v1/dashboard/config', { params })
}

// 获取组件数据
export function getWidgetData(widgetId: string, params?: DashboardParams): Promise<ApiResponse<WidgetDataResponse>> {
  return request.get(`/api/v1/dashboard/data/${widgetId}`, { params })
}

// 刷新组件数据
export function refreshWidgetData(widgetId: string, params?: DashboardParams): Promise<ApiResponse<WidgetDataResponse>> {
  return request.post(`/api/v1/dashboard/data/${widgetId}/refresh`, params)
}

// 获取市县列表
export function getRegions(): Promise<ApiResponse<RegionOption[]>> {
  return request.get('/api/v1/system/regions')
}

// 获取产品列表
export function getProducts(): Promise<ApiResponse<ProductOption[]>> {
  return request.get('/api/v1/system/products')
}

// 获取指定市县的产品列表
export function getProductsByRegion(regionCode: string): Promise<ApiResponse<ProductOption[]>> {
  return request.get(`/api/v1/system/regions/${regionCode}/products`)
}

/**
 * 管理后台API
 */

// 布局管理
export const layoutApi = {
  // 获取布局列表
  getLayouts(): Promise<ApiResponse<LayoutTemplate[]>> {
    return request.get('/api/v1/admin/layouts')
  },

  // 获取布局详情
  getLayout(layoutId: number): Promise<ApiResponse<LayoutTemplate>> {
    return request.get(`/api/v1/admin/layouts/${layoutId}`)
  },

  // 创建布局
  createLayout(data: Partial<LayoutTemplate>): Promise<ApiResponse<void>> {
    return request.post('/api/v1/admin/layouts', data)
  },

  // 更新布局
  updateLayout(layoutId: number, data: Partial<LayoutTemplate>): Promise<ApiResponse<void>> {
    return request.put(`/api/v1/admin/layouts/${layoutId}`, data)
  },

  // 删除布局
  deleteLayout(layoutId: number): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/admin/layouts/${layoutId}`)
  },

  // 设置默认布局
  setDefaultLayout(layoutId: number): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/admin/layouts/${layoutId}/set-default`)
  }
}

// 组件管理
export const widgetApi = {
  // 获取组件列表
  getWidgets(): Promise<ApiResponse<WidgetTemplate[]>> {
    return request.get('/api/v1/admin/widgets')
  },

  // 获取组件详情
  getWidget(widgetId: number): Promise<ApiResponse<WidgetTemplate>> {
    return request.get(`/api/v1/admin/widgets/${widgetId}`)
  },

  // 创建组件
  createWidget(data: Partial<WidgetTemplate>): Promise<ApiResponse<void>> {
    return request.post('/api/v1/admin/widgets', data)
  },

  // 更新组件
  updateWidget(widgetId: number, data: Partial<WidgetTemplate>): Promise<ApiResponse<void>> {
    return request.put(`/api/v1/admin/widgets/${widgetId}`, data)
  },

  // 删除组件
  deleteWidget(widgetId: number): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/admin/widgets/${widgetId}`)
  },

  // 预览组件
  previewWidget(widgetCode: string, params: DashboardParams): Promise<ApiResponse<any>> {
    return request.post(`/api/v1/admin/widgets/${widgetCode}/preview`, params)
  }
}

// 数据源管理
export const dataSourceApi = {
  // 获取数据源列表
  getDataSources(): Promise<ApiResponse<DataSource[]>> {
    return request.get('/api/v1/admin/datasources')
  },

  // 获取数据源详情
  getDataSource(sourceId: number): Promise<ApiResponse<DataSource>> {
    return request.get(`/api/v1/admin/datasources/${sourceId}`)
  },

  // 创建数据源
  createDataSource(data: Partial<DataSource>): Promise<ApiResponse<void>> {
    return request.post('/api/v1/admin/datasources', data)
  },

  // 更新数据源
  updateDataSource(sourceId: number, data: Partial<DataSource>): Promise<ApiResponse<void>> {
    return request.put(`/api/v1/admin/datasources/${sourceId}`, data)
  },

  // 删除数据源
  deleteDataSource(sourceId: number): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/admin/datasources/${sourceId}`)
  },

  // 测试数据源连接
  testDataSource(data: Partial<DataSource>): Promise<ApiResponse<any>> {
    return request.post('/api/v1/admin/datasources/test', data)
  },

  // 预览数据源数据
  previewDataSource(sourceCode: string, params: DashboardParams): Promise<ApiResponse<any>> {
    return request.post(`/api/v1/admin/datasources/${sourceCode}/preview`, params)
  }
}

/**
 * 导出相关API
 */
export const exportApi = {
  // 导出图表
  exportChart(widgetId: string, format: 'png' | 'jpg' | 'pdf', params?: any): Promise<Blob> {
    return request.post(`/api/v1/export/chart/${widgetId}`, params, {
      params: { format },
      responseType: 'blob'
    })
  },

  // 导出表格
  exportTable(widgetId: string, format: 'excel' | 'csv', params?: any): Promise<Blob> {
    return request.post(`/api/v1/export/table/${widgetId}`, params, {
      params: { format },
      responseType: 'blob'
    })
  },

  // 导出整个仪表板
  exportDashboard(params: DashboardParams, format: 'pdf' | 'png'): Promise<Blob> {
    return request.post('/api/v1/export/dashboard', params, {
      params: { format },
      responseType: 'blob'
    })
  }
}

/**
 * 系统配置API
 */
export const systemApi = {
  // 获取系统配置
  getSystemConfig(): Promise<ApiResponse<any>> {
    return request.get('/api/v1/system/config')
  },

  // 更新系统配置
  updateSystemConfig(data: any): Promise<ApiResponse<void>> {
    return request.put('/api/v1/system/config', data)
  },

  // 获取系统状态
  getSystemStatus(): Promise<ApiResponse<any>> {
    return request.get('/api/v1/system/status')
  },

  // 清理缓存
  clearCache(): Promise<ApiResponse<void>> {
    return request.post('/api/v1/system/cache/clear')
  }
}

/**
 * 统计分析API
 */
export const analyticsApi = {
  // 获取访问统计
  getVisitStats(params?: any): Promise<ApiResponse<any>> {
    return request.get('/api/v1/analytics/visits', { params })
  },

  // 获取组件使用统计
  getWidgetStats(params?: any): Promise<ApiResponse<any>> {
    return request.get('/api/v1/analytics/widgets', { params })
  },

  // 获取数据更新统计
  getDataUpdateStats(params?: any): Promise<ApiResponse<any>> {
    return request.get('/api/v1/analytics/data-updates', { params })
  }
}