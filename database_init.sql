-- ================================================================
-- 海南18个市县一张图数据库初始化脚本
-- 创建时间: 2024-01-20
-- 版本: v1.0
-- ================================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hainan_dashboard DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hainan_dashboard;

-- ================================================================
-- 系统配置表
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
    center_point JSON COMMENT '中心点坐标 {"lng": 110.123, "lat": 20.456}',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用,0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_code (region_code),
    INDEX idx_parent_code (parent_code),
    INDEX idx_region_level (region_level)
);

-- 插入海南省及18个市县数据
INSERT INTO sys_regions (region_code, region_name, parent_code, region_level, region_type, center_point) VALUES
-- 海南省
('hainan', '海南省', NULL, 1, 'province', '{"lng": 110.3293, "lat": 19.8516}'),

-- 4个地级市
('haikou', '海口市', 'hainan', 2, 'city', '{"lng": 110.3293, "lat": 20.0458}'),
('sanya', '三亚市', 'hainan', 2, 'city', '{"lng": 109.5119, "lat": 18.2577}'),
('sansha', '三沙市', 'hainan', 2, 'city', '{"lng": 112.3486, "lat": 16.8301}'),
('danzhou', '儋州市', 'hainan', 2, 'city', '{"lng": 109.5765, "lat": 19.5175}'),

-- 5个县级市
('wuzhishan', '五指山市', 'hainan', 3, 'county_city', '{"lng": 109.5169, "lat": 18.7769}'),
('qionghai', '琼海市', 'hainan', 3, 'county_city', '{"lng": 110.4665, "lat": 19.2463}'),
('wenchang', '文昌市', 'hainan', 3, 'county_city', '{"lng": 110.7539, "lat": 19.6123}'),
('wanning', '万宁市', 'hainan', 3, 'county_city', '{"lng": 110.3883, "lat": 18.7962}'),
('dongfang', '东方市', 'hainan', 3, 'county_city', '{"lng": 108.6540, "lat": 19.0956}'),

-- 4个县
('ding_an', '定安县', 'hainan', 3, 'county', '{"lng": 110.3235, "lat": 19.6850}'),
('tunchang', '屯昌县', 'hainan', 3, 'county', '{"lng": 110.1026, "lat": 19.3629}'),
('chengmai', '澄迈县', 'hainan', 3, 'county', '{"lng": 110.0073, "lat": 19.7370}'),
('lingao', '临高县', 'hainan', 3, 'county', '{"lng": 109.6877, "lat": 19.9083}'),

-- 5个自治县
('baisha', '白沙黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.4526, "lat": 19.2246}'),
('changjiang', '昌江黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.0531, "lat": 19.2609}'),
('ledong', '乐东黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.1739, "lat": 18.7473}'),
('lingshui', '陵水黎族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 110.0372, "lat": 18.5048}'),
('baoting', '保亭黎族苗族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.7017, "lat": 18.6365}'),
('qiongzhong', '琼中黎族苗族自治县', 'hainan', 3, 'autonomous_county', '{"lng": 109.8397, "lat": 19.0356}');

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
    INDEX idx_product_code (product_code),
    INDEX idx_category (category_code)
);

-- 插入产品数据
INSERT INTO sys_products (product_code, product_name, category_code, category_name, unit, description) VALUES
-- 热带水果
('litchi', '荔枝', 'tropical_fruit', '热带水果', '万亩', '海南特色热带水果，主要品种有妃子笑、白糖罂、荔枝王等'),
('coconut', '椰子', 'tropical_fruit', '热带水果', '万亩', '海南传统优势作物，椰汁椰肉用途广泛'),
('mango', '芒果', 'tropical_fruit', '热带水果', '万亩', '热带水果之王，品种丰富'),
('banana', '香蕉', 'tropical_fruit', '热带水果', '万亩', '常年可采收的热带水果'),
('longan', '龙眼', 'tropical_fruit', '热带水果', '万亩', '营养丰富的热带水果'),
('rambutan', '红毛丹', 'tropical_fruit', '热带水果', '万亩', '外形独特的热带水果'),
('jackfruit', '菠萝蜜', 'tropical_fruit', '热带水果', '万亩', '世界上最重的水果'),

-- 经济作物
('rubber', '橡胶', 'cash_crop', '经济作物', '万亩', '重要的工业原料作物'),
('pepper', '胡椒', 'spice_crop', '香料作物', '万亩', '世界著名的香料作物'),
('coffee', '咖啡', 'beverage_crop', '饮料作物', '万亩', '热带地区重要的饮料作物'),
('vanilla', '香草兰', 'spice_crop', '香料作物', '万亩', '珍贵的天然香料'),

-- 蔬菜作物
('okra', '秋葵', 'vegetable', '蔬菜', '万亩', '营养价值很高的蔬菜作物'),
('bitter_gourd', '苦瓜', 'vegetable', '蔬菜', '万亩', '具有药用价值的蔬菜'),

-- 粮食作物
('rice', '水稻', 'grain', '粮食作物', '万亩', '主要粮食作物'),
('sweet_potato', '番薯', 'tuber_crop', '薯类作物', '万亩', '重要的粮食和饲料作物');

-- ================================================================
-- 仪表板配置表
-- ================================================================

-- 3. 页面布局配置表
DROP TABLE IF EXISTS dashboard_layouts;
CREATE TABLE dashboard_layouts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    layout_code VARCHAR(50) NOT NULL COMMENT '布局代码',
    layout_name VARCHAR(100) NOT NULL COMMENT '布局名称',
    layout_version VARCHAR(20) DEFAULT 'v1.0' COMMENT '布局版本',
    region_codes JSON COMMENT '适用区域代码数组',
    product_codes JSON COMMENT '适用产品代码数组',
    layout_config JSON NOT NULL COMMENT '布局配置JSON',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认布局',
    status TINYINT DEFAULT 1,
    created_by BIGINT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_layout_code (layout_code),
    INDEX idx_default (is_default),
    UNIQUE KEY uk_layout_version (layout_code, layout_version)
);

-- 4. 组件配置表
DROP TABLE IF EXISTS dashboard_widgets;
CREATE TABLE dashboard_widgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    widget_code VARCHAR(50) NOT NULL UNIQUE COMMENT '组件代码',
    widget_name VARCHAR(100) NOT NULL COMMENT '组件名称',
    widget_type VARCHAR(50) NOT NULL COMMENT '组件类型：Chart,Table,Map,ScrollList',
    chart_type VARCHAR(50) COMMENT '图表类型：bar,line,pie,map等',
    data_source VARCHAR(100) COMMENT '数据源标识',
    title_template VARCHAR(200) COMMENT '标题模板',
    config_template JSON COMMENT '配置模板',
    default_options JSON COMMENT '默认选项',
    refresh_interval INT DEFAULT 86400 COMMENT '刷新间隔(秒)',
    cache_duration INT DEFAULT 3600 COMMENT '缓存时长(秒)',
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_widget_code (widget_code),
    INDEX idx_widget_type (widget_type),
    INDEX idx_data_source (data_source)
);

-- 5. 数据源配置表
DROP TABLE IF EXISTS data_sources;
CREATE TABLE data_sources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_code VARCHAR(50) NOT NULL UNIQUE COMMENT '数据源代码',
    source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    source_type VARCHAR(50) NOT NULL COMMENT '数据源类型：database,api,file',
    connection_config JSON COMMENT '连接配置',
    query_template TEXT COMMENT 'SQL查询模板',
    data_mapping JSON COMMENT '数据字段映射',
    update_frequency VARCHAR(50) COMMENT '更新频率：realtime,daily,weekly',
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_source_code (source_code),
    INDEX idx_source_type (source_type)
);

-- ================================================================
-- 业务数据表
-- ================================================================

-- 6. 农业生产统计表
DROP TABLE IF EXISTS agricultural_statistics;
CREATE TABLE agricultural_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    stat_year INT NOT NULL COMMENT '统计年份',
    stat_quarter TINYINT COMMENT '统计季度',
    stat_month TINYINT COMMENT '统计月份',
    planting_area DECIMAL(10,2) COMMENT '种植面积(万亩)',
    harvest_area DECIMAL(10,2) COMMENT '收获面积(万亩)',
    production_volume DECIMAL(10,2) COMMENT '产量(万吨)',
    production_value DECIMAL(12,2) COMMENT '产值(万元)',
    unit_price DECIMAL(8,2) COMMENT '单价(元)',
    yield_per_hectare DECIMAL(8,2) COMMENT '单产(吨/公顷)',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_year (region_code, product_code, stat_year),
    INDEX idx_year_quarter (stat_year, stat_quarter),
    UNIQUE KEY uk_region_product_period (region_code, product_code, stat_year, stat_quarter, stat_month)
);

-- 7. 价格监测表
DROP TABLE IF EXISTS price_monitoring;
CREATE TABLE price_monitoring (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    variety_name VARCHAR(100) COMMENT '品种名称',
    market_type VARCHAR(50) COMMENT '市场类型：wholesale,retail,origin',
    price_date DATE NOT NULL COMMENT '价格日期',
    current_price DECIMAL(8,2) NOT NULL COMMENT '当前价格',
    previous_price DECIMAL(8,2) COMMENT '前一日价格',
    price_change DECIMAL(8,2) COMMENT '价格变化',
    change_rate DECIMAL(5,2) COMMENT '变化率(%)',
    unit VARCHAR(20) COMMENT '计价单位',
    market_name VARCHAR(100) COMMENT '市场名称',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_region_product_date (region_code, product_code, price_date),
    INDEX idx_price_date (price_date),
    INDEX idx_market_type (market_type),
    UNIQUE KEY uk_price_record (region_code, product_code, variety_name, market_type, price_date)
);

-- 8. 合作社统计表
DROP TABLE IF EXISTS cooperative_statistics;
CREATE TABLE cooperative_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    cooperative_name VARCHAR(200) COMMENT '合作社名称',
    cooperative_type VARCHAR(50) COMMENT '合作社类型',
    registration_code VARCHAR(100) COMMENT '统一社会信用代码',
    member_count INT COMMENT '成员数量',
    land_area DECIMAL(10,2) COMMENT '土地面积(万亩)',
    annual_output DECIMAL(10,2) COMMENT '年产量(万吨)',
    annual_revenue DECIMAL(12,2) COMMENT '年收入(万元)',
    stat_year INT NOT NULL COMMENT '统计年份',
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_year (region_code, product_code, stat_year),
    INDEX idx_cooperative_name (cooperative_name)
);

-- 9. 品种分布表
DROP TABLE IF EXISTS variety_distribution;
CREATE TABLE variety_distribution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    variety_name VARCHAR(100) NOT NULL COMMENT '品种名称',
    variety_code VARCHAR(50) COMMENT '品种代码',
    planting_area DECIMAL(10,2) COMMENT '种植面积(万亩)',
    production_volume DECIMAL(10,2) COMMENT '产量(万吨)',
    area_ratio DECIMAL(5,2) COMMENT '面积占比(%)',
    production_ratio DECIMAL(5,2) COMMENT '产量占比(%)',
    stat_year INT NOT NULL COMMENT '统计年份',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_year (region_code, product_code, stat_year),
    INDEX idx_variety (variety_code),
    UNIQUE KEY uk_region_product_variety_year (region_code, product_code, variety_code, stat_year)
);

-- ================================================================
-- 插入配置数据
-- ================================================================

-- 插入默认布局配置
INSERT INTO dashboard_layouts (layout_code, layout_name, region_codes, product_codes, layout_config, is_default) VALUES
('default_agricultural', '农业产业默认布局', '["haikou","sanya","qionghai","wenchang","wanning","dongfang","ding_an","tunchang","chengmai","lingao","danzhou","wuzhishan","baisha","changjiang","ledong","lingshui","baoting","qiongzhong"]', 
'["litchi","coconut","mango","banana","rubber","pepper"]', 
'{
  "left_1": {"widget_code": "area_trend_chart", "position": "left_1"},
  "left_2": {"widget_code": "production_trend_chart", "position": "left_2"},
  "left_3": {"widget_code": "variety_distribution", "position": "left_3"},
  "center_map": {"widget_code": "production_map", "position": "center_map"},
  "right_1": {"widget_code": "sales_trend_chart", "position": "right_1"},
  "right_2": {"widget_code": "price_trend_chart", "position": "right_2"},
  "right_3": {"widget_code": "cooperative_stats", "position": "right_3"},
  "bottom_scroll": {"widget_code": "price_monitoring_list", "position": "bottom_scroll"}
}', 1);

-- 插入组件配置
INSERT INTO dashboard_widgets (widget_code, widget_name, widget_type, chart_type, data_source, title_template, default_options) VALUES
-- 面积趋势图
('area_trend_chart', '种植面积趋势图', 'Chart', 'bar', 'agricultural_area_trend', '近年#{cityName}#{productName}面积情况', 
'{
  "tooltip": {"trigger": "axis", "axisPointer": {"type": "shadow"}},
  "legend": {"show": true, "bottom": "5%", "textStyle": {"color": "#ffffff"}},
  "grid": {"left": "3%", "right": "4%", "bottom": "15%", "containLabel": true},
  "xAxis": {"type": "category", "axisLabel": {"color": "#ffffff"}},
  "yAxis": {"type": "value", "name": "万亩", "nameTextStyle": {"color": "#ffffff"}, "axisLabel": {"color": "#ffffff"}, "splitLine": {"lineStyle": {"color": "rgba(255,255,255,0.1)"}}},
  "series": [
    {"name": "种植面积", "type": "bar", "barWidth": 20},
    {"name": "收获面积", "type": "bar", "barWidth": 20}
  ]
}'),

-- 产量趋势图
('production_trend_chart', '产量趋势图', 'Chart', 'line', 'agricultural_production_trend', '近年#{cityName}#{productName}产量情况', 
'{
  "tooltip": {"trigger": "axis"},
  "legend": {"show": true, "bottom": "5%", "textStyle": {"color": "#ffffff"}},
  "grid": {"left": "3%", "right": "4%", "bottom": "15%", "containLabel": true},
  "xAxis": {"type": "category", "boundaryGap": false, "axisLabel": {"color": "#ffffff"}},
  "yAxis": {"type": "value", "name": "万吨", "nameTextStyle": {"color": "#ffffff"}, "axisLabel": {"color": "#ffffff"}, "splitLine": {"lineStyle": {"color": "rgba(255,255,255,0.1)"}}},
  "series": [{"type": "line", "smooth": true, "areaStyle": {}}]
}'),

-- 品种分布
('variety_distribution', '品种分布统计', 'TableAndChart', 'pie', 'variety_distribution_data', '#{cityName}#{productName}主要品种生产情况', 
'{
  "tooltip": {"trigger": "item", "formatter": "{b} : {c}万吨 ({d}%)"},
  "legend": {"orient": "vertical", "left": "left", "textStyle": {"color": "#ffffff"}},
  "series": [{
    "name": "产量占比",
    "type": "pie",
    "radius": "60%",
    "center": ["60%", "50%"],
    "emphasis": {"itemStyle": {"shadowBlur": 10, "shadowOffsetX": 0, "shadowColor": "rgba(0, 0, 0, 0.5)"}}
  }]
}'),

-- 生产分布地图
('production_map', '生产分布地图', 'Map', 'map', 'production_distribution_map', '#{year}年#{cityName}各区#{productName}生产情况', 
'{
  "visualMap": {
    "min": 0,
    "max": 100,
    "left": "left",
    "top": "bottom",
    "text": ["高", "低"],
    "calculable": true,
    "inRange": {"color": ["#dff6ff", "#47b5ff", "#1363df"]},
    "textStyle": {"color": "#ffffff"}
  },
  "series": [{
    "name": "生产分布",
    "type": "map",
    "mapType": "hainan",
    "roam": false,
    "label": {"show": true, "color": "#ffffff"},
    "itemStyle": {"borderColor": "#ffffff", "borderWidth": 1}
  }]
}'),

-- 价格趋势图
('price_trend_chart', '价格趋势图', 'Chart', 'line', 'price_trend_data', '近期#{cityName}#{productName}价格情况', 
'{
  "tooltip": {"trigger": "axis"},
  "grid": {"left": "3%", "right": "10%", "bottom": "3%", "containLabel": true},
  "xAxis": {"type": "category", "axisLabel": {"color": "#ffffff"}},
  "yAxis": {"type": "value", "name": "元/斤", "nameTextStyle": {"color": "#ffffff"}, "axisLabel": {"color": "#ffffff"}, "splitLine": {"lineStyle": {"color": "rgba(255,255,255,0.1)"}}},
  "series": [{"type": "line", "smooth": true}]
}'),

-- 价格监测列表
('price_monitoring_list', '价格监测列表', 'ScrollingTable', 'table', 'price_monitoring_list_data', '#{cityName}#{productName}产地价格动态监测', 
'{
  "scroll_speed": 50,
  "row_height": 40,
  "headers": ["日期", "产品/品种", "所在产地", "价格（元/斤）", "前日价格（元/斤）", "价格涨幅（%）"]
}');

-- 插入数据源配置
INSERT INTO data_sources (source_code, source_name, source_type, query_template, data_mapping) VALUES
-- 农业面积趋势数据
('agricultural_area_trend', '农业面积趋势数据', 'database', 
'SELECT stat_year as year, planting_area, harvest_area 
 FROM agricultural_statistics 
 WHERE region_code = #{cityCode} AND product_code = #{productCode} AND stat_quarter IS NULL
 ORDER BY stat_year DESC LIMIT 5', 
'{"categories": "year", "planting_area": "planting_area", "harvest_area": "harvest_area"}'),

-- 农业产量趋势数据
('agricultural_production_trend', '农业产量趋势数据', 'database',
'SELECT stat_year as year, production_volume
 FROM agricultural_statistics 
 WHERE region_code = #{cityCode} AND product_code = #{productCode} AND stat_quarter IS NULL
 ORDER BY stat_year DESC LIMIT 5',
'{"categories": "year", "production_volume": "production_volume"}'),

-- 品种分布数据
('variety_distribution_data', '品种分布数据', 'database',
'SELECT variety_name, planting_area, production_volume, production_ratio
 FROM variety_distribution 
 WHERE region_code = #{cityCode} AND product_code = #{productCode} AND stat_year = #{currentYear}
 ORDER BY production_volume DESC',
'{"variety_name": "variety_name", "planting_area": "planting_area", "production_volume": "production_volume", "production_ratio": "production_ratio"}'),

-- 价格趋势数据
('price_trend_data', '价格趋势数据', 'database',
'SELECT DATE_FORMAT(price_date, "%m-%d") as date, current_price as price
 FROM price_monitoring 
 WHERE region_code = #{cityCode} AND product_code = #{productCode} AND market_type = "origin"
 ORDER BY price_date DESC LIMIT 15',
'{"categories": "date", "price": "price"}'),

-- 价格监测列表数据
('price_monitoring_list_data', '价格监测列表数据', 'database',
'SELECT price_date as date, CONCAT(p.product_name, "/", pm.variety_name) as product_variety,
       r.region_name as region, pm.current_price, pm.previous_price, pm.change_rate
 FROM price_monitoring pm
 LEFT JOIN sys_products p ON pm.product_code = p.product_code
 LEFT JOIN sys_regions r ON pm.region_code = r.region_code
 WHERE pm.region_code = #{cityCode} AND pm.product_code = #{productCode}
 ORDER BY pm.price_date DESC LIMIT 50',
'{"date": "date", "product_variety": "product_variety", "region": "region", "current_price": "current_price", "previous_price": "previous_price", "change_rate": "change_rate"}');

-- ================================================================
-- 插入测试数据
-- ================================================================

-- 插入海口荔枝测试数据
INSERT INTO agricultural_statistics (region_code, product_code, stat_year, planting_area, harvest_area, production_volume, production_value, unit_price) VALUES
('haikou', 'litchi', 2020, 9.32, 6.95, 4.30, 35000, 8.14),
('haikou', 'litchi', 2021, 10.99, 9.24, 6.91, 48000, 6.95),
('haikou', 'litchi', 2022, 11.61, 9.68, 7.89, 55000, 6.97),
('haikou', 'litchi', 2023, 12.53, 10.93, 7.58, 58000, 7.65),
('haikou', 'litchi', 2024, 13.42, 11.45, 9.50, 76000, 8.00);

-- 插入品种分布数据
INSERT INTO variety_distribution (region_code, product_code, variety_name, variety_code, planting_area, production_volume, area_ratio, production_ratio, stat_year) VALUES
('haikou', 'litchi', '白糖罂', 'white_sugar', 0.6, 0.3, 4.47, 3.16, 2024),
('haikou', 'litchi', '荔枝王', 'litchi_king', 3.5, 1.2, 26.08, 12.63, 2024),
('haikou', 'litchi', '妃子笑', 'princess_smile', 9.2, 4.60, 68.56, 48.42, 2024);

-- 插入价格监测数据
INSERT INTO price_monitoring (region_code, product_code, variety_name, market_type, price_date, current_price, previous_price, price_change, change_rate, unit) VALUES
('haikou', 'litchi', '妃子笑', 'origin', '2024-05-19', 16.48, NULL, NULL, NULL, '元/斤'),
('haikou', 'litchi', '妃子笑', 'origin', '2024-05-20', 16.33, 16.48, -0.15, -0.91, '元/斤'),
('haikou', 'litchi', '妃子笑', 'origin', '2024-05-21', 15.89, 16.33, -0.44, -2.69, '元/斤'),
('haikou', 'litchi', '白糖罂', 'origin', '2024-05-19', 19.86, NULL, NULL, NULL, '元/斤'),
('haikou', 'litchi', '白糖罂', 'origin', '2024-05-20', 18.91, 19.86, -0.95, -4.78, '元/斤'),
('haikou', 'litchi', '荔枝王', 'origin', '2024-05-19', 9.81, NULL, NULL, NULL, '元/斤'),
('haikou', 'litchi', '荔枝王', 'origin', '2024-05-20', 9.81, 9.81, 0.00, 0.00, '元/斤');

-- 插入合作社数据
INSERT INTO cooperative_statistics (region_code, product_code, cooperative_name, cooperative_type, member_count, land_area, annual_output, annual_revenue, stat_year) VALUES
('haikou', 'litchi', '海口火山荔枝专业合作社', 'professional', 156, 2.8, 1.85, 1480, 2024),
('haikou', 'litchi', '琼山区荔枝种植合作社', 'planting', 89, 1.5, 1.12, 896, 2024),
('haikou', 'litchi', '秀英荔枝产销合作社', 'marketing', 234, 3.2, 2.45, 1960, 2024);

COMMIT;

-- ================================================================
-- 创建索引优化
-- ================================================================

-- 创建复合索引用于查询优化
CREATE INDEX idx_agri_stats_composite ON agricultural_statistics(region_code, product_code, stat_year DESC);
CREATE INDEX idx_price_monitoring_composite ON price_monitoring(region_code, product_code, price_date DESC);
CREATE INDEX idx_variety_distribution_composite ON variety_distribution(region_code, product_code, stat_year DESC);

-- ================================================================
-- 数据库初始化完成
-- ================================================================
SELECT 'Database initialization completed successfully!' as message;