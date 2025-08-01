# 海南18个市县一张图 - 数据库配置与前端交互流程图

## 🗃️ 数据库配置存储示例

### 1. 基础数据配置
```
sys_regions (区域表)
┌─────────────┬──────────┬──────────┬────────────┐
│ region_code │region_name│region_type│center_point│
├─────────────┼──────────┼──────────┼────────────┤
│ haikou      │ 海口市    │ city     │{lng:110.33}│
│ sanya       │ 三亚市    │ city     │{lng:109.51}│
└─────────────┴──────────┴──────────┴────────────┘

sys_products (产品表)
┌──────────────┬────────────┬──────────┬────────┐
│ product_code │product_name│unit      │icon_url│
├──────────────┼────────────┼──────────┼────────┤
│ litchi       │ 荔枝       │ 万亩     │/icons/ │
│ coconut      │ 椰子       │ 万亩     │/icons/ │
└──────────────┴────────────┴──────────┴────────┘
```

### 2. 布局位置配置
```
dashboard_layout_positions (位置定义表)
┌─────────────┬────────────┬──────────────┬──────────┐
│position_code│position_name│position_type │grid_area │
├─────────────┼────────────┼──────────────┼──────────┤
│ left_1      │ 左一区域   │ widget       │ left-1   │
│ left_2      │ 左二区域   │ widget       │ left-2   │
│ left_3      │ 左三区域   │ widget       │ left-3   │
│ center_map  │ 中间地图   │ map          │center-map│
│center_bottom│ 中下区域   │ scroll       │center-btm│
│ right_1     │ 右一区域   │ widget       │ right-1  │
│ right_2     │ 右二区域   │ widget       │ right-2  │
│ right_3     │ 右三区域   │ widget       │ right-3  │
└─────────────┴────────────┴──────────────┴──────────┘
```

### 3. 组件配置数据（海口荔枝示例）
```
dashboard_position_widgets (组件配置表)
┌───────────┬─────────────┬─────────────┬────────────────┬─────────────┬────────────┬──────────────────┐
│region_code│product_code │position_code│widget_title    │widget_type  │chart_type  │data_source_code  │
├───────────┼─────────────┼─────────────┼────────────────┼─────────────┼────────────┼──────────────────┤
│ haikou    │ litchi      │ left_1      │近年海口荔枝面积│ chart       │ bar        │ area_trend_data  │
│ haikou    │ litchi      │ left_2      │近年海口荔枝产量│ chart       │ line       │ production_trend │
│ haikou    │ litchi      │ left_3      │荔枝品种分布    │ chart       │ pie        │ variety_data     │
│ haikou    │ litchi      │ right_1     │荔枝销售额情况  │ chart       │ bar        │ sales_trend      │
│ haikou    │ litchi      │ right_2     │荔枝价格走势    │ chart       │ line       │ price_trend      │
│ haikou    │ litchi      │ right_3     │合作社统计      │ chart       │ bar        │ cooperative_stats│
│ haikou    │ litchi      │center_bottom│价格动态监测    │ list        │ scroll     │ price_monitor    │
└───────────┴─────────────┴─────────────┴────────────────┴─────────────┴────────────┴──────────────────┘
```

### 4. 地图图层配置
```
map_layer_config (地图图层表)
┌───────────┬─────────────┬──────────┬────────────┬────────┬────────────┬──────────────────┐
│region_code│product_code │layer_code│layer_name  │unit    │is_default  │data_source_code  │
├───────────┼─────────────┼──────────┼────────────┼────────┼────────────┼──────────────────┤
│ haikou    │ litchi      │ area     │ 面积       │ 万亩   │ 1          │ map_area_data    │
│ haikou    │ litchi      │production│ 产量       │ 万吨   │ 0          │ map_production   │
│ haikou    │ litchi      │cooperative│合作社      │ 个     │ 0          │ map_cooperative  │
└───────────┴─────────────┴──────────┴────────────┴────────┴────────────┴──────────────────┘
```

---

## 🔄 前端调用与数据库交互流程图

```
用户访问页面：/dashboard?region=haikou&product=litchi
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         前端初始化流程                                               │
└─────────────────────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 1. 获取页面布局配置                                        │
    │    GET /api/v1/dashboard/layout?regionCode=haikou&...      │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 后端查询数据库                                              │
    │                                                             │
    │ SELECT                                                      │
    │   pos.position_code,                                        │
    │   pos.position_name,                                        │
    │   pos.position_type,                                        │
    │   widget.widget_title,                                      │
    │   widget.widget_type,                                       │
    │   widget.chart_type,                                        │
    │   widget.data_source_code                                   │
    │ FROM dashboard_layout_positions pos                         │
    │ LEFT JOIN dashboard_position_widgets widget                 │
    │   ON pos.position_code = widget.position_code              │
    │   AND widget.region_code = 'haikou'                        │
    │   AND widget.product_code = 'litchi'                       │
    │ ORDER BY pos.sort_order                                     │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 返回布局配置JSON                                           │
    │                                                             │
    │ {                                                           │
    │   "layout_positions": {                                     │
    │     "left_1": {                                             │
    │       "position_type": "widget",                            │
    │       "widget_title": "近年海口荔枝面积情况",               │
    │       "widget_type": "chart",                               │
    │       "chart_type": "bar",                                  │
    │       "data_api": "/api/v1/dashboard/widget/left_1?..."     │
    │     },                                                      │
    │     "center_map": {                                         │
    │       "position_type": "map",                               │
    │       "layers_api": "/api/v1/dashboard/map/layers?..."      │
    │     }                                                       │
    │   }                                                         │
    │ }                                                           │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 2. 前端根据配置创建页面结构                                 │
    │                                                             │
    │ ┌─────────┬─────────┬─────────┐                             │
    │ │ left_1  │ left_2  │ left_3  │                             │
    │ │ (widget)│ (widget)│ (widget)│                             │
    │ ├─────────┼─────────┼─────────┤                             │
    │ │         │center_map(map)    │                             │
    │ │ right_1 ├─────────┬─────────┤                             │
    │ │ (widget)│right_2  │ right_3 │                             │
    │ │         │(widget) │(widget) │                             │
    │ ├─────────┼─────────┼─────────┤                             │
    │ │    center_bottom(scroll)    │                             │
    │ └─────────┴─────────┴─────────┘                             │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         批量获取组件数据                                           │
└─────────────────────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 3. 批量获取所有组件数据                                     │
    │    POST /api/v1/dashboard/batch-widgets                     │
    │    {                                                        │
    │      "region_code": "haikou",                               │
    │      "product_code": "litchi",                              │
    │      "positions": ["left_1","left_2","left_3","right_1",   │
    │                    "right_2","right_3","center_bottom"]     │
    │    }                                                        │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 后端并行处理每个位置的数据                                  │
    │                                                             │
    │ left_1位置处理：                                           │
    │ 1. 查询配置：dashboard_position_widgets                     │
    │    -> data_source_code = "area_trend_data"                  │
    │                                                             │
    │ 2. 查询数据源：data_sources                                 │
    │    -> query_template = "SELECT ... FROM chart_data WHERE"  │
    │                                                             │
    │ 3. 执行SQL：                                                │
    │    SELECT dimension_1 as year, metric_value_1 as area      │
    │    FROM chart_data                                          │
    │    WHERE region_code='haikou' AND product_code='litchi'    │
    │    AND chart_code='area_trend'                             │
    │    ORDER BY dimension_1 DESC LIMIT 5                       │
    │                                                             │
    │ 4. 数据结果：                                               │
    │    [                                                        │
    │      {year: "2024", area: 13.42},                          │
    │      {year: "2023", area: 12.53},                          │
    │      {year: "2022", area: 11.61}                           │
    │    ]                                                        │
    │                                                             │
    │ 5. 组装ECharts配置：                                        │
    │    从dashboard_position_widgets.chart_options获取基础配置   │
    │    + 填充实际数据                                           │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         地图数据获取                                               │
└─────────────────────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 4. 获取地图图层配置                                         │
    │    GET /api/v1/dashboard/map/layers?regionCode=haikou&...   │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 查询地图图层配置                                            │
    │                                                             │
    │ SELECT layer_code, layer_name, layer_icon, unit,           │
    │        is_default, color_scheme                             │
    │ FROM map_layer_config                                       │
    │ WHERE region_code = 'haikou'                                │
    │   AND product_code = 'litchi'                               │
    │   AND status = 1                                            │
    │ ORDER BY sort_order                                         │
    │                                                             │
    │ 结果：                                                      │
    │ [                                                           │
    │   {layer_code: "area", layer_name: "面积", is_default: 1}, │
    │   {layer_code: "production", layer_name: "产量"},          │
    │   {layer_code: "cooperative", layer_name: "合作社"}        │
    │ ]                                                           │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 5. 获取默认图层数据（面积）                                 │
    │    GET /api/v1/dashboard/map/data/area?regionCode=haikou... │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 查询地图数据                                                │
    │                                                             │
    │ SELECT sub_region_code, sub_region_name,                    │
    │        value_amount, value_unit, percentage, rank_order     │
    │ FROM map_data                                               │
    │ WHERE region_code = 'haikou'                                │
    │   AND product_code = 'litchi'                               │
    │   AND layer_code = 'area'                                   │
    │   AND data_year = 2024                                      │
    │                                                             │
    │ 结果：                                                      │
    │ [                                                           │
    │   {sub_region_name: "琼山区", value_amount: 8.99, rank: 1},│
    │   {sub_region_name: "秀英区", value_amount: 3.56, rank: 2},│
    │   {sub_region_name: "美兰区", value_amount: 0.57, rank: 3},│
    │   {sub_region_name: "龙华区", value_amount: 0.28, rank: 4} │
    │ ]                                                           │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         前端渲染页面                                               │
└─────────────────────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 6. 前端渲染完整页面                                         │
    │                                                             │
    │ ┌─────────┬─────────┬─────────┐                             │
    │ │面积趋势 │产量趋势 │品种分布 │ ← 根据组件配置渲染            │
    │ │柱状图   │折线图   │饼状图   │                               │
    │ ├─────────┼─────────┴─────────┤                             │
    │ │销售趋势 │   🗺️ 海口地图     │ ← 显示面积数据                │
    │ │柱状图   │   [面积][产量]    │   图层切换按钮                │
    │ ├─────────┤   [合作社]        │                               │
    │ │价格走势 │                   │                               │
    │ │折线图   ├─────────┬─────────┤                             │
    │ │         │合作社   │价格监测 │ ← 滚动列表                   │
    │ │         │柱状图   │滚动列表 │                               │
    │ └─────────┴─────────┴─────────┘                             │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         用户交互：地图图层切换                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 7. 用户点击"产量"按钮                                       │
    │    只调用地图数据接口，其他组件不变                         │
    │    GET /api/v1/dashboard/map/data/production?regionCode=... │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 查询产量图层数据                                            │
    │                                                             │
    │ SELECT sub_region_code, sub_region_name,                    │
    │        value_amount, value_unit, percentage, rank_order     │
    │ FROM map_data                                               │
    │ WHERE region_code = 'haikou'                                │
    │   AND product_code = 'litchi'                               │
    │   AND layer_code = 'production'  ← 只改变这个字段           │
    │   AND data_year = 2024                                      │
    │                                                             │
    │ 结果：                                                      │
    │ [                                                           │
    │   {sub_region_name: "琼山区", value_amount: 7.10},         │
    │   {sub_region_name: "秀英区", value_amount: 1.99},         │
    │   {sub_region_name: "美兰区", value_amount: 0.34},         │
    │   {sub_region_name: "龙华区", value_amount: 0.06}          │
    │ ]                                                           │
    └─────────────────────────────────────────────────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────────────────────────┐
    │ 8. 前端只更新地图区域                                       │
    │                                                             │
    │ ┌─────────┬─────────┬─────────┐                             │
    │ │面积趋势 │产量趋势 │品种分布 │ ← 保持不变                   │
    │ │柱状图   │折线图   │饼状图   │                               │
    │ ├─────────┼─────────┴─────────┤                             │
    │ │销售趋势 │   🗺️ 海口地图     │ ← 只更新地图数据              │
    │ │柱状图   │   [面积]🔸[产量]  │   产量按钮高亮                │
    │ ├─────────┤   [合作社]        │   显示产量分布                │
    │ │价格走势 │                   │                               │
    │ │折线图   ├─────────┬─────────┤                             │
    │ │         │合作社   │价格监测 │ ← 保持不变                   │
    │ │         │柱状图   │滚动列表 │                               │
    │ └─────────┴─────────┴─────────┘                             │
    └─────────────────────────────────────────────────────────────┘
```

---

## 🔍 关键配置机制说明

### 1. 数据库配置驱动
```
配置表作用：
├── dashboard_layout_positions     → 定义8个固定位置
├── dashboard_position_widgets     → 每个位置显示什么组件
├── map_layer_config              → 地图有哪些图层可切换
├── data_sources                  → 每个组件的数据来源
└── chart_data/map_data           → 实际的业务数据
```

### 2. 前端渲染逻辑
```
渲染流程：
1. 根据 layout_positions 创建8个容器
2. 根据 position_widgets 配置每个容器的组件类型
3. 调用对应的数据接口获取 ECharts 配置
4. 渲染图表到对应位置
```

### 3. 地图切换机制
```
切换流程：
1. 初始加载所有可用图层按钮（map_layer_config）
2. 默认显示第一个图层数据
3. 用户点击按钮时，只调用新图层的数据接口
4. 前端只更新地图组件，其他保持不变
```

### 4. 扩展新市县的配置步骤
```
新增三亚椰子：
1. sys_regions 已有三亚数据
2. sys_products 已有椰子数据  
3. 在 dashboard_position_widgets 中配置：
   - region_code = 'sanya'
   - product_code = 'coconut'  
   - 8个position的组件配置
4. 在 map_layer_config 中配置三亚椰子的图层
5. 导入 chart_data 和 map_data 业务数据
```

这样的设计完全实现了您要求的"后台配置驱动"，每个布局位置显示什么完全由数据库决定，地图切换也有独立的数据流。您觉得这个交互流程清楚吗？