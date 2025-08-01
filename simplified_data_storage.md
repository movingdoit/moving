# 海南18个市县一张图 - 简化数据存储设计

## 🗃️ 核心数据表设计（简化版）

### 1. 系统基础表（不变）
```sql
-- 区域表：18个市县基础信息
CREATE TABLE sys_regions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE,
    region_name VARCHAR(100) NOT NULL,
    region_type VARCHAR(20),
    center_point JSON,
    status TINYINT DEFAULT 1
);

-- 产品表：所有农产品信息
CREATE TABLE sys_products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL,
    unit VARCHAR(20),
    icon_url VARCHAR(255),
    status TINYINT DEFAULT 1
);
```

### 2. 布局配置表（不变）
```sql
-- 布局位置定义表（固定8个位置）
CREATE TABLE dashboard_layout_positions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_code VARCHAR(20) NOT NULL UNIQUE, -- left_1, left_2, left_3, right_1, right_2, right_3, center_map, center_bottom
    position_name VARCHAR(50) NOT NULL,
    position_type VARCHAR(20) NOT NULL, -- widget, map, scroll
    sort_order INT DEFAULT 0
);

-- 布局位置组件配置表
CREATE TABLE dashboard_position_widgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    position_code VARCHAR(20) NOT NULL,
    widget_title VARCHAR(200), -- 如：近年海口荔枝面积情况
    widget_type VARCHAR(20) NOT NULL, -- chart, list
    chart_type VARCHAR(20), -- bar, line, pie, radar等
    chart_options JSON, -- ECharts配置选项
    status TINYINT DEFAULT 1,
    
    UNIQUE KEY uk_region_product_position (region_code, product_code, position_code)
);

-- 地图图层配置表
CREATE TABLE map_layer_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    layer_code VARCHAR(50) NOT NULL, -- area, production, cooperative
    layer_name VARCHAR(100) NOT NULL, -- 面积、产量、合作社
    layer_icon VARCHAR(100),
    unit VARCHAR(20),
    color_scheme JSON,
    is_default TINYINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    
    UNIQUE KEY uk_region_product_layer (region_code, product_code, layer_code)
);
```

---

## 📊 业务数据表（按用途分类）

### 3. 各个位置对应的数据表

#### 3.1 图表统计数据表（对应6个图表位置：left_1, left_2, left_3, right_1, right_2, right_3）
```sql
-- 统计图表数据表
CREATE TABLE chart_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    statistic_type VARCHAR(50) NOT NULL, -- area_trend(面积趋势), production_trend(产量趋势), price_trend(价格趋势), variety_distribution(品种分布), sales_trend(销售趋势), cooperative_count(合作社统计)
    data_year VARCHAR(10) NOT NULL, -- 2024, 2023, 2022...
    data_month VARCHAR(10), -- 01, 02, 03... (月度数据用)
    
    -- 统计值字段
    value_1 DECIMAL(15,4), -- 主要数值：面积、产量、价格、数量等
    value_2 DECIMAL(15,4), -- 次要数值：收获面积、销售额等  
    value_3 DECIMAL(15,4), -- 其他数值：成本、利润等
    
    -- 分类字段
    category_1 VARCHAR(100), -- 品种、等级、规格等
    category_2 VARCHAR(100), -- 区域、渠道等
    
    unit VARCHAR(20), -- 万亩、万吨、元/斤、个等
    percentage DECIMAL(5,2), -- 占比(%)
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_type (region_code, product_code, statistic_type),
    INDEX idx_data_year (data_year),
    UNIQUE KEY uk_chart_record (region_code, product_code, statistic_type, data_year, data_month, category_1, category_2)
);
```

#### 3.2 地图数据表（对应center_map位置）
```sql
-- 地图数据表
CREATE TABLE map_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL, -- 父级区域（市县）
    sub_region_code VARCHAR(20) NOT NULL, -- 子区域代码（区/镇）
    sub_region_name VARCHAR(100) NOT NULL, -- 子区域名称
    product_code VARCHAR(50) NOT NULL,
    layer_type VARCHAR(50) NOT NULL, -- area(面积), production(产量), cooperative(合作社)
    data_year INT NOT NULL,
    
    value_amount DECIMAL(15,4) NOT NULL, -- 数值
    unit VARCHAR(20), -- 单位
    percentage DECIMAL(5,2), -- 占比(%)
    rank_order INT, -- 排名
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_layer_year (region_code, product_code, layer_type, data_year),
    UNIQUE KEY uk_map_record (region_code, sub_region_code, product_code, layer_type, data_year)
);
```

#### 3.3 滚动列表数据表（对应center_bottom位置）
```sql
-- 滚动列表数据表
CREATE TABLE scroll_news (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    news_type VARCHAR(50) NOT NULL, -- price_monitor(价格监测), market_news(市场动态), policy_news(政策资讯)
    
    title VARCHAR(200) NOT NULL, -- 标题
    content TEXT, -- 内容
    price DECIMAL(10,4), -- 价格（价格监测用）
    change_rate DECIMAL(5,2), -- 涨跌幅(%)（价格监测用）
    market VARCHAR(100), -- 市场名称
    
    publish_date DATE NOT NULL, -- 发布日期
    priority INT DEFAULT 0, -- 优先级
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_region_product_type (region_code, product_code, news_type),
    INDEX idx_publish_date_priority (publish_date DESC, priority DESC)
);
```

---

## 🎯 数据表与页面位置的对应关系

### 页面布局与数据表映射
```
┌─────────────┬─────────────┬─────────────┐
│ left_1      │ left_2      │ left_3      │ ← chart_statistics 表
│ 面积趋势图  │ 产量趋势图  │ 品种分布图  │   (statistic_type不同)
├─────────────┼─────────────┴─────────────┤
│ right_1     │        center_map         │ ← map_statistics 表
│ 销售趋势图  │        🗺️地图             │   (layer_type切换)
├─────────────┤                           │
│ right_2     │                           │
│ 价格走势图  │                           │
├─────────────┼─────────────┬─────────────┤
│ right_3     │  center_bottom           │ ← scroll_news 表
│ 合作社统计  │  📜滚动列表              │   (news_type不同)
└─────────────┴──────────────────────────┘

数据表对应：
├── chart_statistics  → 6个图表位置 (left_1, left_2, left_3, right_1, right_2, right_3)
├── map_statistics    → 1个地图位置 (center_map)
└── scroll_news       → 1个列表位置 (center_bottom)
```

---

## 💾 具体数据存储示例

### 1. 图表数据存储示例
```sql
-- 海口荔枝面积趋势数据（对应left_1位置）
INSERT INTO chart_statistics VALUES
(1, 'haikou', 'litchi', 'area_trend', '2024', NULL, 13.42, 11.45, NULL, NULL, NULL, '万亩', NULL),
(2, 'haikou', 'litchi', 'area_trend', '2023', NULL, 12.53, 10.93, NULL, NULL, NULL, '万亩', NULL),
(3, 'haikou', 'litchi', 'area_trend', '2022', NULL, 11.61, 9.68, NULL, NULL, NULL, '万亩', NULL);

-- 海口荔枝产量趋势数据（对应left_2位置）
INSERT INTO chart_statistics VALUES
(4, 'haikou', 'litchi', 'production_trend', '2024', NULL, 9.50, NULL, NULL, NULL, NULL, '万吨', NULL),
(5, 'haikou', 'litchi', 'production_trend', '2023', NULL, 7.58, NULL, NULL, NULL, NULL, '万吨', NULL);

-- 海口荔枝品种分布数据（对应left_3位置）
INSERT INTO chart_statistics VALUES
(6, 'haikou', 'litchi', 'variety_distribution', '2024', NULL, NULL, NULL, NULL, '妃子笑', NULL, NULL, 45.6),
(7, 'haikou', 'litchi', 'variety_distribution', '2024', NULL, NULL, NULL, NULL, '白糖罂', NULL, NULL, 32.4),
(8, 'haikou', 'litchi', 'variety_distribution', '2024', NULL, NULL, NULL, NULL, '桂味', NULL, NULL, 22.0);

-- 海口荔枝价格趋势数据（对应right_2位置）
INSERT INTO chart_statistics VALUES
(9, 'haikou', 'litchi', 'price_trend', '2024', '06', 16.8, NULL, NULL, NULL, NULL, '元/斤', NULL),
(10, 'haikou', 'litchi', 'price_trend', '2024', '07', 12.5, NULL, NULL, NULL, NULL, '元/斤', NULL);
```

### 2. 地图数据存储示例
```sql
-- 海口荔枝地图数据
INSERT INTO map_statistics VALUES
(1, 'haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'area', 2024, 8.99, '万亩', 66.9, 1),
(2, 'haikou', 'haikou_xiuying', '秀英区', 'litchi', 'area', 2024, 3.56, '万亩', 26.5, 2),
(3, 'haikou', 'haikou_meilan', '美兰区', 'litchi', 'area', 2024, 0.57, '万亩', 4.2, 3),
(4, 'haikou', 'haikou_longhua', '龙华区', 'litchi', 'area', 2024, 0.28, '万亩', 2.1, 4);

-- 产量图层数据
INSERT INTO map_statistics VALUES
(5, 'haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'production', 2024, 7.10, '万吨', 74.7, 1),
(6, 'haikou', 'haikou_xiuying', '秀英区', 'litchi', 'production', 2024, 1.99, '万吨', 20.9, 2);
```

### 3. 滚动列表数据存储示例
```sql
-- 荔枝价格监测数据
INSERT INTO scroll_news VALUES
(1, 'haikou', 'litchi', 'price_monitor', '妃子笑荔枝批发价格', '今日琼山区妃子笑荔枝批发价格16.48元/斤', 16.48, -0.91, '海口南北蔬菜批发市场', '2024-01-20', 1),
(2, 'haikou', 'litchi', 'price_monitor', '白糖罂荔枝零售价格', '秀英区白糖罂荔枝零售价格19.86元/斤', 19.86, 2.15, '秀英港农贸市场', '2024-01-20', 2);

-- 市场动态数据
INSERT INTO scroll_news VALUES
(3, 'haikou', 'litchi', 'market_news', '海口荔枝出口创新高', '2024年海口荔枝出口量达到5000吨，同比增长25%', NULL, NULL, NULL, '2024-01-19', 1),
(4, 'haikou', 'litchi', 'market_news', '荔枝深加工产业发展', '海口市推进荔枝深加工产业，开发荔枝干、荔枝酒等产品', NULL, NULL, NULL, '2024-01-18', 2);
```

---

## 🔍 数据查询示例

### 前端调用时的SQL查询
```sql
-- 获取left_1位置的面积趋势图数据
SELECT data_year, value_1 as planting_area, value_2 as harvest_area, unit
FROM chart_statistics 
WHERE region_code = 'haikou' 
  AND product_code = 'litchi' 
  AND statistic_type = 'area_trend'
ORDER BY data_year DESC 
LIMIT 5;

-- 获取地图面积图层数据
SELECT sub_region_name, value_amount, unit, percentage, rank_order
FROM map_statistics
WHERE region_code = 'haikou'
  AND product_code = 'litchi' 
  AND layer_type = 'area'
  AND data_year = 2024;

-- 获取center_bottom位置的价格监测数据
SELECT title, content, price, change_rate, market, publish_date
FROM scroll_news
WHERE region_code = 'haikou'
  AND product_code = 'litchi'
  AND news_type = 'price_monitor'
ORDER BY publish_date DESC, priority DESC
LIMIT 10;
```

---

## 🎯 总结

### 简化后的设计优势
1. **数据表清晰**: 只有3个业务数据表，职责明确
2. **位置对应明确**: 每个位置的数据来源一目了然
3. **无需数据源表**: 直接从统一数据库查询，简化了架构
4. **易于维护**: 数据结构简单，容易理解和操作
5. **扩展性好**: 新增市县产品只需要插入相应数据

### 数据表功能总结
- **chart_statistics**: 存储所有图表位置的统计数据
- **map_statistics**: 存储地图位置的分区域数据  
- **scroll_news**: 存储滚动列表位置的资讯数据

这样设计是不是更清晰了？每个位置的数据来源都很明确！