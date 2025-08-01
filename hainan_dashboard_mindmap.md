# 海南18个市县一张图系统设计思维导图

```
海南18个市县一张图数据支持系统
├── 🎯 核心需求分析
│   ├── 动态内容要求
│   │   ├── 标题动态（如：近年海口荔枝面积情况）
│   │   ├── 图表类型动态（柱状图、饼状图、折线图等）
│   │   ├── 数据完全从后台获取
│   │   └── 字段名称动态（年份、种植面积、收获面积等）
│   ├── 布局固定结构
│   │   ├── 左一区域（Left 1）
│   │   ├── 左二区域（Left 2） 
│   │   ├── 左三区域（Left 3）
│   │   ├── 中间地图区域（Center Map - 2/3）
│   │   ├── 中下区域（Center Bottom - 1/3）
│   │   ├── 右一区域（Right 1）
│   │   ├── 右二区域（Right 2）
│   │   └── 右三区域（Right 3）
│   └── 地图交互需求
│       ├── 点击切换数据类型（面积、产量、合作社）
│       ├── 只更新地图数据，其他区域不变
│       └── 切换按钮也需要后台配置
│
├── 🏗️ 系统架构设计
│   ├── 前后端分离架构
│   │   ├── 前端：Vue3 + TypeScript + ECharts
│   │   ├── 后端：Spring Boot + MySQL + Redis
│   │   └── 通信：RESTful API + JSON
│   ├── 元数据驱动理念
│   │   ├── 前端作为渲染引擎
│   │   ├── 所有配置存储在数据库
│   │   ├── 后端提供配置化JSON
│   │   └── 动态组装页面内容
│   └── 微服务设计思路
│       ├── 布局配置服务
│       ├── 组件数据服务
│       ├── 地图数据服务
│       └── 系统基础服务
│
├── 📊 数据库设计
│   ├── 系统基础表
│   │   ├── sys_regions（18个市县基础信息）
│   │   │   ├── region_code（区域代码）
│   │   │   ├── region_name（区域名称）
│   │   │   ├── geo_data（地理边界数据）
│   │   │   └── center_point（中心点坐标）
│   │   └── sys_products（产品分类表）
│   │       ├── product_code（产品代码：litchi、coconut等）
│   │       ├── product_name（产品名称）
│   │       ├── category_name（分类名称）
│   │       └── unit（计量单位）
│   ├── 布局配置表
│   │   ├── dashboard_layout_positions（布局位置定义）
│   │   │   ├── position_code（left_1, left_2...right_3）
│   │   │   ├── position_name（左一区域、左二区域...）
│   │   │   ├── position_type（widget、map、scroll）
│   │   │   └── grid_area（CSS Grid区域定义）
│   │   └── dashboard_position_widgets（位置组件配置）
│   │       ├── region_code + product_code（区域产品组合）
│   │       ├── position_code（位置代码）
│   │       ├── widget_title（组件标题）
│   │       ├── widget_type + chart_type（组件和图表类型）
│   │       ├── data_source_code（数据源代码）
│   │       └── chart_options（ECharts配置JSON）
│   ├── 地图专用表
│   │   ├── map_layer_config（地图图层配置）
│   │   │   ├── layer_code（area、production、cooperative）
│   │   │   ├── layer_name（面积、产量、合作社）
│   │   │   ├── layer_icon（图层图标）
│   │   │   ├── color_scheme（颜色方案）
│   │   │   └── is_default（是否默认图层）
│   │   └── map_data（地图数据表）
│   │       ├── region_code + sub_region_code（父子区域）
│   │       ├── product_code + layer_code（产品图层）
│   │       ├── data_year（数据年份）
│   │       ├── value_amount（数值）
│   │       └── percentage + rank_order（占比排名）
│   ├── 业务数据表
│   │   ├── chart_data（统计图表数据）
│   │   │   ├── region_code + product_code + chart_code
│   │   │   ├── data_date（数据日期）
│   │   │   ├── dimension_1/2/3（维度字段：年份、品种、区域）
│   │   │   ├── metric_value_1/2/3（指标值）
│   │   │   └── extra_data（额外数据JSON）
│   │   └── scroll_list_data（滚动列表数据）
│   │       ├── region_code + product_code + list_code
│   │       ├── title + content（标题内容）
│   │       ├── priority（优先级）
│   │       └── tags（标签数组）
│   └── 数据源配置表
│       └── data_sources（数据源配置）
│           ├── source_code（数据源代码）
│           ├── source_type（sql、api、static）
│           ├── query_template（SQL查询模板）
│           ├── data_mapping（数据字段映射JSON）
│           └── cache_duration（缓存时长）
│
├── 🔗 API接口设计
│   ├── 布局配置接口
│   │   ├── GET /api/v1/dashboard/layout
│   │   │   ├── 输入：regionCode + productCode
│   │   │   └── 输出：完整布局配置 + 组件API地址
│   │   └── 返回8个位置的组件配置信息
│   ├── 组件数据接口
│   │   ├── GET /api/v1/dashboard/widget/{position}
│   │   │   ├── 路径参数：position（left_1...right_3）
│   │   │   ├── 查询参数：regionCode + productCode
│   │   │   └── 返回：组件信息 + ECharts配置 + 数据
│   │   └── POST /api/v1/dashboard/batch-widgets
│   │       ├── 批量获取多个位置数据
│   │       └── 提高页面加载性能
│   ├── 地图专用接口
│   │   ├── GET /api/v1/dashboard/map/layers
│   │   │   ├── 获取可切换的图层列表
│   │   │   └── 返回：面积、产量、合作社等按钮配置
│   │   └── GET /api/v1/dashboard/map/data/{layerCode}
│   │       ├── 地图数据切换专用接口
│   │       ├── 只更新地图数据，其他组件不变
│   │       └── 返回：ECharts地图配置 + 数据 + 统计信息
│   └── 系统基础接口
│       ├── GET /api/v1/system/regions（18个市县列表）
│       └── GET /api/v1/system/products（产品分类列表）
│
├── 🎨 前端架构设计
│   ├── 技术栈选择
│   │   ├── Vue 3 + Composition API
│   │   ├── TypeScript（类型安全）
│   │   ├── Element Plus（UI组件库）
│   │   ├── ECharts 5（图表库）
│   │   ├── Pinia（状态管理）
│   │   └── Vite（构建工具）
│   ├── 组件设计
│   │   ├── DashboardLayout（主布局组件）
│   │   │   ├── CSS Grid 8宫格布局
│   │   │   └── 响应式适配
│   │   ├── WidgetContainer（组件容器）
│   │   │   ├── 动态组件加载
│   │   │   ├── 错误边界处理
│   │   │   └── 加载状态管理
│   │   ├── ChartWidget（图表组件）
│   │   │   ├── ECharts封装
│   │   │   ├── 支持多种图表类型
│   │   │   └── 主题和样式配置
│   │   ├── MapWidget（地图组件）
│   │   │   ├── 地图渲染
│   │   │   ├── 图层切换逻辑
│   │   │   └── 交互事件处理
│   │   └── ScrollListWidget（滚动列表）
│   │       ├── 虚拟滚动优化
│   │       └── 实时数据更新
│   ├── 状态管理
│   │   ├── dashboardStore（仪表板状态）
│   │   │   ├── 当前区域和产品
│   │   │   ├── 布局配置缓存
│   │   │   └── 组件数据缓存
│   │   └── mapStore（地图状态）
│   │       ├── 当前图层
│   │       ├── 图层数据缓存
│   │       └── 地图交互状态
│   └── 数据流设计
│       ├── 页面初始化流程
│       │   ├── 1. 获取布局配置
│       │   ├── 2. 批量加载组件数据
│       │   ├── 3. 获取地图图层配置
│       │   └── 4. 加载默认地图数据
│       ├── 地图切换流程
│       │   ├── 1. 用户点击图层按钮
│       │   ├── 2. 调用地图数据接口
│       │   ├── 3. 更新地图组件
│       │   └── 4. 其他组件保持不变
│       └── 数据更新流程
│           ├── 定时刷新策略
│           ├── 错误重试机制
│           └── 缓存失效处理
│
├── ⚙️ 后端架构设计
│   ├── 技术栈选择
│   │   ├── Spring Boot 3.2（主框架）
│   │   ├── MySQL 8.0（主数据库）
│   │   ├── Redis（缓存层）
│   │   ├── MyBatis Plus（ORM框架）
│   │   └── Druid（数据库连接池）
│   ├── 分层架构
│   │   ├── Controller层（API接口）
│   │   │   ├── DashboardController（仪表板接口）
│   │   │   ├── MapController（地图接口）
│   │   │   └── SystemController（系统接口）
│   │   ├── Service层（业务逻辑）
│   │   │   ├── DashboardService（布局和组件服务）
│   │   │   ├── MapLayerService（地图图层服务）
│   │   │   ├── DataSourceService（数据源服务）
│   │   │   └── CacheService（缓存服务）
│   │   ├── Repository层（数据访问）
│   │   │   ├── 配置数据Mapper
│   │   │   ├── 业务数据Mapper
│   │   │   └── 系统数据Mapper
│   │   └── Entity层（数据模型）
│   │       ├── 系统实体
│   │       ├── 配置实体
│   │       └── 业务实体
│   ├── 核心服务设计
│   │   ├── ConfigurationService（配置服务）
│   │   │   ├── 布局配置解析
│   │   │   ├── 组件配置组装
│   │   │   └── 数据源配置管理
│   │   ├── DataProcessingService（数据处理服务）
│   │   │   ├── SQL查询执行
│   │   │   ├── 数据格式转换
│   │   │   ├── ECharts配置生成
│   │   │   └── 缓存策略应用
│   │   └── MapDataService（地图数据服务）
│   │       ├── 图层数据查询
│   │       ├── 地理数据处理
│   │       ├── 统计计算（排名、占比）
│   │       └── 地图配置生成
│   └── 性能优化
│       ├── 缓存策略
│       │   ├── 布局配置缓存（30分钟）
│       │   ├── 组件数据缓存（10分钟）
│       │   └── 地图数据缓存（30分钟）
│       ├── 数据库优化
│       │   ├── 索引设计
│       │   ├── 查询优化
│       │   └── 连接池配置
│       └── API优化
│           ├── 批量接口设计
│           ├── 分页查询
│           └── 响应压缩
│
├── 🔄 数据流转设计
│   ├── 配置数据流
│   │   ├── 1. 管理员在后台配置组件
│   │   ├── 2. 配置存储到数据库
│   │   ├── 3. 前端请求时组装JSON
│   │   └── 4. 缓存提高响应速度
│   ├── 业务数据流
│   │   ├── 1. 业务系统产生数据
│   │   ├── 2. 数据导入到chart_data、map_data表
│   │   ├── 3. API根据配置查询数据
│   │   └── 4. 实时计算统计指标
│   └── 地图切换数据流
│       ├── 1. 用户点击图层按钮
│       ├── 2. 前端调用map/data/{layerCode}接口
│       ├── 3. 后端查询对应图层数据
│       ├── 4. 返回ECharts地图配置
│       └── 5. 前端只更新地图组件
│
├── 🚀 部署架构设计
│   ├── 容器化部署
│   │   ├── Docker容器
│   │   │   ├── 前端容器（Nginx + Vue构建产物）
│   │   │   ├── 后端容器（Spring Boot应用）
│   │   │   ├── 数据库容器（MySQL 8.0）
│   │   │   └── 缓存容器（Redis）
│   │   └── Docker Compose编排
│   │       ├── 服务依赖管理
│   │       ├── 网络配置
│   │       ├── 数据卷挂载
│   │       └── 环境变量配置
│   ├── 负载均衡
│   │   ├── Nginx反向代理
│   │   ├── 静态资源分离
│   │   └── API请求转发
│   └── 监控运维
│       ├── 应用监控（Spring Boot Actuator）
│       ├── 系统监控（Prometheus + Grafana）
│       ├── 日志收集（ELK Stack）
│       └── 数据备份策略
│
└── 📋 实施计划
    ├── 第一阶段：基础架构搭建
    │   ├── 数据库设计和初始化
    │   ├── 后端项目框架搭建
    │   ├── 前端项目框架搭建
    │   └── 基础API接口开发
    ├── 第二阶段：核心功能开发
    │   ├── 布局配置功能
    │   ├── 组件渲染引擎
    │   ├── 地图组件开发
    │   └── 数据源配置系统
    ├── 第三阶段：业务数据集成
    │   ├── 海口荔枝数据录入
    │   ├── 其他市县数据扩展
    │   ├── 数据导入工具开发
    │   └── 性能优化调试
    ├── 第四阶段：系统完善
    │   ├── 管理后台开发
    │   ├── 用户权限管理
    │   ├── 数据监控告警
    │   └── 部署文档编写
    └── 第五阶段：测试上线
        ├── 单元测试和集成测试
        ├── 性能测试和压力测试
        ├── 用户验收测试
        └── 生产环境部署
```

## 🎯 设计核心思想

### 1. 元数据驱动架构
- **前端**：纯渲染引擎，根据后端配置动态生成页面
- **后端**：配置管理中心，所有显示逻辑都存储在数据库
- **优势**：无需修改代码即可调整页面布局和内容

### 2. 布局位置化设计
- **固定8个位置**：完全按照您要求的布局设计
- **位置-组件映射**：每个位置可配置不同的组件类型
- **灵活配置**：支持18个市县 × 多种产品的任意组合

### 3. 地图数据独立切换
- **专用接口**：地图切换有独立的API
- **局部更新**：只更新地图数据，其他组件保持不变
- **性能优化**：避免全页面重新渲染

### 4. 配置化优先
- **图表类型**：柱状图、饼状图、折线图等完全可配置
- **数据源**：SQL模板化，支持参数替换
- **样式主题**：ECharts配置存储在数据库中

这个思维导图涵盖了整个系统的设计思路，您觉得哪个部分需要进一步细化？