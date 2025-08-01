-- ================================================================
-- 海南18个市县一张图优化数据库设计
-- 按照具体布局位置重新设计表结构
-- 创建时间: 2024-01-20
-- 版本: v2.0
-- ================================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hainan_dashboard DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hainan_dashboard;

-- ================================================================
-- 系统基础表（保持不变）
-- ================================================================

-- 1. 行政区划表
DROP TABLE IF EXISTS sys_regions;
CREATE TABLE sys_regions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE COMMENT '区划代码',
    region_name VARCHAR(100) NOT NULL COMMENT '区划名称',
    parent_code VARCHAR(20) COMMENT '父级代码',
    region_level TINYINT NOT NULL COMMENT '行政级别：1-省,2-市,3-县,4-镇',
    region_type VARCHAR(20) COMMENT '区划类型：province,city,county,town',
    geo_data JSON COMMENT '地理边界数据',
    center_point JSON COMMENT '中心点坐标',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用,0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_code (region_code)
);

-- 2. 产品分类表
DROP TABLE IF EXISTS sys_products;
CREATE TABLE sys_products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) NOT NULL UNIQUE COMMENT '产品代码',
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    category_code VARCHAR(50) COMMENT '分类代码',
    category_name VARCHAR(100) COMMENT '分类名称',
    unit VARCHAR(20) COMMENT '计量单位',
    icon_url VARCHAR(255) COMMENT '图标地址',
    description TEXT COMMENT '产品描述',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_code (product_code)
);

-- ================================================================
-- 布局配置表（重新设计）
-- ================================================================

-- 3. 页面布局位置定义表
DROP TABLE IF EXISTS dashboard_layout_positions;
CREATE TABLE dashboard_layout_positions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_code VARCHAR(20) NOT NULL UNIQUE COMMENT '位置代码',
    position_name VARCHAR(50) NOT NULL COMMENT '位置名称',
    position_type VARCHAR(20) NOT NULL COMMENT '位置类型：widget,map,scroll',
    grid_area VARCHAR(50) COMMENT 'CSS Grid区域定义',
    width_ratio DECIMAL(5,2) COMMENT '宽度比例',
    height_ratio DECIMAL(5,2) COMMENT '高度比例',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_position_code (position_code)
);

-- 插入布局位置定义
INSERT INTO dashboard_layout_positions (position_code, position_name, position_type, grid_area, width_ratio, height_ratio, sort_order) VALUES
('left_1', '左一区域', 'widget', 'left-1', 33.33, 33.33, 1),
('left_2', '左二区域', 'widget', 'left-2', 33.33, 33.33, 2),
('left_3', '左三区域', 'widget', 'left-3', 33.33, 33.33, 3),
('center_map', '中间地图区域', 'map', 'center-map', 33.34, 66.67, 4),
('center_bottom', '中下区域', 'scroll', 'center-bottom', 33.34, 33.33, 5),
('right_1', '右一区域', 'widget', 'right-1', 33.33, 33.33, 6),
('right_2', '右二区域', 'widget', 'right-2', 33.33, 33.33, 7),
('right_3', '右三区域', 'widget', 'right-3', 33.33, 33.33, 8);

-- 4. 布局位置组件配置表
DROP TABLE IF EXISTS dashboard_position_widgets;
CREATE TABLE dashboard_position_widgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '适用区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '适用产品代码',
    position_code VARCHAR(20) NOT NULL COMMENT '位置代码',
    widget_code VARCHAR(50) NOT NULL COMMENT '组件代码',
    widget_title VARCHAR(200) COMMENT '组件标题',
    widget_type VARCHAR(20) NOT NULL COMMENT '组件类型：chart,table,list,map',
    chart_type VARCHAR(20) COMMENT '图表类型：bar,line,pie,radar等',
    data_source_code VARCHAR(50) COMMENT '数据源代码',
    widget_config JSON COMMENT '组件特定配置',
    chart_options JSON COMMENT 'ECharts配置选项',
    refresh_interval INT DEFAULT 300 COMMENT '刷新间隔(秒)',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_region_product_position (region_code, product_code, position_code),
    INDEX idx_widget_code (widget_code),
    INDEX idx_position (position_code)
);

-- ================================================================
-- 地图专用配置表
-- ================================================================

-- 5. 地图图层配置表
DROP TABLE IF EXISTS map_layer_config;
CREATE TABLE map_layer_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    layer_code VARCHAR(50) NOT NULL COMMENT '图层代码',
    layer_name VARCHAR(100) NOT NULL COMMENT '图层名称',
    layer_icon VARCHAR(100) COMMENT '图层图标',
    data_source_code VARCHAR(50) NOT NULL COMMENT '数据源代码',
    value_field VARCHAR(50) NOT NULL COMMENT '数值字段',
    unit VARCHAR(20) COMMENT '单位',
    color_scheme JSON COMMENT '颜色方案配置',
    legend_config JSON COMMENT '图例配置',
    tooltip_template VARCHAR(500) COMMENT '提示框模板',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认图层',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_region_product_layer (region_code, product_code, layer_code),
    INDEX idx_layer_code (layer_code)
);

-- ================================================================
-- 数据源配置表（优化）
-- ================================================================

-- 6. 数据源配置表
DROP TABLE IF EXISTS data_sources;
CREATE TABLE data_sources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_code VARCHAR(50) NOT NULL UNIQUE COMMENT '数据源代码',
    source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    source_type VARCHAR(20) NOT NULL COMMENT '数据源类型：sql,api,static',
    query_template TEXT COMMENT 'SQL查询模板或API地址',
    query_params JSON COMMENT '查询参数配置',
    data_mapping JSON COMMENT '数据字段映射',
    cache_duration INT DEFAULT 300 COMMENT '缓存时长(秒)',
    update_frequency VARCHAR(20) DEFAULT 'manual' COMMENT '更新频率',
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_source_code (source_code),
    INDEX idx_source_type (source_type)
);

-- ================================================================
-- 业务数据表（按布局位置优化）
-- ================================================================

-- 7. 统计图表数据表
DROP TABLE IF EXISTS chart_data;
CREATE TABLE chart_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    chart_code VARCHAR(50) NOT NULL COMMENT '图表代码',
    data_date DATE NOT NULL COMMENT '数据日期',
    dimension_1 VARCHAR(100) COMMENT '维度1（如年份）',
    dimension_2 VARCHAR(100) COMMENT '维度2（如品种）',
    dimension_3 VARCHAR(100) COMMENT '维度3（如区域）',
    metric_value_1 DECIMAL(15,4) COMMENT '指标值1',
    metric_value_2 DECIMAL(15,4) COMMENT '指标值2',
    metric_value_3 DECIMAL(15,4) COMMENT '指标值3',
    metric_unit VARCHAR(20) COMMENT '指标单位',
    extra_data JSON COMMENT '额外数据',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_chart (region_code, product_code, chart_code),
    INDEX idx_data_date (data_date),
    UNIQUE KEY uk_chart_data_record (region_code, product_code, chart_code, data_date, dimension_1, dimension_2)
);

-- 8. 地图数据表
DROP TABLE IF EXISTS map_data;
CREATE TABLE map_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '父级区域代码',
    sub_region_code VARCHAR(20) NOT NULL COMMENT '子区域代码',
    sub_region_name VARCHAR(100) NOT NULL COMMENT '子区域名称',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    layer_code VARCHAR(50) NOT NULL COMMENT '图层代码',
    data_year INT NOT NULL COMMENT '数据年份',
    value_amount DECIMAL(15,4) NOT NULL COMMENT '数值',
    value_unit VARCHAR(20) COMMENT '单位',
    percentage DECIMAL(5,2) COMMENT '占比(%)',
    rank_order INT COMMENT '排名',
    geo_center_point JSON COMMENT '地理中心点',
    extra_info JSON COMMENT '额外信息',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_layer_year (region_code, product_code, layer_code, data_year),
    INDEX idx_sub_region (sub_region_code),
    UNIQUE KEY uk_map_data_record (region_code, sub_region_code, product_code, layer_code, data_year)
);

-- 9. 滚动列表数据表
DROP TABLE IF EXISTS scroll_list_data;
CREATE TABLE scroll_list_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    list_code VARCHAR(50) NOT NULL COMMENT '列表代码',
    data_date DATE NOT NULL COMMENT '数据日期',
    title VARCHAR(200) COMMENT '标题',
    content TEXT COMMENT '内容',
    link_url VARCHAR(500) COMMENT '链接地址',
    tags JSON COMMENT '标签数组',
    priority INT DEFAULT 0 COMMENT '优先级',
    extra_data JSON COMMENT '额外数据',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_list (region_code, product_code, list_code),
    INDEX idx_data_date_priority (data_date, priority DESC)
);

-- ================================================================
-- 插入配置数据
-- ================================================================

-- 插入产品数据
INSERT INTO sys_products (product_code, product_name, category_code, category_name, unit, description) VALUES
('litchi', '荔枝', 'tropical_fruit', '热带水果', '万亩', '海南特色热带水果'),
('coconut', '椰子', 'tropical_fruit', '热带水果', '万亩', '海南传统优势作物'),
('mango', '芒果', 'tropical_fruit', '热带水果', '万亩', '热带水果之王'),
('banana', '香蕉', 'tropical_fruit', '热带水果', '万亩', '常年可采收的热带水果'),
('rubber', '橡胶', 'cash_crop', '经济作物', '万亩', '重要的工业原料作物'),
('pepper', '胡椒', 'spice_crop', '香料作物', '万亩', '世界著名的香料作物');

-- 插入海南18个市县数据
INSERT INTO sys_regions (region_code, region_name, parent_code, region_level, region_type, center_point) VALUES
('hainan', '海南省', NULL, 1, 'province', '{"lng": 110.3293, "lat": 19.8516}'),
('haikou', '海口市', 'hainan', 2, 'city', '{"lng": 110.3293, "lat": 20.0458}'),
('sanya', '三亚市', 'hainan', 2, 'city', '{"lng": 109.5119, "lat": 18.2577}'),
('sansha', '三沙市', 'hainan', 2, 'city', '{"lng": 112.3486, "lat": 16.8301}'),
('danzhou', '儋州市', 'hainan', 2, 'city', '{"lng": 109.5765, "lat": 19.5175}'),
('wuzhishan', '五指山市', 'hainan', 3, 'county_city', '{"lng": 109.5169, "lat": 18.7769}'),
('qionghai', '琼海市', 'hainan', 3, 'county_city', '{"lng": 110.4665, "lat": 19.2463}'),
('wenchang', '文昌市', 'hainan', 3, 'county_city', '{"lng": 110.7539, "lat": 19.6123}'),
('wanning', '万宁市', 'hainan', 3, 'county_city', '{"lng": 110.3883, "lat": 18.7962}'),
('dongfang', '东方市', 'hainan', 3, 'county_city', '{"lng": 108.6540, "lat": 19.0956}'),
('ding_an', '定安县', 'hainan', 3, 'county', '{"lng": 110.3235, "lat": 19.6850}'),
('tunchang', '屯昌县', 'hainan', 3, 'county', '{"lng": 110.1026, "lat": 19.3629}'),
('chengmai', '澄迈县', 'hainan', 3, 'county', '{"lng": 110.0073, "lat": 19.7370}'),
('lingao', '临高县', 'hainan', 3, 'county', '{"lng": 109.6877, "lat": 19.9083}'),
('baisha', '白沙黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.4526, "lat": 19.2246}'),
('changjiang', '昌江黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.0531, "lat": 19.2609}'),
('ledong', '乐东黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.1739, "lat": 18.7473}'),
('lingshui', '陵水黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 110.0372, "lat": 18.5048}'),
('baoting', '保亭黎族苗族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.7017, "lat": 18.6365}'),
('qiongzhong', '琼中黎族苗族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.8397, "lat": 19.0356}');

-- 插入数据源配置
INSERT INTO data_sources (source_code, source_name, source_type, query_template, data_mapping, cache_duration) VALUES
-- 面积趋势数据源
('area_trend_data', '面积趋势数据', 'sql',
'SELECT dimension_1 as year, metric_value_1 as planting_area, metric_value_2 as harvest_area 
 FROM chart_data 
 WHERE region_code = #{regionCode} AND product_code = #{productCode} AND chart_code = "area_trend"
 ORDER BY dimension_1 DESC LIMIT 5',
'{"categories": "year", "series": [{"name": "种植面积", "field": "planting_area"}, {"name": "收获面积", "field": "harvest_area"}]}', 600),

-- 产量趋势数据源
('production_trend_data', '产量趋势数据', 'sql',
'SELECT dimension_1 as year, metric_value_1 as production 
 FROM chart_data 
 WHERE region_code = #{regionCode} AND product_code = #{productCode} AND chart_code = "production_trend"
 ORDER BY dimension_1 DESC LIMIT 5',
'{"categories": "year", "series": [{"name": "产量", "field": "production"}]}', 600),

-- 地图面积数据源
('map_area_data', '地图面积数据', 'sql',
'SELECT sub_region_code, sub_region_name, value_amount, value_unit
 FROM map_data 
 WHERE region_code = #{regionCode} AND product_code = #{productCode} AND layer_code = "area" AND data_year = #{year}',
'{"name_field": "sub_region_name", "value_field": "value_amount", "code_field": "sub_region_code"}', 1800),

-- 地图产量数据源
('map_production_data', '地图产量数据', 'sql',
'SELECT sub_region_code, sub_region_name, value_amount, value_unit
 FROM map_data 
 WHERE region_code = #{regionCode} AND product_code = #{productCode} AND layer_code = "production" AND data_year = #{year}',
'{"name_field": "sub_region_name", "value_field": "value_amount", "code_field": "sub_region_code"}', 1800),

-- 地图合作社数据源
('map_cooperative_data', '地图合作社数据', 'sql',
'SELECT sub_region_code, sub_region_name, value_amount, value_unit
 FROM map_data 
 WHERE region_code = #{regionCode} AND product_code = #{productCode} AND layer_code = "cooperative" AND data_year = #{year}',
'{"name_field": "sub_region_name", "value_field": "value_amount", "code_field": "sub_region_code"}', 1800);

-- 插入海口荔枝的布局配置
INSERT INTO dashboard_position_widgets (region_code, product_code, position_code, widget_code, widget_title, widget_type, chart_type, data_source_code, chart_options) VALUES
-- 左一：面积趋势图
('haikou', 'litchi', 'left_1', 'area_trend_chart', '近年海口荔枝面积情况', 'chart', 'bar', 'area_trend_data',
'{"tooltip": {"trigger": "axis"}, "legend": {"show": true, "bottom": "5%"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "万亩"}, "series": [{"type": "bar", "barWidth": 20}, {"type": "bar", "barWidth": 20}]}'),

-- 左二：产量趋势图
('haikou', 'litchi', 'left_2', 'production_trend_chart', '近年海口荔枝产量情况', 'chart', 'line', 'production_trend_data',
'{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "万吨"}, "series": [{"type": "line", "smooth": true, "areaStyle": {}}]}'),

-- 左三：品种分布饼图
('haikou', 'litchi', 'left_3', 'variety_pie_chart', '海口荔枝主要品种分布', 'chart', 'pie', 'variety_distribution_data',
'{"tooltip": {"trigger": "item"}, "legend": {"orient": "vertical", "left": "left"}, "series": [{"type": "pie", "radius": "60%", "center": ["60%", "50%"]}]}'),

-- 右一：销售额趋势
('haikou', 'litchi', 'right_1', 'sales_trend_chart', '近年海口荔枝销售额情况', 'chart', 'bar', 'sales_trend_data',
'{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "亿元"}, "series": [{"type": "bar"}]}'),

-- 右二：价格走势
('haikou', 'litchi', 'right_2', 'price_trend_chart', '近期海口荔枝价格情况', 'chart', 'line', 'price_trend_data',
'{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "元/斤"}, "series": [{"type": "line", "smooth": true}]}'),

-- 右三：合作社统计
('haikou', 'litchi', 'right_3', 'cooperative_stats_chart', '海口荔枝合作社统计', 'chart', 'bar', 'cooperative_stats_data',
'{"tooltip": {"trigger": "axis"}, "xAxis": {"type": "category"}, "yAxis": {"type": "value", "name": "个"}, "series": [{"type": "bar"}]}');

-- 插入地图图层配置
INSERT INTO map_layer_config (region_code, product_code, layer_code, layer_name, layer_icon, data_source_code, value_field, unit, color_scheme, is_default, sort_order) VALUES
('haikou', 'litchi', 'area', '面积', 'area-icon', 'map_area_data', 'value_amount', '万亩', 
'{"type": "continuous", "min": 0, "max": 10, "colors": ["#dff6ff", "#47b5ff", "#1363df"]}', 1, 1),

('haikou', 'litchi', 'production', '产量', 'production-icon', 'map_production_data', 'value_amount', '万吨',
'{"type": "continuous", "min": 0, "max": 8, "colors": ["#f6fdd8", "#ffc54d", "#ff8400"]}', 0, 2),

('haikou', 'litchi', 'cooperative', '合作社', 'cooperative-icon', 'map_cooperative_data', 'value_amount', '个',
'{"type": "continuous", "min": 0, "max": 60, "colors": ["#e4fde2", "#74d680", "#2b9348"]}', 0, 3);

-- 插入测试数据
-- 图表数据
INSERT INTO chart_data (region_code, product_code, chart_code, data_date, dimension_1, metric_value_1, metric_value_2, metric_unit) VALUES
('haikou', 'litchi', 'area_trend', '2020-12-31', '2020', 9.32, 6.95, '万亩'),
('haikou', 'litchi', 'area_trend', '2021-12-31', '2021', 10.99, 9.24, '万亩'),
('haikou', 'litchi', 'area_trend', '2022-12-31', '2022', 11.61, 9.68, '万亩'),
('haikou', 'litchi', 'area_trend', '2023-12-31', '2023', 12.53, 10.93, '万亩'),
('haikou', 'litchi', 'area_trend', '2024-12-31', '2024', 13.42, 11.45, '万亩'),

('haikou', 'litchi', 'production_trend', '2020-12-31', '2020', 4.30, NULL, '万吨'),
('haikou', 'litchi', 'production_trend', '2021-12-31', '2021', 6.91, NULL, '万吨'),
('haikou', 'litchi', 'production_trend', '2022-12-31', '2022', 7.89, NULL, '万吨'),
('haikou', 'litchi', 'production_trend', '2023-12-31', '2023', 7.58, NULL, '万吨'),
('haikou', 'litchi', 'production_trend', '2024-12-31', '2024', 9.50, NULL, '万吨');

-- 地图数据
INSERT INTO map_data (region_code, sub_region_code, sub_region_name, product_code, layer_code, data_year, value_amount, value_unit) VALUES
-- 面积数据
('haikou', 'haikou_xiuying', '秀英区', 'litchi', 'area', 2024, 3.56, '万亩'),
('haikou', 'haikou_longhua', '龙华区', 'litchi', 'area', 2024, 0.28, '万亩'),
('haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'area', 2024, 8.99, '万亩'),
('haikou', 'haikou_meilan', '美兰区', 'litchi', 'area', 2024, 0.57, '万亩'),

-- 产量数据
('haikou', 'haikou_xiuying', '秀英区', 'litchi', 'production', 2024, 1.99, '万吨'),
('haikou', 'haikou_longhua', '龙华区', 'litchi', 'production', 2024, 0.06, '万吨'),
('haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'production', 2024, 7.10, '万吨'),
('haikou', 'haikou_meilan', '美兰区', 'litchi', 'production', 2024, 0.34, '万吨'),

-- 合作社数据
('haikou', 'haikou_xiuying', '秀英区', 'litchi', 'cooperative', 2024, 57, '个'),
('haikou', 'haikou_longhua', '龙华区', 'litchi', 'cooperative', 2024, 3, '个'),
('haikou', 'haikou_qiongshan', '琼山区', 'litchi', 'cooperative', 2024, 40, '个'),
('haikou', 'haikou_meilan', '美兰区', 'litchi', 'cooperative', 2024, 1, '个');

COMMIT;

-- ================================================================
-- 创建索引
-- ================================================================
CREATE INDEX idx_position_widgets_lookup ON dashboard_position_widgets(region_code, product_code, status);
CREATE INDEX idx_map_layer_lookup ON map_layer_config(region_code, product_code, status);
CREATE INDEX idx_chart_data_lookup ON chart_data(region_code, product_code, chart_code, data_date);
CREATE INDEX idx_map_data_lookup ON map_data(region_code, product_code, layer_code, data_year);

SELECT 'Layout-optimized database initialization completed successfully!' as message;