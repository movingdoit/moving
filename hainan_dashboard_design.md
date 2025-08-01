# 海南18个市县一张图动态数据支持技术方案

## 一、总体设计思路

### 1.1 核心理念：元数据驱动架构
采用**元数据驱动（Metadata-Driven）**的设计思想，实现前后端彻底解耦：
- 前端作为"渲染引擎"，根据后端返回的配置动态构建页面
- 后端作为"配置中心"，管理所有页面布局、图表类型、数据源等元信息
- 所有展示内容（标题、图表、数据）都通过配置动态生成，无需修改代码

### 1.2 技术架构分层
```
┌─────────────────────────────────────────┐
│           前端表现层 (Vue3/React)         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │图表组件 │ │表格组件 │ │地图组件 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
┌─────────────────────────────────────────┐
│          后端服务层 (Spring Boot)         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │配置API  │ │数据API  │ │管理后台 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
┌─────────────────────────────────────────┐
│            数据源层 (MySQL)              │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │业务数据 │ │配置数据 │ │系统数据 │    │
│  └─────────┘ └─────────┘ └─────────┘    │
└─────────────────────────────────────────┘
```

## 二、API接口设计

### 2.1 核心API接口规范

#### 2.1.1 页面配置获取接口
```http
GET /api/v1/dashboard/config
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| cityCode | String | 是 | 市县代码 | haikou, sanya, qionghai |
| productCode | String | 否 | 产品代码 | litchi, coconut, mango |
| year | Integer | 否 | 年份 | 2024 |
| layoutVersion | String | 否 | 布局版本 | v1.0 |

**响应数据结构：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "page_meta": {
      "page_title": "海口市荔枝产业一张图",
      "city_name": "海口市",
      "city_code": "haikou",
      "product_name": "荔枝",
      "product_code": "litchi",
      "update_time": "2024-01-20 10:00:00",
      "layout_version": "v1.0"
    },
    "layout": {
      "left_1": {
        "widget_id": "area_trend_chart",
        "title": "近年海口荔枝面积情况",
        "type": "Chart",
        "config": {
          "chart_type": "bar",
          "data_source": "agricultural_statistics",
          "refresh_interval": 86400
        },
        "chart_options": {
          // ECharts完整配置
        }
      },
      "center_map": {
        "widget_id": "production_distribution_map",
        "title": "2024年海口各区荔枝生产分布",
        "type": "Map",
        "config": {
          "map_code": "haikou",
          "default_layer": "production",
          "switch_layers": true
        },
        "map_options": {
          // 地图配置
        }
      }
      // ... 其他布局区域
    }
  }
}
```

#### 2.1.2 实时数据获取接口
```http
GET /api/v1/dashboard/data/{widgetId}
```

**响应数据：**
```json
{
  "code": 200,
  "data": {
    "widget_id": "area_trend_chart",
    "chart_data": {
      "categories": ["2020", "2021", "2022", "2023", "2024"],
      "series": [
        {
          "name": "种植面积（万亩）",
          "data": [9.32, 10.99, 11.61, 12.53, 13.42]
        },
        {
          "name": "收获面积（万亩）",
          "data": [6.95, 9.24, 9.68, 10.93, 11.45]
        }
      ]
    },
    "last_update": "2024-01-20 09:30:00"
  }
}
```

### 2.2 管理后台API接口

#### 2.2.1 页面布局管理
```http
# 获取布局模板列表
GET /api/v1/admin/layouts

# 创建/更新布局配置
POST /api/v1/admin/layouts
PUT /api/v1/admin/layouts/{layoutId}

# 获取组件配置
GET /api/v1/admin/widgets/{widgetId}
```

#### 2.2.2 数据源管理
```http
# 数据源配置
GET /api/v1/admin/datasources
POST /api/v1/admin/datasources

# 数据映射配置
GET /api/v1/admin/data-mappings
POST /api/v1/admin/data-mappings
```

## 三、数据库设计

### 3.1 核心数据表结构

#### 3.1.1 行政区划表 (sys_regions)
```sql
CREATE TABLE sys_regions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL UNIQUE COMMENT '区划代码',
    region_name VARCHAR(100) NOT NULL COMMENT '区划名称',
    parent_code VARCHAR(20) COMMENT '父级代码',
    region_level TINYINT NOT NULL COMMENT '行政级别：1-省,2-市,3-县,4-镇',
    region_type VARCHAR(20) COMMENT '区划类型：province,city,county,town',
    geo_data JSON COMMENT '地理边界数据',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用,0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入海南18个市县数据
INSERT INTO sys_regions (region_code, region_name, parent_code, region_level, region_type) VALUES
('haikou', '海口市', 'hainan', 2, 'city'),
('sanya', '三亚市', 'hainan', 2, 'city'),
('sansha', '三沙市', 'hainan', 2, 'city'),
('danzhou', '儋州市', 'hainan', 2, 'city'),
('wuzhishan', '五指山市', 'hainan', 2, 'city'),
('qionghai', '琼海市', 'hainan', 2, 'city'),
('wenchang', '文昌市', 'hainan', 2, 'city'),
('wanning', '万宁市', 'hainan', 2, 'city'),
('dongfang', '东方市', 'hainan', 2, 'city'),
('ding_an', '定安县', 'hainan', 3, 'county'),
('tunchang', '屯昌县', 'hainan', 3, 'county'),
('chengmai', '澄迈县', 'hainan', 3, 'county'),
('lingao', '临高县', 'hainan', 3, 'county'),
('baisha', '白沙黎族自治县', 'hainan', 3, 'county'),
('changjiang', '昌江黎族自治县', 'hainan', 3, 'county'),
('ledong', '乐东黎族自治县', 'hainan', 3, 'county'),
('lingshui', '陵水黎族自治县', 'hainan', 3, 'county'),
('baoting', '保亭黎族苗族自治县', 'hainan', 3, 'county'),
('qiongzhong', '琼中黎族苗族自治县', 'hainan', 3, 'county');
```

#### 3.1.2 产品分类表 (sys_products)
```sql
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
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入产品数据
INSERT INTO sys_products (product_code, product_name, category_code, category_name, unit) VALUES
('litchi', '荔枝', 'fruit', '水果', '万亩'),
('coconut', '椰子', 'fruit', '水果', '万亩'),
('mango', '芒果', 'fruit', '水果', '万亩'),
('banana', '香蕉', 'fruit', '水果', '万亩'),
('rubber', '橡胶', 'cash_crop', '经济作物', '万亩'),
('pepper', '胡椒', 'spice', '香料', '万亩');
```

#### 3.1.3 页面布局配置表 (dashboard_layouts)
```sql
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
    INDEX idx_region_product (region_codes, product_codes)
);
```

#### 3.1.4 组件配置表 (dashboard_widgets)
```sql
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
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3.1.5 数据源配置表 (data_sources)
```sql
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
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3.2 业务数据表结构

#### 3.2.1 农业生产统计表 (agricultural_statistics)
```sql
CREATE TABLE agricultural_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    stat_year INT NOT NULL COMMENT '统计年份',
    stat_quarter TINYINT COMMENT '统计季度',
    stat_month TINYINT COMMENT '统计月份',
    planting_area DECIMAL(10,2) COMMENT '种植面积',
    harvest_area DECIMAL(10,2) COMMENT '收获面积',
    production_volume DECIMAL(10,2) COMMENT '产量',
    production_value DECIMAL(12,2) COMMENT '产值',
    unit_price DECIMAL(8,2) COMMENT '单价',
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_year (region_code, product_code, stat_year),
    INDEX idx_year_quarter (stat_year, stat_quarter)
);
```

#### 3.2.2 价格监测表 (price_monitoring)
```sql
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
    data_source VARCHAR(100) COMMENT '数据来源',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_region_product_date (region_code, product_code, price_date),
    INDEX idx_price_date (price_date)
);
```

#### 3.2.3 合作社统计表 (cooperative_statistics)
```sql
CREATE TABLE cooperative_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    region_code VARCHAR(20) NOT NULL COMMENT '区域代码',
    product_code VARCHAR(50) NOT NULL COMMENT '产品代码',
    cooperative_name VARCHAR(200) COMMENT '合作社名称',
    cooperative_type VARCHAR(50) COMMENT '合作社类型',
    member_count INT COMMENT '成员数量',
    land_area DECIMAL(10,2) COMMENT '土地面积',
    annual_output DECIMAL(10,2) COMMENT '年产量',
    stat_year INT NOT NULL COMMENT '统计年份',
    status TINYINT DEFAULT 1,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_region_product_year (region_code, product_code, stat_year)
);
```

## 四、前端组件设计

### 4.1 通用组件架构

#### 4.1.1 图表组件 (ChartComponent.vue)
```vue
<template>
  <div class="chart-container">
    <div class="chart-header" v-if="title">
      <h3>{{ title }}</h3>
      <div class="chart-tools">
        <el-button-group v-if="tools.export">
          <el-button size="small" @click="exportChart">导出</el-button>
        </el-button-group>
      </div>
    </div>
    <div 
      ref="chartRef" 
      :style="{ width: width, height: height }"
      class="chart-content"
    ></div>
    <div class="chart-footer" v-if="showFooter">
      <span class="update-time">更新时间: {{ updateTime }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  title: String,
  chartOptions: Object,
  width: { type: String, default: '100%' },
  height: { type: String, default: '400px' },
  tools: { type: Object, default: () => ({ export: true }) },
  updateTime: String,
  showFooter: { type: Boolean, default: true }
})

const chartRef = ref(null)
let chartInstance = null

onMounted(() => {
  initChart()
})

watch(() => props.chartOptions, (newOptions) => {
  if (chartInstance && newOptions) {
    chartInstance.setOption(newOptions, true)
  }
}, { deep: true })

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    if (props.chartOptions) {
      chartInstance.setOption(props.chartOptions)
    }
    
    // 响应式处理
    window.addEventListener('resize', handleResize)
  }
}

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

const exportChart = () => {
  if (chartInstance) {
    const url = chartInstance.getDataURL({
      pixelRatio: 2,
      backgroundColor: '#fff'
    })
    const link = document.createElement('a')
    link.href = url
    link.download = `${props.title || 'chart'}.png`
    link.click()
  }
}
</script>
```

#### 4.1.2 地图组件 (MapComponent.vue)
```vue
<template>
  <div class="map-container">
    <div class="map-header">
      <h3>{{ title }}</h3>
      <div class="map-controls">
        <el-radio-group v-model="currentLayer" @change="switchLayer">
          <el-radio-button 
            v-for="layer in layers" 
            :key="layer.key"
            :label="layer.key"
          >
            {{ layer.name }}
          </el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <div class="map-content">
      <div ref="mapRef" :style="{ width: '100%', height: mapHeight }"></div>
      <div class="map-legend" v-if="showLegend">
        <!-- 图例组件 -->
      </div>
    </div>
    <div class="map-summary" v-if="summaryData">
      <el-table :data="summaryData" size="small">
        <el-table-column 
          v-for="col in summaryColumns" 
          :key="col.prop"
          :prop="col.prop" 
          :label="col.label"
        />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  title: String,
  mapOptions: Object,
  mapHeight: { type: String, default: '500px' },
  showLegend: { type: Boolean, default: true }
})

const mapRef = ref(null)
const currentLayer = ref('production')
let mapInstance = null

// 地图数据层切换逻辑
const switchLayer = (layerKey) => {
  if (mapInstance && props.mapOptions?.dataLayers?.[layerKey]) {
    const layerData = props.mapOptions.dataLayers[layerKey]
    mapInstance.setOption({
      visualMap: layerData.visualMap,
      series: [{
        ...mapInstance.getOption().series[0],
        data: layerData.data
      }]
    })
  }
}
</script>
```

#### 4.1.3 数据表格组件 (TableComponent.vue)
```vue
<template>
  <div class="table-container">
    <div class="table-header" v-if="title">
      <h3>{{ title }}</h3>
      <div class="table-tools">
        <el-input 
          v-if="searchable"
          v-model="searchText"
          placeholder="搜索..."
          size="small"
          style="width: 200px"
        />
        <el-button size="small" @click="exportTable">导出</el-button>
      </div>
    </div>
    <el-table 
      :data="filteredData" 
      :size="tableSize"
      :stripe="stripe"
      :border="border"
      style="width: 100%"
    >
      <el-table-column 
        v-for="col in columns" 
        :key="col.prop"
        :prop="col.prop" 
        :label="col.label"
        :width="col.width"
        :formatter="col.formatter"
        :sortable="col.sortable"
      />
    </el-table>
    <el-pagination
      v-if="pagination && total > pageSize"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  title: String,
  data: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
  searchable: { type: Boolean, default: false },
  pagination: { type: Boolean, default: false },
  tableSize: { type: String, default: 'default' },
  stripe: { type: Boolean, default: true },
  border: { type: Boolean, default: true }
})

const searchText = ref('')
const currentPage = ref(1)
const pageSize = ref(20)

const filteredData = computed(() => {
  let result = props.data
  
  // 搜索过滤
  if (searchText.value && props.searchable) {
    result = result.filter(row => 
      Object.values(row).some(val => 
        String(val).toLowerCase().includes(searchText.value.toLowerCase())
      )
    )
  }
  
  // 分页处理
  if (props.pagination) {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    result = result.slice(start, end)
  }
  
  return result
})

const total = computed(() => props.data.length)
</script>
```

### 4.2 主页面组件 (DashboardView.vue)
```vue
<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h1>{{ pageTitle }}</h1>
      <div class="header-controls">
        <el-select v-model="selectedCity" @change="onCityChange">
          <el-option 
            v-for="city in cities" 
            :key="city.code"
            :label="city.name" 
            :value="city.code"
          />
        </el-select>
        <el-select v-model="selectedProduct" @change="onProductChange">
          <el-option 
            v-for="product in products" 
            :key="product.code"
            :label="product.name" 
            :value="product.code"
          />
        </el-select>
      </div>
    </div>
    
    <div class="dashboard-layout">
      <div class="layout-left">
        <component 
          v-for="widget in leftWidgets" 
          :key="widget.widget_id"
          :is="getComponentType(widget.type)"
          v-bind="widget"
        />
      </div>
      
      <div class="layout-center">
        <component 
          v-if="centerWidget"
          :is="getComponentType(centerWidget.type)"
          v-bind="centerWidget"
        />
      </div>
      
      <div class="layout-right">
        <component 
          v-for="widget in rightWidgets" 
          :key="widget.widget_id"
          :is="getComponentType(widget.type)"
          v-bind="widget"
        />
      </div>
    </div>
    
    <div class="dashboard-bottom">
      <component 
        v-if="bottomWidget"
        :is="getComponentType(bottomWidget.type)"
        v-bind="bottomWidget"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ChartComponent from '@/components/ChartComponent.vue'
import MapComponent from '@/components/MapComponent.vue'
import TableComponent from '@/components/TableComponent.vue'
import ScrollListComponent from '@/components/ScrollListComponent.vue'
import { getDashboardConfig } from '@/api/dashboard'

const route = useRoute()
const router = useRouter()

const selectedCity = ref('haikou')
const selectedProduct = ref('litchi')
const dashboardConfig = ref(null)

const cities = ref([
  { code: 'haikou', name: '海口市' },
  { code: 'sanya', name: '三亚市' },
  // ... 其他市县
])

const products = ref([
  { code: 'litchi', name: '荔枝' },
  { code: 'coconut', name: '椰子' },
  // ... 其他产品
])

// 计算属性
const pageTitle = computed(() => dashboardConfig.value?.page_meta?.page_title || '')
const leftWidgets = computed(() => {
  const layout = dashboardConfig.value?.layout || {}
  return Object.entries(layout)
    .filter(([key]) => key.startsWith('left_'))
    .map(([key, widget]) => widget)
})

const onMounted(async () => {
  // 从URL参数初始化
  selectedCity.value = route.query.city || 'haikou'
  selectedProduct.value = route.query.product || 'litchi'
  
  await loadDashboardConfig()
})

const loadDashboardConfig = async () => {
  try {
    const response = await getDashboardConfig({
      cityCode: selectedCity.value,
      productCode: selectedProduct.value
    })
    dashboardConfig.value = response.data
  } catch (error) {
    console.error('加载仪表板配置失败:', error)
  }
}

const getComponentType = (type) => {
  const componentMap = {
    'Chart': ChartComponent,
    'Map': MapComponent,
    'Table': TableComponent,
    'ScrollList': ScrollListComponent
  }
  return componentMap[type] || ChartComponent
}

const onCityChange = () => {
  updateRoute()
  loadDashboardConfig()
}

const onProductChange = () => {
  updateRoute()
  loadDashboardConfig()
}

const updateRoute = () => {
  router.push({
    query: {
      city: selectedCity.value,
      product: selectedProduct.value
    }
  })
}
</script>
```

## 五、后端服务架构

### 5.1 Spring Boot项目结构
```
src/main/java/com/hainan/dashboard/
├── config/                 # 配置类
├── controller/            # 控制器
│   ├── DashboardController.java
│   ├── AdminController.java
│   └── DataController.java
├── service/               # 服务层
│   ├── DashboardService.java
│   ├── ConfigService.java
│   └── DataService.java
├── repository/            # 数据访问层
├── entity/               # 实体类
├── dto/                  # 数据传输对象
├── utils/                # 工具类
└── DashboardApplication.java
```

### 5.2 核心服务实现

#### 5.2.1 仪表板配置服务 (DashboardService.java)
```java
@Service
@Slf4j
public class DashboardService {
    
    @Autowired
    private DashboardLayoutRepository layoutRepository;
    
    @Autowired
    private DashboardWidgetRepository widgetRepository;
    
    @Autowired
    private DataService dataService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 获取仪表板配置
     */
    public DashboardConfigDTO getDashboardConfig(String cityCode, String productCode, String layoutVersion) {
        String cacheKey = String.format("dashboard:config:%s:%s:%s", cityCode, productCode, layoutVersion);
        
        // 尝试从缓存获取
        DashboardConfigDTO cachedConfig = (DashboardConfigDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedConfig != null) {
            return cachedConfig;
        }
        
        // 从数据库获取布局配置
        DashboardLayout layout = layoutRepository.findByRegionAndProduct(cityCode, productCode, layoutVersion);
        if (layout == null) {
            layout = layoutRepository.findDefaultLayout();
        }
        
        // 构建配置对象
        DashboardConfigDTO config = buildDashboardConfig(layout, cityCode, productCode);
        
        // 缓存配置（缓存1小时）
        redisTemplate.opsForValue().set(cacheKey, config, Duration.ofHours(1));
        
        return config;
    }
    
    private DashboardConfigDTO buildDashboardConfig(DashboardLayout layout, String cityCode, String productCode) {
        DashboardConfigDTO config = new DashboardConfigDTO();
        
        // 设置页面元信息
        PageMetaDTO pageMeta = new PageMetaDTO();
        pageMeta.setCityName(getCityName(cityCode));
        pageMeta.setProductName(getProductName(productCode));
        pageMeta.setPageTitle(String.format("%s%s产业一张图", pageMeta.getCityName(), pageMeta.getProductName()));
        config.setPageMeta(pageMeta);
        
        // 构建布局配置
        Map<String, WidgetConfigDTO> layoutConfig = new HashMap<>();
        JSONObject layoutJson = layout.getLayoutConfig();
        
        for (String position : layoutJson.keySet()) {
            JSONObject widgetConfig = layoutJson.getJSONObject(position);
            String widgetCode = widgetConfig.getString("widget_code");
            
            DashboardWidget widget = widgetRepository.findByCode(widgetCode);
            if (widget != null) {
                WidgetConfigDTO widgetDto = buildWidgetConfig(widget, cityCode, productCode);
                layoutConfig.put(position, widgetDto);
            }
        }
        
        config.setLayout(layoutConfig);
        return config;
    }
    
    private WidgetConfigDTO buildWidgetConfig(DashboardWidget widget, String cityCode, String productCode) {
        WidgetConfigDTO config = new WidgetConfigDTO();
        config.setWidgetId(widget.getWidgetCode());
        config.setType(widget.getWidgetType());
        config.setTitle(buildTitle(widget.getTitleTemplate(), cityCode, productCode));
        
        // 根据组件类型构建配置
        switch (widget.getWidgetType()) {
            case "Chart":
                config.setChartOptions(buildChartOptions(widget, cityCode, productCode));
                break;
            case "Map":
                config.setMapOptions(buildMapOptions(widget, cityCode, productCode));
                break;
            case "Table":
                config.setTableOptions(buildTableOptions(widget, cityCode, productCode));
                break;
        }
        
        return config;
    }
    
    private JSONObject buildChartOptions(DashboardWidget widget, String cityCode, String productCode) {
        // 获取数据
        List<Map<String, Object>> data = dataService.getWidgetData(widget.getDataSource(), cityCode, productCode);
        
        // 构建ECharts配置
        JSONObject options = widget.getDefaultOptions();
        
        // 根据数据更新配置
        if ("bar".equals(widget.getChartType())) {
            updateBarChartData(options, data);
        } else if ("line".equals(widget.getChartType())) {
            updateLineChartData(options, data);
        } else if ("pie".equals(widget.getChartType())) {
            updatePieChartData(options, data);
        }
        
        return options;
    }
}
```

#### 5.2.2 数据服务 (DataService.java)
```java
@Service
public class DataService {
    
    @Autowired
    private DataSourceRepository dataSourceRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 获取组件数据
     */
    public List<Map<String, Object>> getWidgetData(String dataSourceCode, String cityCode, String productCode) {
        DataSource dataSource = dataSourceRepository.findByCode(dataSourceCode);
        if (dataSource == null) {
            throw new RuntimeException("数据源不存在: " + dataSourceCode);
        }
        
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("productCode", productCode);
        params.put("currentYear", Year.now().getValue());
        
        // 替换SQL模板中的参数
        String sql = replaceSqlParameters(dataSource.getQueryTemplate(), params);
        
        // 执行查询
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        
        // 应用数据映射
        return applyDataMapping(result, dataSource.getDataMapping());
    }
    
    private String replaceSqlParameters(String sqlTemplate, Map<String, Object> params) {
        String sql = sqlTemplate;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sql = sql.replace("#{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return sql;
    }
    
    private List<Map<String, Object>> applyDataMapping(List<Map<String, Object>> data, JSONObject mapping) {
        if (mapping == null || mapping.isEmpty()) {
            return data;
        }
        
        return data.stream().map(row -> {
            Map<String, Object> mappedRow = new HashMap<>();
            for (String key : mapping.keySet()) {
                String sourceField = mapping.getString(key);
                mappedRow.put(key, row.get(sourceField));
            }
            return mappedRow;
        }).collect(Collectors.toList());
    }
}
```

### 5.3 管理后台实现

#### 5.3.1 布局配置管理
```java
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    @Autowired
    private ConfigService configService;
    
    /**
     * 获取布局模板列表
     */
    @GetMapping("/layouts")
    public Result<List<LayoutTemplateDTO>> getLayouts() {
        List<LayoutTemplateDTO> layouts = configService.getAllLayouts();
        return Result.success(layouts);
    }
    
    /**
     * 创建布局配置
     */
    @PostMapping("/layouts")
    public Result<Void> createLayout(@RequestBody CreateLayoutRequest request) {
        configService.createLayout(request);
        return Result.success();
    }
    
    /**
     * 更新组件配置
     */
    @PutMapping("/widgets/{widgetId}")
    public Result<Void> updateWidget(@PathVariable Long widgetId, @RequestBody UpdateWidgetRequest request) {
        configService.updateWidget(widgetId, request);
        return Result.success();
    }
    
    /**
     * 预览配置效果
     */
    @PostMapping("/preview")
    public Result<DashboardConfigDTO> previewConfig(@RequestBody PreviewRequest request) {
        DashboardConfigDTO config = configService.previewConfig(request);
        return Result.success(config);
    }
}
```

## 六、开发实施指南

### 6.1 开发阶段规划

#### 第一阶段：基础架构搭建 (1-2周)
1. **数据库设计与初始化**
   - 创建所有数据表
   - 初始化基础数据（18个市县、产品分类等）
   - 准备测试数据

2. **后端框架搭建**
   - Spring Boot项目初始化
   - 配置数据库连接、Redis缓存
   - 搭建基础的MVC架构

3. **前端框架搭建**
   - Vue3项目初始化
   - 引入Element Plus、ECharts等依赖
   - 搭建基础路由和布局

#### 第二阶段：核心功能开发 (2-3周)
1. **API接口开发**
   - 实现仪表板配置API
   - 实现数据获取API
   - 完善错误处理和响应格式

2. **前端组件开发**
   - 开发通用图表组件
   - 开发地图组件（海南地图）
   - 开发表格和滚动列表组件

3. **数据处理逻辑**
   - 实现数据源管理
   - 实现数据查询和转换
   - 实现缓存机制

#### 第三阶段：业务功能完善 (1-2周)
1. **管理后台开发**
   - 布局配置管理界面
   - 组件配置管理界面
   - 数据源配置界面

2. **业务逻辑完善**
   - 18个市县数据适配
   - 多产品类型支持
   - 动态标题和配置

#### 第四阶段：测试与优化 (1周)
1. **功能测试**
2. **性能优化**
3. **部署上线**

### 6.2 数据初始化示例

#### 6.2.1 布局配置数据
```sql
-- 插入默认布局配置
INSERT INTO dashboard_layouts (layout_code, layout_name, region_codes, product_codes, layout_config, is_default) VALUES
('default_agricultural', '农业产业默认布局', '["haikou","sanya","qionghai"]', '["litchi","coconut","mango"]', '{
  "left_1": {"widget_code": "area_trend_chart", "position": "left_1"},
  "left_2": {"widget_code": "production_trend_chart", "position": "left_2"},
  "left_3": {"widget_code": "variety_distribution", "position": "left_3"},
  "center_map": {"widget_code": "production_map", "position": "center_map"},
  "right_1": {"widget_code": "sales_trend_chart", "position": "right_1"},
  "right_2": {"widget_code": "price_trend_chart", "position": "right_2"},
  "bottom_scroll": {"widget_code": "price_monitoring_list", "position": "bottom_scroll"}
}', 1);
```

#### 6.2.2 组件配置数据
```sql
-- 插入图表组件配置
INSERT INTO dashboard_widgets (widget_code, widget_name, widget_type, chart_type, data_source, title_template, default_options) VALUES
('area_trend_chart', '种植面积趋势图', 'Chart', 'bar', 'agricultural_area_trend', '近年#{cityName}#{productName}面积情况', '{
  "tooltip": {"trigger": "axis"},
  "legend": {"show": true, "bottom": "5%"},
  "grid": {"left": "3%", "right": "4%", "bottom": "15%", "containLabel": true},
  "xAxis": {"type": "category"},
  "yAxis": {"type": "value", "name": "万亩"},
  "series": [
    {"name": "种植面积", "type": "bar", "barWidth": 20},
    {"name": "收获面积", "type": "bar", "barWidth": 20}
  ]
}');
```

#### 6.2.3 数据源配置
```sql
-- 插入数据源配置
INSERT INTO data_sources (source_code, source_name, source_type, query_template, data_mapping) VALUES
('agricultural_area_trend', '农业面积趋势数据', 'database', 
'SELECT stat_year as year, planting_area, harvest_area 
 FROM agricultural_statistics 
 WHERE region_code = #{cityCode} AND product_code = #{productCode}
 ORDER BY stat_year DESC LIMIT 5', 
'{"categories": "year", "planting_area": "planting_area", "harvest_area": "harvest_area"}');
```

### 6.3 前端Mock数据示例

创建 `src/mock/dashboard.js`：
```javascript
export const mockDashboardConfig = {
  page_meta: {
    page_title: "海口市荔枝产业一张图",
    city_name: "海口市",
    city_code: "haikou",
    product_name: "荔枝",
    product_code: "litchi",
    update_time: "2024-01-20 10:00:00"
  },
  layout: {
    left_1: {
      widget_id: "area_trend_chart",
      title: "近年海口荔枝面积情况",
      type: "Chart",
      chart_options: {
        tooltip: { trigger: "axis" },
        legend: { show: true, bottom: "5%" },
        dataset: {
          source: [
            ["年份", "种植面积（万亩）", "收获面积（万亩）"],
            ["2020", 9.32, 6.95],
            ["2021", 10.99, 9.24],
            ["2022", 11.61, 9.68],
            ["2023", 12.53, 10.93],
            ["2024", 13.42, 11.45]
          ]
        },
        xAxis: { type: "category" },
        yAxis: { type: "value", name: "万亩" },
        series: [
          { type: "bar", barWidth: 20 },
          { type: "bar", barWidth: 20 }
        ]
      }
    }
    // ... 其他组件配置
  }
}
```

## 七、总结

这个技术方案的核心优势：

1. **高度可配置化**：通过元数据驱动，实现了真正的"零代码"配置
2. **前后端解耦**：清晰的API契约设计，支持并行开发
3. **可扩展性强**：新增市县、产品或图表类型都只需配置，无需修改代码
4. **性能优化**：合理的缓存策略和数据分层
5. **易于维护**：统一的组件规范和配置管理

通过这套方案，可以快速支持海南18个市县的不同产业数据展示，并且具备良好的扩展性，为未来的功能增强打下坚实基础。