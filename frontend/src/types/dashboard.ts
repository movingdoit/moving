/**
 * 海南一张图数据类型定义
 */

// 基础响应结构
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 页面元信息
export interface PageMeta {
  page_title: string
  city_name: string
  city_code: string
  product_name: string
  product_code: string
  update_time: string
  layout_version?: string
}

// 组件配置基础接口
export interface BaseWidgetConfig {
  widget_id: string
  title: string
  type: WidgetType
  config?: Record<string, any>
  update_time?: string
}

// 图表组件配置
export interface ChartWidgetConfig extends BaseWidgetConfig {
  type: 'Chart'
  chart_options: EChartsOption
}

// 地图组件配置
export interface MapWidgetConfig extends BaseWidgetConfig {
  type: 'Map'
  map_options: {
    map_code: string
    default_layer: string
    switch_layers?: boolean
    data_layers: Record<string, MapDataLayer>
    summary_table?: TableData
  }
}

// 表格组件配置
export interface TableWidgetConfig extends BaseWidgetConfig {
  type: 'Table'
  table_options: TableData
}

// 滚动列表组件配置
export interface ScrollListWidgetConfig extends BaseWidgetConfig {
  type: 'ScrollingTable'
  table_options: TableData
  component_config?: {
    scroll_speed?: number
    row_height?: number
  }
}

// 组件类型联合
export type WidgetConfig = ChartWidgetConfig | MapWidgetConfig | TableWidgetConfig | ScrollListWidgetConfig

// 组件类型枚举
export type WidgetType = 'Chart' | 'Map' | 'Table' | 'ScrollingTable' | 'TableAndChart'

// 地图数据层
export interface MapDataLayer {
  name: string
  unit: string
  visualMap: any
  data: Array<{
    name: string
    value: number
  }>
}

// 表格数据结构
export interface TableData {
  headers: string[]
  rows: any[][]
}

// 页面布局配置
export interface DashboardLayout {
  [position: string]: WidgetConfig
}

// 完整的仪表板配置
export interface DashboardConfig {
  page_meta: PageMeta
  layout: DashboardLayout
}

// ECharts配置类型 (简化版，实际使用echarts的完整类型)
export interface EChartsOption {
  title?: any
  tooltip?: any
  legend?: any
  grid?: any
  xAxis?: any
  yAxis?: any
  series?: any[]
  dataset?: any
  visualMap?: any
  [key: string]: any
}

// 市县选项
export interface RegionOption {
  code: string
  name: string
  type: string
}

// 产品选项
export interface ProductOption {
  code: string
  name: string
  category: string
  unit: string
}

// 仪表板请求参数
export interface DashboardParams {
  cityCode: string
  productCode?: string
  year?: number
  layoutVersion?: string
}

// 组件数据响应
export interface WidgetDataResponse {
  widget_id: string
  chart_data?: {
    categories: string[]
    series: Array<{
      name: string
      data: number[]
    }>
  }
  table_data?: TableData
  map_data?: Record<string, MapDataLayer>
  last_update: string
}

// 管理后台相关类型
export interface LayoutTemplate {
  id: number
  layout_code: string
  layout_name: string
  layout_version: string
  region_codes: string[]
  product_codes: string[]
  layout_config: Record<string, any>
  is_default: boolean
  status: number
}

export interface WidgetTemplate {
  id: number
  widget_code: string
  widget_name: string
  widget_type: WidgetType
  chart_type?: string
  data_source: string
  title_template: string
  default_options: Record<string, any>
  refresh_interval: number
  cache_duration: number
  status: number
}

export interface DataSource {
  id: number
  source_code: string
  source_name: string
  source_type: 'database' | 'api' | 'file'
  connection_config?: Record<string, any>
  query_template: string
  data_mapping: Record<string, string>
  update_frequency: string
  status: number
}

// 事件类型
export interface DashboardEvents {
  onCityChange: (cityCode: string) => void
  onProductChange: (productCode: string) => void
  onWidgetRefresh: (widgetId: string) => void
  onWidgetError: (widgetId: string, error: Error) => void
}

// 钩子函数参数
export interface UseDashboardOptions {
  autoLoad?: boolean
  enableCache?: boolean
  refreshInterval?: number
}

// 组件导出配置
export interface ExportConfig {
  format: 'png' | 'jpg' | 'pdf' | 'excel'
  quality?: number
  backgroundColor?: string
  pixelRatio?: number
}

// 布局位置枚举
export enum LayoutPosition {
  LEFT_1 = 'left_1',
  LEFT_2 = 'left_2', 
  LEFT_3 = 'left_3',
  CENTER_MAP = 'center_map',
  RIGHT_1 = 'right_1',
  RIGHT_2 = 'right_2',
  RIGHT_3 = 'right_3',
  BOTTOM_SCROLL = 'bottom_scroll'
}

// 图表类型枚举
export enum ChartType {
  BAR = 'bar',
  LINE = 'line',
  PIE = 'pie',
  MAP = 'map',
  SCATTER = 'scatter',
  RADAR = 'radar'
}

// 数据更新频率枚举
export enum UpdateFrequency {
  REALTIME = 'realtime',
  MINUTELY = 'minutely',
  HOURLY = 'hourly',
  DAILY = 'daily',
  WEEKLY = 'weekly',
  MONTHLY = 'monthly'
}