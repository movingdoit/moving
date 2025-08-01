# 海南18个市县一张图技术方案

## 项目概述

海南18个市县一张图是一个基于**元数据驱动**架构的动态数据可视化平台，支持海南省18个市县的农业产业数据展示。采用前后端分离架构，通过配置化的方式实现页面布局、图表类型、数据源的动态管理，无需修改代码即可适配不同市县和产品的数据展示需求。

## 核心特性

- 🚀 **元数据驱动**：所有页面布局、图表配置通过数据库配置动态生成
- 🎯 **高度可配置**：支持18个市县、多种农产品的数据展示切换
- 📊 **丰富图表**：基于ECharts，支持柱状图、折线图、饼图、地图等多种图表类型
- 🗺️ **地图可视化**：集成海南地图，支持多维度数据展示和图层切换
- ⚡ **高性能**：Redis缓存、数据分层、异步处理保障系统性能
- 🔧 **易维护**：标准化的API接口、组件化的前端架构
- 📱 **响应式设计**：适配不同屏幕尺寸的显示效果

## 技术架构

### 前端技术栈
- **框架**：Vue 3 + TypeScript
- **UI组件**：Element Plus
- **图表库**：Apache ECharts
- **状态管理**：Pinia
- **路由管理**：Vue Router 4
- **HTTP客户端**：Axios
- **构建工具**：Vite

### 后端技术栈
- **框架**：Spring Boot 3.2.1
- **数据库**：MySQL 8.0
- **缓存**：Redis + Redisson
- **ORM**：MyBatis Plus
- **连接池**：Druid
- **API文档**：Knife4j (Swagger3)
- **工具库**：HuTool

### 系统架构图

```
┌─────────────────────────────────────────┐
│           前端表现层 (Vue3/TS)           │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │图表组件 │ │表格组件 │ │地图组件 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
                    ▼ HTTP/HTTPS
┌─────────────────────────────────────────┐
│          后端服务层 (Spring Boot)         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │配置API  │ │数据API  │ │管理后台 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
                    ▼ JDBC/Redis
┌─────────────────────────────────────────┐
│            数据源层 (MySQL)              │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │业务数据 │ │配置数据 │ │系统数据 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
```

## 项目结构

```
hainan-dashboard/
├── README.md                              # 项目说明文档
├── hainan_dashboard_design.md             # 技术设计文档
├── database_init.sql                      # 数据库初始化脚本
├── frontend/                              # 前端项目
│   ├── src/
│   │   ├── components/                    # 通用组件
│   │   ├── views/                         # 页面组件
│   │   ├── api/                          # API接口
│   │   ├── types/                        # TypeScript类型定义
│   │   ├── utils/                        # 工具函数
│   │   └── styles/                       # 样式文件
│   ├── package.json                      # 前端依赖配置
│   └── vite.config.ts                    # Vite配置
└── backend/                              # 后端项目
    ├── src/main/java/com/hainan/dashboard/
    │   ├── controller/                   # 控制器层
    │   ├── service/                      # 服务层
    │   ├── entity/                       # 实体类
    │   ├── dto/                          # 数据传输对象
    │   ├── mapper/                       # 数据访问层
    │   ├── config/                       # 配置类
    │   └── common/                       # 公共工具
    ├── src/main/resources/
    │   ├── application.yml               # 应用配置
    │   └── mapper/                       # MyBatis映射文件
    └── pom.xml                           # Maven配置
```

## 快速开始

### 环境要求

- **Java**: JDK 17+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Maven**: 3.6+

### 1. 数据库初始化

```bash
# 执行数据库初始化脚本
mysql -u root -p < database_init.sql
```

### 2. 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务启动后可访问：
- API文档：http://localhost:8080/doc.html
- 数据库监控：http://localhost:8080/druid
- 健康检查：http://localhost:8080/actuator/health

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端应用启动后访问：http://localhost:3000

## API接口文档

### 核心接口

#### 1. 获取仪表板配置
```http
GET /api/v1/dashboard/config?cityCode=haikou&productCode=litchi
```

#### 2. 获取组件数据
```http
GET /api/v1/dashboard/data/{widgetId}?cityCode=haikou&productCode=litchi
```

#### 3. 获取市县列表
```http
GET /api/v1/system/regions
```

#### 4. 获取产品列表
```http
GET /api/v1/system/products
```

详细API文档请访问：http://localhost:8080/doc.html

## 数据库设计

### 核心数据表

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| sys_regions | 行政区划表 | region_code, region_name, parent_code |
| sys_products | 产品分类表 | product_code, product_name, category_code |
| dashboard_layouts | 页面布局配置表 | layout_code, layout_config, region_codes |
| dashboard_widgets | 组件配置表 | widget_code, widget_type, chart_type |
| data_sources | 数据源配置表 | source_code, query_template, data_mapping |
| agricultural_statistics | 农业生产统计表 | region_code, product_code, stat_year |
| price_monitoring | 价格监测表 | region_code, product_code, price_date |

## 开发指南

### 前端开发

#### 1. 添加新的图表组件
```typescript
// 在 src/components/ 下创建新组件
// 参考 ChartComponent.vue 的实现方式
```

#### 2. 添加新的数据类型
```typescript
// 在 src/types/dashboard.ts 中定义新的接口
export interface NewWidgetConfig extends BaseWidgetConfig {
  type: 'NewWidget'
  // 添加特定配置
}
```

### 后端开发

#### 1. 添加新的数据源
```sql
-- 在 data_sources 表中插入新的数据源配置
INSERT INTO data_sources (source_code, source_name, query_template, data_mapping) VALUES
('new_data_source', '新数据源', 'SELECT * FROM new_table WHERE region_code = #{cityCode}', '{"field1": "field1"}');
```

#### 2. 添加新的组件类型
```java
// 在 DashboardService 中添加新的组件处理逻辑
private JSONObject buildNewWidgetOptions(DashboardWidget widget, String cityCode, String productCode) {
    // 实现新组件的配置构建逻辑
}
```

## 部署指南

### Docker部署（推荐）

#### 1. 构建镜像
```bash
# 后端镜像
cd backend
docker build -t hainan-dashboard-backend .

# 前端镜像
cd frontend
docker build -t hainan-dashboard-frontend .
```

#### 2. 使用docker-compose启动
```bash
docker-compose up -d
```

### 传统部署

#### 1. 后端部署
```bash
# 打包
mvn clean package -P prod

# 启动
java -jar target/dashboard-backend-1.0.0.jar --spring.profiles.active=prod
```

#### 2. 前端部署
```bash
# 构建
npm run build

# 部署到Nginx
cp -r dist/* /var/www/html/
```

#### 3. Nginx配置
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /var/www/html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 系统配置

### 缓存策略
- **仪表板配置缓存**：30分钟
- **组件数据缓存**：10分钟
- **基础数据缓存**：1小时

### 性能优化
- 数据库连接池：最大20个连接
- Redis连接池：最大200个连接
- API限流：每分钟100次请求

## 常见问题

### Q1: 如何添加新的市县？
A: 在 `sys_regions` 表中插入新的区域记录，并更新相关的布局配置。

### Q2: 如何修改图表样式？
A: 在管理后台修改组件的 `default_options` 配置，或直接更新数据库中的配置。

### Q3: 如何添加新的数据源？
A: 在 `data_sources` 表中添加新的数据源配置，包括查询模板和字段映射。

### Q4: 数据更新频率如何控制？
A: 通过 `dashboard_widgets` 表的 `refresh_interval` 字段控制各组件的数据刷新间隔。

## 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 技术支持

如有问题，请通过以下方式联系：
- 提交 Issue：[GitHub Issues](https://github.com/your-org/hainan-dashboard/issues)
- 邮箱：support@hainan-dashboard.com
- 文档：[在线文档](https://docs.hainan-dashboard.com)

---

**海南18个市县一张图** - 让数据更有价值 🌴