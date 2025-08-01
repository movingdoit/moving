# 海南18个市县一张图API接口设计（优化版）

## API接口总览

基于新的布局位置数据库设计，重新设计API接口规范：

### 核心接口列表

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v1/dashboard/layout` | GET | 获取页面布局配置 |
| `/api/v1/dashboard/widget/{position}` | GET | 获取指定位置的组件数据 |
| `/api/v1/dashboard/map/layers` | GET | 获取地图图层配置 |
| `/api/v1/dashboard/map/data/{layerCode}` | GET | 获取地图图层数据 |
| `/api/v1/dashboard/batch-widgets` | POST | 批量获取组件数据 |

---

## 1. 页面布局配置接口

### 获取页面布局配置
```http
GET /api/v1/dashboard/layout?regionCode=haikou&productCode=litchi
```

**请求参数：**
- `regionCode`: 区域代码（必填）
- `productCode`: 产品代码（必填）

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "region_info": {
      "region_code": "haikou",
      "region_name": "海口市",
      "product_code": "litchi",
      "product_name": "荔枝"
    },
    "layout_positions": {
      "left_1": {
        "position_code": "left_1",
        "position_name": "左一区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "area_trend_chart",
          "widget_title": "近年海口荔枝面积情况",
          "widget_type": "chart",
          "chart_type": "bar",
          "data_api": "/api/v1/dashboard/widget/left_1?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      },
      "left_2": {
        "position_code": "left_2",
        "position_name": "左二区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "production_trend_chart",
          "widget_title": "近年海口荔枝产量情况",
          "widget_type": "chart",
          "chart_type": "line",
          "data_api": "/api/v1/dashboard/widget/left_2?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      },
      "left_3": {
        "position_code": "left_3",
        "position_name": "左三区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "variety_pie_chart",
          "widget_title": "海口荔枝主要品种分布",
          "widget_type": "chart",
          "chart_type": "pie",
          "data_api": "/api/v1/dashboard/widget/left_3?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      },
      "center_map": {
        "position_code": "center_map",
        "position_name": "中间地图区域",
        "position_type": "map",
        "map": {
          "map_code": "haikou_districts",
          "map_title": "海口各区荔枝生产分布",
          "layers_api": "/api/v1/dashboard/map/layers?regionCode=haikou&productCode=litchi",
          "default_layer": "area"
        }
      },
      "center_bottom": {
        "position_code": "center_bottom",
        "position_name": "中下区域",
        "position_type": "scroll",
        "widget": {
          "widget_code": "price_monitor_list",
          "widget_title": "荔枝价格动态监测",
          "widget_type": "list",
          "data_api": "/api/v1/dashboard/widget/center_bottom?regionCode=haikou&productCode=litchi",
          "refresh_interval": 60
        }
      },
      "right_1": {
        "position_code": "right_1",
        "position_name": "右一区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "sales_trend_chart",
          "widget_title": "近年海口荔枝销售额情况",
          "widget_type": "chart",
          "chart_type": "bar",
          "data_api": "/api/v1/dashboard/widget/right_1?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      },
      "right_2": {
        "position_code": "right_2",
        "position_name": "右二区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "price_trend_chart",
          "widget_title": "近期海口荔枝价格情况",
          "widget_type": "chart",
          "chart_type": "line",
          "data_api": "/api/v1/dashboard/widget/right_2?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      },
      "right_3": {
        "position_code": "right_3",
        "position_name": "右三区域",
        "position_type": "widget",
        "widget": {
          "widget_code": "cooperative_stats_chart",
          "widget_title": "海口荔枝合作社统计",
          "widget_type": "chart",
          "chart_type": "bar",
          "data_api": "/api/v1/dashboard/widget/right_3?regionCode=haikou&productCode=litchi",
          "refresh_interval": 300
        }
      }
    }
  }
}
```

---

## 2. 组件数据接口

### 获取指定位置的组件数据
```http
GET /api/v1/dashboard/widget/{position}?regionCode=haikou&productCode=litchi
```

**路径参数：**
- `position`: 位置代码（left_1, left_2, left_3, right_1, right_2, right_3, center_bottom）

**请求参数：**
- `regionCode`: 区域代码（必填）
- `productCode`: 产品代码（必填）

**响应示例（柱状图）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "widget_info": {
      "position_code": "left_1",
      "widget_code": "area_trend_chart",
      "widget_title": "近年海口荔枝面积情况",
      "widget_type": "chart",
      "chart_type": "bar",
      "last_update": "2024-01-20 10:30:00"
    },
    "chart_config": {
      "tooltip": {
        "trigger": "axis",
        "axisPointer": {
          "type": "shadow"
        }
      },
      "legend": {
        "show": true,
        "bottom": "5%",
        "textStyle": {
          "color": "#ffffff"
        }
      },
      "grid": {
        "left": "3%",
        "right": "4%",
        "bottom": "15%",
        "containLabel": true
      },
      "xAxis": {
        "type": "category",
        "axisLabel": {
          "color": "#ffffff"
        },
        "data": ["2020", "2021", "2022", "2023", "2024"]
      },
      "yAxis": {
        "type": "value",
        "name": "万亩",
        "nameTextStyle": {
          "color": "#ffffff"
        },
        "axisLabel": {
          "color": "#ffffff"
        },
        "splitLine": {
          "lineStyle": {
            "color": "rgba(255,255,255,0.1)"
          }
        }
      },
      "series": [
        {
          "name": "种植面积",
          "type": "bar",
          "barWidth": 20,
          "data": [9.32, 10.99, 11.61, 12.53, 13.42]
        },
        {
          "name": "收获面积",
          "type": "bar",
          "barWidth": 20,
          "data": [6.95, 9.24, 9.68, 10.93, 11.45]
        }
      ]
    }
  }
}
```

**响应示例（滚动列表）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "widget_info": {
      "position_code": "center_bottom",
      "widget_code": "price_monitor_list",
      "widget_title": "荔枝价格动态监测",
      "widget_type": "list",
      "last_update": "2024-01-20 10:30:00"
    },
    "list_config": {
      "scroll_speed": 50,
      "row_height": 40,
      "headers": ["日期", "品种", "产地", "价格(元/斤)", "涨跌幅(%)"]
    },
    "list_data": [
      {
        "date": "2024-01-20",
        "variety": "妃子笑",
        "origin": "琼山区",
        "price": 16.48,
        "change_rate": -0.91
      },
      {
        "date": "2024-01-20",
        "variety": "白糖罂",
        "origin": "秀英区",
        "price": 19.86,
        "change_rate": 2.15
      }
    ]
  }
}
```

---

## 3. 地图图层配置接口

### 获取地图图层配置
```http
GET /api/v1/dashboard/map/layers?regionCode=haikou&productCode=litchi
```

**请求参数：**
- `regionCode`: 区域代码（必填）
- `productCode`: 产品代码（必填）

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "map_info": {
      "region_code": "haikou",
      "region_name": "海口市",
      "product_code": "litchi",
      "product_name": "荔枝",
      "map_title": "2024年海口各区荔枝生产情况"
    },
    "available_layers": [
      {
        "layer_code": "area",
        "layer_name": "面积",
        "layer_icon": "area-icon",
        "unit": "万亩",
        "is_default": true,
        "color_scheme": {
          "type": "continuous",
          "min": 0,
          "max": 10,
          "colors": ["#dff6ff", "#47b5ff", "#1363df"]
        },
        "data_api": "/api/v1/dashboard/map/data/area?regionCode=haikou&productCode=litchi"
      },
      {
        "layer_code": "production",
        "layer_name": "产量",
        "layer_icon": "production-icon",
        "unit": "万吨",
        "is_default": false,
        "color_scheme": {
          "type": "continuous",
          "min": 0,
          "max": 8,
          "colors": ["#f6fdd8", "#ffc54d", "#ff8400"]
        },
        "data_api": "/api/v1/dashboard/map/data/production?regionCode=haikou&productCode=litchi"
      },
      {
        "layer_code": "cooperative",
        "layer_name": "合作社",
        "layer_icon": "cooperative-icon",
        "unit": "个",
        "is_default": false,
        "color_scheme": {
          "type": "continuous",
          "min": 0,
          "max": 60,
          "colors": ["#e4fde2", "#74d680", "#2b9348"]
        },
        "data_api": "/api/v1/dashboard/map/data/cooperative?regionCode=haikou&productCode=litchi"
      }
    ]
  }
}
```

---

## 4. 地图图层数据接口（地图数据切换专用）

### 获取地图图层数据
```http
GET /api/v1/dashboard/map/data/{layerCode}?regionCode=haikou&productCode=litchi&year=2024
```

**路径参数：**
- `layerCode`: 图层代码（area, production, cooperative等）

**请求参数：**
- `regionCode`: 区域代码（必填）
- `productCode`: 产品代码（必填）
- `year`: 数据年份（可选，默认当前年份）

**响应示例（面积图层）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "layer_info": {
      "layer_code": "area",
      "layer_name": "面积",
      "unit": "万亩",
      "data_year": 2024,
      "last_update": "2024-01-20 09:00:00"
    },
    "map_config": {
      "visualMap": {
        "min": 0,
        "max": 10,
        "left": "left",
        "top": "bottom",
        "text": ["高", "低"],
        "calculable": true,
        "inRange": {
          "color": ["#dff6ff", "#47b5ff", "#1363df"]
        },
        "textStyle": {
          "color": "#ffffff"
        }
      },
      "series": [{
        "name": "种植面积",
        "type": "map",
        "mapType": "haikou",
        "roam": false,
        "label": {
          "show": true,
          "color": "#ffffff"
        },
        "itemStyle": {
          "borderColor": "#ffffff",
          "borderWidth": 1
        },
        "data": [
          {
            "name": "秀英区",
            "value": 3.56,
            "region_code": "haikou_xiuying"
          },
          {
            "name": "龙华区",
            "value": 0.28,
            "region_code": "haikou_longhua"
          },
          {
            "name": "琼山区",
            "value": 8.99,
            "region_code": "haikou_qiongshan"
          },
          {
            "name": "美兰区",
            "value": 0.57,
            "region_code": "haikou_meilan"
          }
        ]
      }]
    },
    "summary_data": [
      {
        "region_name": "秀英区",
        "value": 3.56,
        "unit": "万亩",
        "percentage": 26.5,
        "rank": 2
      },
      {
        "region_name": "龙华区", 
        "value": 0.28,
        "unit": "万亩",
        "percentage": 2.1,
        "rank": 4
      },
      {
        "region_name": "琼山区",
        "value": 8.99,
        "unit": "万亩", 
        "percentage": 66.9,
        "rank": 1
      },
      {
        "region_name": "美兰区",
        "value": 0.57,
        "unit": "万亩",
        "percentage": 4.2,
        "rank": 3
      }
    ]
  }
}
```

---

## 5. 批量获取组件数据接口

### 批量获取多个位置的组件数据
```http
POST /api/v1/dashboard/batch-widgets
```

**请求体：**
```json
{
  "region_code": "haikou",
  "product_code": "litchi",
  "positions": ["left_1", "left_2", "right_1", "right_2"]
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "region_info": {
      "region_code": "haikou",
      "region_name": "海口市",
      "product_code": "litchi",
      "product_name": "荔枝"
    },
    "widgets": {
      "left_1": {
        "widget_info": {
          "position_code": "left_1",
          "widget_title": "近年海口荔枝面积情况",
          "widget_type": "chart",
          "chart_type": "bar"
        },
        "chart_config": {
          // ECharts配置...
        }
      },
      "left_2": {
        "widget_info": {
          "position_code": "left_2", 
          "widget_title": "近年海口荔枝产量情况",
          "widget_type": "chart",
          "chart_type": "line"
        },
        "chart_config": {
          // ECharts配置...
        }
      }
      // 其他组件数据...
    },
    "last_update": "2024-01-20 10:30:00"
  }
}
```

---

## 6. 系统基础接口

### 获取区域列表
```http
GET /api/v1/system/regions
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success", 
  "data": [
    {
      "region_code": "haikou",
      "region_name": "海口市",
      "region_type": "city",
      "available_products": ["litchi", "coconut", "mango"]
    },
    {
      "region_code": "sanya",
      "region_name": "三亚市", 
      "region_type": "city",
      "available_products": ["mango", "banana"]
    }
    // ... 其他16个市县
  ]
}
```

### 获取产品列表
```http
GET /api/v1/system/products?regionCode=haikou
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "product_code": "litchi",
      "product_name": "荔枝",
      "category_name": "热带水果",
      "unit": "万亩",
      "icon_url": "/icons/litchi.png"
    },
    {
      "product_code": "coconut",
      "product_name": "椰子",
      "category_name": "热带水果", 
      "unit": "万亩",
      "icon_url": "/icons/coconut.png"
    }
    // ... 其他产品
  ]
}
```

---

## API使用流程

### 前端页面加载流程

1. **获取页面布局**
   ```javascript
   // 1. 获取布局配置
   const layoutResponse = await api.get('/api/v1/dashboard/layout', {
     params: { regionCode: 'haikou', productCode: 'litchi' }
   });
   
   // 2. 根据布局配置渲染页面结构
   const layout = layoutResponse.data.layout_positions;
   ```

2. **加载组件数据**
   ```javascript
   // 3. 批量获取组件数据
   const widgetResponse = await api.post('/api/v1/dashboard/batch-widgets', {
     region_code: 'haikou',
     product_code: 'litchi', 
     positions: ['left_1', 'left_2', 'left_3', 'right_1', 'right_2', 'right_3', 'center_bottom']
   });
   ```

3. **加载地图配置和数据**
   ```javascript
   // 4. 获取地图图层配置
   const layersResponse = await api.get('/api/v1/dashboard/map/layers', {
     params: { regionCode: 'haikou', productCode: 'litchi' }
   });
   
   // 5. 获取默认图层数据
   const defaultLayer = layersResponse.data.available_layers.find(l => l.is_default);
   const mapDataResponse = await api.get(`/api/v1/dashboard/map/data/${defaultLayer.layer_code}`, {
     params: { regionCode: 'haikou', productCode: 'litchi', year: 2024 }
   });
   ```

4. **地图图层切换**
   ```javascript
   // 6. 用户点击切换图层时
   const switchMapLayer = async (layerCode) => {
     const newMapData = await api.get(`/api/v1/dashboard/map/data/${layerCode}`, {
       params: { regionCode: 'haikou', productCode: 'litchi', year: 2024 }
     });
     
     // 只更新地图数据，其他组件保持不变
     updateMapVisualization(newMapData.data);
   };
   ```

### 数据更新流程

```javascript
// 定时刷新组件数据
setInterval(async () => {
  // 只刷新需要实时更新的组件（如价格监测）
  const priceData = await api.get('/api/v1/dashboard/widget/center_bottom', {
    params: { regionCode: 'haikou', productCode: 'litchi' }
  });
  updatePriceList(priceData.data);
}, 60000); // 1分钟刷新一次
```

这样设计的优势：

1. **清晰的职责分离**：布局配置、组件数据、地图数据各自独立
2. **高效的数据加载**：支持批量获取和按需加载
3. **灵活的地图切换**：专用的地图数据接口，切换时只更新地图数据
4. **良好的缓存策略**：不同类型的数据可以设置不同的缓存时间
5. **易于扩展**：新增位置或图层只需要在数据库中配置