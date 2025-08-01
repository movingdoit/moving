# 海南18个市县一张图 - 统一数据存储设计方案

## 🤔 问题分析

### 当前面临的问题
```
海南18个市县 × 多种农产品 = 大量数据组合

例如：
- 海口：荔枝、龙眼、香蕉、椰子、胡椒...
- 文昌：椰子、荔枝、地瓜、花生、海产品...
- 三亚：芒果、火龙果、莲雾、水稻...
- 儋州：橡胶、咖啡、柑橘、香蕉...

问题：
1. 是否每个市县每个产品都要单独建表？ ❌
2. 数据结构能否统一？ ✅
3. 如何保证数据的灵活性和通用性？ ✅
```

---

## 💡 解决方案：统一数据模型 + 灵活配置

### 核心思想
```
不是按市县×产品建表，而是按数据类型建通用表
所有市县、所有产品共用相同的数据表结构
通过字段区分不同的市县和产品
```

---

## 📊 重新设计的数据表结构

### 1. 系统配置表（保持不变）

```sql
-- 区域表：18个市县基础信息
sys_regions (
    region_code, region_name, region_type, 
    geo_data, center_point, status
)

-- 产品表：所有农产品信息
sys_products (
    product_code, product_name, category_code, 
    unit, icon_url, description, status
)

-- 布局位置定义表
dashboard_layout_positions (
    position_code, position_name, position_type,
    grid_area, width_ratio, height_ratio
)
```

### 2. 统一的业务数据表

#### 2.1 通用图表数据表（所有市县产品共用）
```sql
-- 统一的图表数据存储表
CREATE TABLE unified_chart_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '市县代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码', 
    data_category VARCHAR(50) NOT NULL COMMENT '数据类别：area_trend,production_trend,price_trend等',
    data_date DATE NOT NULL COMMENT '数据日期',
    
    -- 通用维度字段
    dimension_1 VARCHAR(100) COMMENT '维度1：年份/月份/季度',
    dimension_2 VARCHAR(100) COMMENT '维度2：品种/区域/等级',
    dimension_3 VARCHAR(100) COMMENT '维度3：其他分类',
    dimension_4 VARCHAR(100) COMMENT '维度4：扩展分类',
    
    -- 通用指标字段
    metric_value_1 DECIMAL(15,4) COMMENT '指标值1：面积/产量/价格',
    metric_value_2 DECIMAL(15,4) COMMENT '指标值2：收获面积/销售额',
    metric_value_3 DECIMAL(15,4) COMMENT '指标值3：成本/利润',
    metric_value_4 DECIMAL(15,4) COMMENT '指标值4：扩展指标',
    
    -- 通用属性字段
    unit VARCHAR(20) COMMENT '计量单位',
    data_source VARCHAR(100) COMMENT '数据来源',
    extra_attributes JSON COMMENT '额外属性（灵活扩展）',
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 复合索引优化查询
    INDEX idx_region_product_category (region_code, product_code, data_category),
    INDEX idx_data_date (data_date),
    
    -- 唯一约束防止重复数据
    UNIQUE KEY uk_chart_data_record (
        region_code, product_code, data_category, 
        data_date, dimension_1, dimension_2
    )
);
```

#### 2.2 通用地图数据表（所有市县产品共用）
```sql
-- 统一的地图数据存储表
CREATE TABLE unified_map_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '父级区域代码（市县）',
    sub_region_code VARCHAR(20) NOT NULL COMMENT '子区域代码（区/镇）',
    sub_region_name VARCHAR(100) NOT NULL COMMENT '子区域名称',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    layer_code VARCHAR(50) NOT NULL COMMENT '图层代码：area,production,cooperative等',
    data_year INT NOT NULL COMMENT '数据年份',
    
    -- 通用数值字段
    value_amount DECIMAL(15,4) NOT NULL COMMENT '主要数值',
    value_secondary DECIMAL(15,4) COMMENT '次要数值（如成本、利润等）',
    
    -- 统计字段
    percentage DECIMAL(5,2) COMMENT '占比(%)',
    rank_order INT COMMENT '排名',
    growth_rate DECIMAL(5,2) COMMENT '增长率(%)',
    
    -- 地理和扩展信息
    geo_center_point JSON COMMENT '地理中心点',
    extra_info JSON COMMENT '额外信息（产品特有属性）',
    
    unit VARCHAR(20) COMMENT '计量单位',
    data_source VARCHAR(100) COMMENT '数据来源',
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 复合索引
    INDEX idx_region_product_layer_year (region_code, product_code, layer_code, data_year),
    INDEX idx_sub_region (sub_region_code),
    
    -- 唯一约束
    UNIQUE KEY uk_map_data_record (
        region_code, sub_region_code, product_code, 
        layer_code, data_year
    )
);
```

#### 2.3 通用列表数据表
```sql
-- 统一的列表数据存储表（价格监测、新闻动态等）
CREATE TABLE unified_list_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    list_category VARCHAR(50) NOT NULL COMMENT '列表类别：price_monitor,news,policy等',
    data_date DATE NOT NULL COMMENT '数据日期',
    
    -- 通用内容字段
    title VARCHAR(200) COMMENT '标题',
    content TEXT COMMENT '内容',
    summary VARCHAR(500) COMMENT '摘要',
    
    -- 通用属性字段  
    price DECIMAL(10,4) COMMENT '价格（价格监测用）',
    change_rate DECIMAL(5,2) COMMENT '变化率（价格监测用）',
    tags JSON COMMENT '标签数组',
    
    -- 链接和优先级
    link_url VARCHAR(500) COMMENT '链接地址',
    priority INT DEFAULT 0 COMMENT '优先级',
    
    -- 扩展字段
    extra_data JSON COMMENT '额外数据（灵活扩展）',
    data_source VARCHAR(100) COMMENT '数据来源',
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_category (region_code, product_code, list_category),
    INDEX idx_data_date_priority (data_date, priority DESC)
);
```

---

## 🔧 配置表设计（支持多产品）

### 组件配置表（支持多产品复用）
```sql
-- 组件配置表（一个配置可以复用到多个市县产品）
CREATE TABLE dashboard_position_widgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_code VARCHAR(50) NOT NULL UNIQUE COMMENT '配置代码（可复用）',
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    
    -- 适用范围
    region_codes JSON COMMENT '适用区域数组 ["haikou","wenchang"] 或 ["*"] 表示全部',
    product_codes JSON COMMENT '适用产品数组 ["litchi","longan"] 或 ["*"] 表示全部',
    position_code VARCHAR(20) NOT NULL COMMENT '位置代码',
    
    -- 组件配置
    widget_title_template VARCHAR(200) COMMENT '标题模板：近年#{regionName}#{productName}面积情况',
    widget_type VARCHAR(20) NOT NULL COMMENT '组件类型',
    chart_type VARCHAR(20) COMMENT '图表类型',
    
    -- 数据源配置
    data_category VARCHAR(50) COMMENT '数据类别：对应unified_chart_data.data_category',
    query_config JSON COMMENT '查询配置',
    chart_options_template JSON COMMENT 'ECharts配置模板',
    
    -- 其他配置
    refresh_interval INT DEFAULT 300,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_config_code (config_code),
    INDEX idx_position (position_code)
);

-- 实际应用关系表（哪些市县产品使用了哪些配置）
CREATE TABLE dashboard_widget_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    position_code VARCHAR(20) NOT NULL,
    config_code VARCHAR(50) NOT NULL COMMENT '引用的配置代码',
    
    -- 个性化覆盖（可选）
    custom_title VARCHAR(200) COMMENT '自定义标题（覆盖模板）',
    custom_chart_options JSON COMMENT '自定义图表配置（覆盖模板）',
    
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_region_product_position (region_code, product_code, position_code),
    FOREIGN KEY (config_code) REFERENCES dashboard_position_widgets(config_code),
    INDEX idx_config_code (config_code)
);
```

### 地图图层配置表（支持多产品复用）
```sql
-- 地图图层配置表
CREATE TABLE map_layer_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_code VARCHAR(50) NOT NULL UNIQUE COMMENT '图层配置代码',
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    
    -- 适用范围
    region_codes JSON COMMENT '适用区域数组',
    product_codes JSON COMMENT '适用产品数组',
    
    layer_code VARCHAR(50) NOT NULL COMMENT '图层代码',
    layer_name_template VARCHAR(100) COMMENT '图层名称模板：#{productName}面积分布',
    layer_icon VARCHAR(100) COMMENT '图层图标',
    
    -- 数据配置
    map_data_layer_code VARCHAR(50) COMMENT '对应unified_map_data.layer_code',
    value_field VARCHAR(50) COMMENT '数值字段名',
    unit_template VARCHAR(20) COMMENT '单位模板',
    
    -- 样式配置
    color_scheme JSON COMMENT '颜色方案',
    legend_config JSON COMMENT '图例配置',
    tooltip_template VARCHAR(500) COMMENT '提示框模板',
    
    is_default TINYINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_config_code (config_code)
);

-- 地图图层应用关系表
CREATE TABLE map_layer_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    config_code VARCHAR(50) NOT NULL,
    
    -- 个性化配置
    custom_layer_name VARCHAR(100) COMMENT '自定义图层名称',
    custom_color_scheme JSON COMMENT '自定义颜色方案',
    
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_region_product_config (region_code, product_code, config_code),
    FOREIGN KEY (config_code) REFERENCES map_layer_configs(config_code)
);
```

---

## 💾 数据存储示例

### 1. 统一图表数据存储
```sql
-- 海口荔枝面积数据
INSERT INTO unified_chart_data VALUES
(1, 'haikou', 'litchi', 'area_trend', '2024-12-31', '2024', NULL, NULL, NULL, 13.42, 11.45, NULL, NULL, '万亩', '农业局', NULL),
(2, 'haikou', 'litchi', 'area_trend', '2023-12-31', '2023', NULL, NULL, NULL, 12.53, 10.93, NULL, NULL, '万亩', '农业局', NULL),

-- 文昌椰子面积数据（相同表结构）
(3, 'wenchang', 'coconut', 'area_trend', '2024-12-31', '2024', NULL, NULL, NULL, 25.68, 22.15, NULL, NULL, '万亩', '农业局', NULL),
(4, 'wenchang', 'coconut', 'area_trend', '2023-12-31', '2023', NULL, NULL, NULL, 24.12, 21.34, NULL, NULL, '万亩', '农业局', NULL),

-- 三亚芒果品种分布数据
(5, 'sanya', 'mango', 'variety_distribution', '2024-12-31', NULL, '台农1号', NULL, NULL, 45.6, NULL, NULL, NULL, '%', '品种统计', NULL),
(6, 'sanya', 'mango', 'variety_distribution', '2024-12-31', NULL, '贵妃芒', NULL, NULL, 32.4, NULL, NULL, NULL, '%', '品种统计', NULL);
```

### 2. 统一地图数据存储
```sql
-- 海口荔枝地图数据
INSERT INTO unified_map_data VALUES
(1, 'haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'area', 2024, 8.99, NULL, 66.9, 1, 5.2, NULL, '万亩', '统计局'),
(2, 'haikou', 'haikou_xiuying', '秀英区', 'litchi', 'area', 2024, 3.56, NULL, 26.5, 2, 2.1, NULL, '万亩', '统计局'),

-- 文昌椰子地图数据（相同表结构）
(3, 'wenchang', 'wenchang_wencheng', '文城镇', 'coconut', 'area', 2024, 12.45, NULL, 48.5, 1, 8.3, NULL, '万亩', '统计局'),
(4, 'wenchang', 'wenchang_longlou', '龙楼镇', 'coconut', 'area', 2024, 8.67, NULL, 33.8, 2, 6.1, NULL, '万亩', '统计局');
```

---

## 🔄 配置复用示例

### 1. 创建通用配置（可被多个市县产品复用）
```sql
-- 面积趋势图配置（适用于所有市县的所有产品）
INSERT INTO dashboard_position_widgets VALUES
(1, 'area_trend_chart_config', '面积趋势图通用配置', 
 '["*"]', '["*"]', 'left_1',
 '近年#{regionName}#{productName}面积情况', 'chart', 'bar',
 'area_trend', 
 '{"limit": 5, "order": "DESC"}',
 '{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "#{unit}"}}',
 300, 1, 1);

-- 产量趋势图配置（适用于部分产品）
INSERT INTO dashboard_position_widgets VALUES  
(2, 'production_trend_config', '产量趋势图配置',
 '["*"]', '["litchi", "coconut", "mango"]', 'left_2',
 '近年#{regionName}#{productName}产量情况', 'chart', 'line', 
 'production_trend',
 '{"limit": 5, "order": "DESC"}',
 '{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "#{unit}"}}',
 300, 2, 1);
```

### 2. 应用配置到具体市县产品
```sql
-- 海口荔枝使用通用配置
INSERT INTO dashboard_widget_applications VALUES
(1, 'haikou', 'litchi', 'left_1', 'area_trend_chart_config', NULL, NULL, 1),
(2, 'haikou', 'litchi', 'left_2', 'production_trend_config', NULL, NULL, 1),

-- 文昌椰子也使用相同配置
(3, 'wenchang', 'coconut', 'left_1', 'area_trend_chart_config', NULL, NULL, 1),
(4, 'wenchang', 'coconut', 'left_2', 'production_trend_config', NULL, NULL, 1),

-- 三亚芒果使用相同配置，但自定义标题
(5, 'sanya', 'mango', 'left_1', 'area_trend_chart_config', '三亚芒果种植规模分析', NULL, 1);
```

---

## 🎯 这样设计的优势

### 1. 数据表统一
```
✅ 只需要3个主要业务数据表
✅ 所有市县、所有产品共用相同结构
✅ 避免了 18×N 个表的爆炸式增长
```

### 2. 配置可复用
```
✅ 一个配置可以应用到多个市县产品
✅ 支持配置继承和个性化覆盖
✅ 新增市县产品只需要应用现有配置
```

### 3. 扩展性强
```
✅ 新增产品类型只需要插入数据，不需要改表结构
✅ JSON字段支持灵活扩展
✅ 模板化支持动态内容生成
```

### 4. 维护简单
```
✅ 统一的数据结构，便于数据导入和管理
✅ 配置修改可以批量生效到多个应用
✅ 数据查询和统计分析更加简单
```

---

## 📝 新增市县产品的步骤

### 假设新增"定安粽子"
```sql
-- 1. 确认基础数据（通常已存在）
-- sys_regions 中已有定安县
-- sys_products 中添加粽子

-- 2. 导入业务数据
INSERT INTO unified_chart_data VALUES
('dingan', 'zongzi', 'production_trend', '2024-12-31', '2024', NULL, NULL, NULL, 850.5, NULL, NULL, NULL, '万个', '商务局', NULL);

INSERT INTO unified_map_data VALUES  
('dingan', 'dingan_dingcheng', '定城镇', 'zongzi', 'production', 2024, 425.8, NULL, 50.1, 1, 12.5, NULL, '万个', '商务局');

-- 3. 应用现有配置（秒级完成）
INSERT INTO dashboard_widget_applications VALUES
('dingan', 'zongzi', 'left_1', 'area_trend_chart_config', '定安粽子生产规模统计', NULL, 1),
('dingan', 'zongzi', 'left_2', 'production_trend_config', NULL, NULL, 1);

-- 4. 立即生效
-- 前端访问 /dashboard?region=dingan&product=zongzi 即可显示
```

您觉得这种统一数据存储 + 配置复用的方案如何？这样就不需要为每个市县产品单独建表了！