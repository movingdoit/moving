# 海南18个市县一张图 - 原设计方案（优化版）

## 🎯 回到原始设计思路

您说得对，原来的设计思路更直观清晰：
- **固定8个布局位置**：左一、左二、左三、右一、右二、右三、中间地图、中下
- **按位置配置组件**：每个位置显示什么图表完全由后台配置
- **地图独立切换**：地图有专门的图层切换机制

---

## 📊 原始数据库设计（保持不变）

### 1. 系统基础表
```sql
-- 区域表：18个市县基础信息
CREATE TABLE sys_regions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE,
    region_name VARCHAR(100) NOT NULL,
    region_level TINYINT NOT NULL,
    region_type VARCHAR(20),
    geo_data JSON,
    center_point JSON,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 产品表：所有农产品信息  
CREATE TABLE sys_products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL,
    category_code VARCHAR(50),
    unit VARCHAR(20),
    icon_url VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. 布局配置表
```sql
-- 布局位置定义表（固定8个位置）
CREATE TABLE dashboard_layout_positions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_code VARCHAR(20) NOT NULL UNIQUE,
    position_name VARCHAR(50) NOT NULL,
    position_type VARCHAR(20) NOT NULL, -- widget,map,scroll
    grid_area VARCHAR(50),
    width_ratio DECIMAL(5,2),
    height_ratio DECIMAL(5,2),
    sort_order INT DEFAULT 0
);

-- 布局位置组件配置表
CREATE TABLE dashboard_position_widgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    position_code VARCHAR(20) NOT NULL,
    widget_code VARCHAR(50) NOT NULL,
    widget_title VARCHAR(200),
    widget_type VARCHAR(20) NOT NULL, -- chart,table,list,map
    chart_type VARCHAR(20), -- bar,line,pie,radar等
    data_source_code VARCHAR(50),
    widget_config JSON,
    chart_options JSON, -- ECharts配置选项
    refresh_interval INT DEFAULT 300,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_region_product_position (region_code, product_code, position_code)
);
```

### 3. 地图专用配置表
```sql
-- 地图图层配置表
CREATE TABLE map_layer_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    layer_code VARCHAR(50) NOT NULL,
    layer_name VARCHAR(100) NOT NULL,
    layer_icon VARCHAR(100),
    data_source_code VARCHAR(50) NOT NULL,
    value_field VARCHAR(50) NOT NULL,
    unit VARCHAR(20),
    color_scheme JSON,
    legend_config JSON,
    tooltip_template VARCHAR(500),
    is_default TINYINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_region_product_layer (region_code, product_code, layer_code)
);
```

---

## 💾 业务数据存储优化

### 针对您担心的数据存储问题，我们这样处理：

#### 方案：通用业务数据表 + 灵活字段设计

```sql
-- 统一的图表数据表（支持所有市县所有产品）
CREATE TABLE chart_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    chart_code VARCHAR(50) NOT NULL, -- area_trend, production_trend, price_trend等
    data_date DATE NOT NULL,
    
    -- 灵活的维度字段
    dimension_1 VARCHAR(100), -- 年份/月份/季度/品种
    dimension_2 VARCHAR(100), -- 区域/等级/规格
    dimension_3 VARCHAR(100), -- 其他分类
    
    -- 灵活的指标字段
    metric_value_1 DECIMAL(15,4), -- 面积/产量/价格/数量
    metric_value_2 DECIMAL(15,4), -- 收获面积/销售额/成本
    metric_value_3 DECIMAL(15,4), -- 其他指标
    
    metric_unit VARCHAR(20),
    extra_data JSON, -- 扩展数据
    data_source VARCHAR(100),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_chart (region_code, product_code, chart_code),
    INDEX idx_data_date (data_date),
    UNIQUE KEY uk_chart_data_record (region_code, product_code, chart_code, data_date, dimension_1, dimension_2)
);

-- 统一的地图数据表（支持所有市县所有产品）
CREATE TABLE map_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL, -- 父级区域（市县）
    sub_region_code VARCHAR(20) NOT NULL, -- 子区域（区/镇）
    sub_region_name VARCHAR(100) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    layer_code VARCHAR(50) NOT NULL, -- area,production,cooperative等
    data_year INT NOT NULL,
    
    value_amount DECIMAL(15,4) NOT NULL,
    value_unit VARCHAR(20),
    percentage DECIMAL(5,2),
    rank_order INT,
    geo_center_point JSON,
    extra_info JSON,
    data_source VARCHAR(100),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_layer_year (region_code, product_code, layer_code, data_year),
    UNIQUE KEY uk_map_data_record (region_code, sub_region_code, product_code, layer_code, data_year)
);

-- 统一的滚动列表数据表
CREATE TABLE scroll_list_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    list_code VARCHAR(50) NOT NULL, -- price_monitor, news, policy等
    data_date DATE NOT NULL,
    
    title VARCHAR(200),
    content TEXT,
    link_url VARCHAR(500),
    tags JSON,
    priority INT DEFAULT 0,
    extra_data JSON,
    data_source VARCHAR(100),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_list (region_code, product_code, list_code),
    INDEX idx_data_date_priority (data_date, priority DESC)
);
```

---

## 🔧 数据源配置表
```sql
-- 数据源配置表
CREATE TABLE data_sources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_code VARCHAR(50) NOT NULL UNIQUE,
    source_name VARCHAR(100) NOT NULL,
    source_type VARCHAR(20) NOT NULL, -- sql,api,static
    query_template TEXT, -- SQL查询模板
    query_params JSON, -- 查询参数配置
    data_mapping JSON, -- 数据字段映射
    cache_duration INT DEFAULT 300,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 💡 为什么这样设计更好？

### 1. 清晰的职责分离
```
配置表：
├── dashboard_layout_positions     → 定义8个固定位置
├── dashboard_position_widgets     → 每个位置显示什么组件
├── map_layer_config              → 地图有哪些图层可切换
└── data_sources                  → 每个组件的数据来源

数据表：
├── chart_data                    → 所有图表的业务数据
├── map_data                      → 所有地图的业务数据
└── scroll_list_data              → 所有列表的业务数据
```

### 2. 简单直观的配置方式
```sql
-- 海口荔枝的配置示例
INSERT INTO dashboard_position_widgets VALUES
('haikou', 'litchi', 'left_1', 'area_trend_chart', '近年海口荔枝面积情况', 'chart', 'bar', 'area_trend_data', NULL, '{}', 300),
('haikou', 'litchi', 'left_2', 'production_trend_chart', '近年海口荔枝产量情况', 'chart', 'line', 'production_trend_data', NULL, '{}', 300),
('haikou', 'litchi', 'left_3', 'variety_pie_chart', '海口荔枝主要品种分布', 'chart', 'pie', 'variety_distribution_data', NULL, '{}', 300);

-- 文昌椰子可以配置完全不同的图表
INSERT INTO dashboard_position_widgets VALUES
('wenchang', 'coconut', 'left_1', 'coconut_area_map', '文昌椰子种植分布', 'chart', 'map', 'coconut_area_data', NULL, '{}', 300),
('wenchang', 'coconut', 'left_2', 'coconut_yield_radar', '椰子产量多维分析', 'chart', 'radar', 'coconut_yield_data', NULL, '{}', 300);
```

### 3. 灵活的数据存储
```sql
-- 海口荔枝面积数据
INSERT INTO chart_data VALUES
('haikou', 'litchi', 'area_trend', '2024-12-31', '2024', NULL, NULL, 13.42, 11.45, NULL, '万亩', NULL, '农业局'),
('haikou', 'litchi', 'area_trend', '2023-12-31', '2023', NULL, NULL, 12.53, 10.93, NULL, '万亩', NULL, '农业局');

-- 文昌椰子面积数据（相同表，不同数据）
INSERT INTO chart_data VALUES
('wenchang', 'coconut', 'area_trend', '2024-12-31', '2024', NULL, NULL, 25.68, 22.15, NULL, '万亩', NULL, '农业局'),
('wenchang', 'coconut', 'area_trend', '2023-12-31', '2023', NULL, NULL, 24.12, 21.34, NULL, '万亩', NULL, '农业局');
```

---

## 🚀 新增市县产品的简单步骤

### 新增"定安粽子"
```sql
-- 1. 配置8个位置的组件（根据业务需要）
INSERT INTO dashboard_position_widgets VALUES
('dingan', 'zongzi', 'left_1', 'zongzi_production_chart', '定安粽子生产统计', 'chart', 'bar', 'zongzi_production_data', NULL, '{}', 300),
('dingan', 'zongzi', 'left_2', 'zongzi_sales_trend', '粽子销售趋势', 'chart', 'line', 'zongzi_sales_data', NULL, '{}', 300),
-- ... 其他6个位置

-- 2. 配置地图图层
INSERT INTO map_layer_config VALUES
('dingan', 'zongzi', 'production', '产量分布', 'production-icon', 'map_zongzi_production', 'value_amount', '万个', '{}', '{}', NULL, 1, 1, 1);

-- 3. 导入业务数据
INSERT INTO chart_data VALUES
('dingan', 'zongzi', 'production_trend', '2024-12-31', '2024', NULL, NULL, 850.5, NULL, NULL, '万个', NULL, '商务局');

INSERT INTO map_data VALUES
('dingan', 'dingan_dingcheng', '定城镇', 'zongzi', 'production', 2024, 425.8, '万个', 50.1, 1, NULL, NULL, '商务局');

-- 4. 立即生效
-- 前端访问 /dashboard?region=dingan&product=zongzi 即可显示
```

---

## 🎯 原设计的核心优势

1. **布局位置化**: 8个固定位置，清晰明确
2. **配置灵活性**: 每个市县产品可以配置完全不同的组件
3. **地图独立**: 地图切换有专门的配置和接口
4. **数据统一**: 虽然配置独立，但数据表是统一的
5. **易于理解**: 一目了然的表结构和配置关系

### 与复杂配置复用方案对比
| 特性 | 原设计 | 复杂方案 |
|------|--------|----------|
| 理解难度 | 简单直观 | 较复杂 |
| 配置灵活性 | 高 | 高 |
| 维护成本 | 低 | 中等 |
| 扩展性 | 好 | 很好 |
| 学习成本 | 低 | 较高 |

您说得对，原来的设计确实更加清晰简单，容易理解和维护。我们继续按照这个思路进行设计和开发！