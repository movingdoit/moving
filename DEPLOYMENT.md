# 海南18个市县一张图部署指南

## 部署架构

本项目支持多种部署方式，推荐使用Docker容器化部署，也支持传统的物理机/虚拟机部署。

## 系统要求

### 硬件要求
- **CPU**: 4核心以上
- **内存**: 8GB以上（推荐16GB）
- **存储**: 100GB以上SSD硬盘
- **网络**: 100Mbps以上带宽

### 软件要求
- **操作系统**: Ubuntu 20.04+ / CentOS 7+ / Docker环境
- **Java**: OpenJDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nginx**: 1.18+

## Docker部署（推荐）

### 1. 环境准备

#### 安装Docker和Docker Compose
```bash
# Ubuntu/Debian
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# 安装Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### 克隆项目代码
```bash
git clone https://github.com/your-org/hainan-dashboard.git
cd hainan-dashboard
```

### 2. 配置文件准备

#### 创建环境变量文件
```bash
# 创建 .env 文件
cat > .env << EOF
# 数据库配置
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=hainan_dashboard
MYSQL_USER=dashboard_user
MYSQL_PASSWORD=your_dashboard_password

# Redis配置
REDIS_PASSWORD=your_redis_password

# 应用配置
SPRING_PROFILES_ACTIVE=docker
TZ=Asia/Shanghai
EOF
```

#### 创建Docker配置目录
```bash
mkdir -p docker/{mysql/conf.d,redis,nginx/conf.d,prometheus,grafana}
```

#### MySQL配置
```bash
cat > docker/mysql/conf.d/my.cnf << EOF
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
default-time-zone='+08:00'
max_connections=200
innodb_buffer_pool_size=1G
innodb_log_file_size=256M
slow_query_log=1
slow_query_log_file=/var/log/mysql/slow.log
long_query_time=2
EOF
```

#### Redis配置
```bash
cat > docker/redis/redis.conf << EOF
bind 0.0.0.0
port 6379
timeout 300
tcp-keepalive 60
maxmemory 512mb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
EOF
```

#### Nginx配置
```bash
cat > docker/nginx/conf.d/default.conf << EOF
upstream backend {
    server backend:8080;
}

server {
    listen 80;
    server_name localhost;
    client_max_body_size 10M;

    # 前端静态资源
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files \$uri \$uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://backend;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # 文件上传代理
    location /upload {
        proxy_pass http://backend;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        client_max_body_size 50M;
    }

    # API文档
    location /doc.html {
        proxy_pass http://backend;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
    }
}
EOF
```

### 3. 构建和启动

#### 构建应用镜像
```bash
# 构建后端镜像
docker build -t hainan-dashboard-backend:latest ./backend

# 构建前端镜像
docker build -t hainan-dashboard-frontend:latest ./frontend
```

#### 启动服务
```bash
# 启动基础服务（数据库、缓存、应用）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
```

#### 启动监控服务（可选）
```bash
# 启动监控组件
docker-compose --profile monitoring up -d

# 访问监控面板
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3001 (admin/admin123)
```

### 4. 验证部署

```bash
# 检查服务状态
curl http://localhost:8080/actuator/health

# 检查API文档
curl http://localhost:8080/doc.html

# 检查前端页面
curl http://localhost/
```

## 传统部署方式

### 1. 环境准备

#### 安装Java 17
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel
```

#### 安装Node.js
```bash
# 使用NodeSource仓库
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

#### 安装MySQL
```bash
# Ubuntu/Debian
sudo apt install mysql-server

# 初始化数据库
mysql -u root -p < database_init.sql
```

#### 安装Redis
```bash
# Ubuntu/Debian
sudo apt install redis-server

# 启动Redis
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

#### 安装Nginx
```bash
# Ubuntu/Debian
sudo apt install nginx

# 启动Nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

### 2. 后端部署

#### 编译打包
```bash
cd backend
mvn clean package -P prod
```

#### 创建运行用户
```bash
sudo useradd -r -s /bin/false dashboard
sudo mkdir -p /opt/hainan-dashboard
sudo chown dashboard:dashboard /opt/hainan-dashboard
```

#### 部署应用
```bash
# 复制jar包
sudo cp target/dashboard-backend-1.0.0.jar /opt/hainan-dashboard/

# 创建配置文件
sudo mkdir -p /opt/hainan-dashboard/config
sudo cp src/main/resources/application-prod.yml /opt/hainan-dashboard/config/
```

#### 创建systemd服务
```bash
sudo cat > /etc/systemd/system/hainan-dashboard.service << EOF
[Unit]
Description=Hainan Dashboard Backend
After=network.target mysql.service redis.service

[Service]
Type=simple
User=dashboard
Group=dashboard
WorkingDirectory=/opt/hainan-dashboard
ExecStart=/usr/bin/java -jar -Xms1g -Xmx2g -Dspring.profiles.active=prod dashboard-backend-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# 启动服务
sudo systemctl daemon-reload
sudo systemctl start hainan-dashboard
sudo systemctl enable hainan-dashboard
```

### 3. 前端部署

#### 构建前端
```bash
cd frontend
npm install
npm run build
```

#### 部署到Nginx
```bash
# 复制构建产物
sudo cp -r dist/* /var/www/html/

# 配置Nginx
sudo cat > /etc/nginx/sites-available/hainan-dashboard << EOF
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/html;
    index index.html;

    location / {
        try_files \$uri \$uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
    }
}
EOF

# 启用站点
sudo ln -s /etc/nginx/sites-available/hainan-dashboard /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

## 生产环境优化

### 1. 数据库优化

#### MySQL配置优化
```sql
-- 创建索引
CREATE INDEX idx_agri_stats_region_product_year ON agricultural_statistics(region_code, product_code, stat_year);
CREATE INDEX idx_price_region_product_date ON price_monitoring(region_code, product_code, price_date);

-- 设置MySQL参数
SET GLOBAL innodb_buffer_pool_size = 2147483648; -- 2GB
SET GLOBAL max_connections = 300;
SET GLOBAL query_cache_size = 268435456; -- 256MB
```

#### 定期备份脚本
```bash
#!/bin/bash
# backup-db.sh

BACKUP_DIR="/data/backups/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="hainan_dashboard_${DATE}.sql"

mkdir -p $BACKUP_DIR

mysqldump -u root -p \
  --single-transaction \
  --routines \
  --triggers \
  hainan_dashboard > "$BACKUP_DIR/$BACKUP_FILE"

# 压缩备份文件
gzip "$BACKUP_DIR/$BACKUP_FILE"

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete
```

### 2. 缓存优化

#### Redis配置优化
```bash
# /etc/redis/redis.conf
maxmemory 2gb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
```

### 3. 应用优化

#### JVM参数调优
```bash
# 生产环境JVM参数
JAVA_OPTS="-Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/hainan-dashboard/logs/heapdump.hprof \
  -Dspring.profiles.active=prod"
```

### 4. 安全配置

#### Nginx安全配置
```nginx
# 隐藏Nginx版本
server_tokens off;

# 安全头设置
add_header X-Frame-Options DENY;
add_header X-Content-Type-Options nosniff;
add_header X-XSS-Protection "1; mode=block";
add_header Strict-Transport-Security "max-age=31536000; includeSubDomains";

# 限制请求大小
client_max_body_size 10M;

# 防止DDoS
limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;
limit_req zone=api burst=20 nodelay;
```

#### 防火墙配置
```bash
# 使用ufw防火墙
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw --force enable
```

## 监控和维护

### 1. 日志管理

#### 配置日志轮转
```bash
# /etc/logrotate.d/hainan-dashboard
/opt/hainan-dashboard/logs/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    sharedscripts
    postrotate
        systemctl reload hainan-dashboard
    endscript
}
```

### 2. 健康检查脚本

```bash
#!/bin/bash
# health-check.sh

# 检查后端服务
backend_status=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health)
if [ "$backend_status" != "200" ]; then
    echo "Backend service is down!"
    # 发送告警通知
    # systemctl restart hainan-dashboard
fi

# 检查数据库连接
mysql_status=$(mysqladmin -u dashboard_user -p ping 2>/dev/null | grep "mysqld is alive" | wc -l)
if [ "$mysql_status" != "1" ]; then
    echo "MySQL is not responding!"
fi

# 检查Redis连接
redis_status=$(redis-cli ping | grep "PONG" | wc -l)
if [ "$redis_status" != "1" ]; then
    echo "Redis is not responding!"
fi
```

### 3. 性能监控

#### 使用Prometheus监控配置
```yaml
# docker/prometheus/prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'hainan-dashboard'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
    
  - job_name: 'mysql'
    static_configs:
      - targets: ['mysql:3306']
      
  - job_name: 'redis'
    static_configs:
      - targets: ['redis:6379']
```

## 故障排除

### 常见问题和解决方案

#### 1. 应用启动失败
```bash
# 查看应用日志
sudo journalctl -u hainan-dashboard -f

# 检查配置文件
sudo -u dashboard java -jar /opt/hainan-dashboard/dashboard-backend-1.0.0.jar --spring.config.check
```

#### 2. 数据库连接问题
```bash
# 检查MySQL服务状态
sudo systemctl status mysql

# 测试数据库连接
mysql -h localhost -u dashboard_user -p hainan_dashboard -e "SELECT 1"
```

#### 3. 前端页面空白
```bash
# 检查Nginx错误日志
sudo tail -f /var/log/nginx/error.log

# 检查前端构建产物
ls -la /var/www/html/
```

#### 4. 性能问题排查
```bash
# 查看系统资源使用
top
iotop
nethogs

# 查看Java堆内存使用
jmap -histo $(pgrep java)
```

## 升级指南

### 1. 数据备份
```bash
# 备份数据库
mysqldump -u root -p hainan_dashboard > backup_before_upgrade.sql

# 备份Redis数据
redis-cli BGSAVE
cp /var/lib/redis/dump.rdb backup_redis_$(date +%Y%m%d).rdb
```

### 2. 应用升级
```bash
# 停止服务
sudo systemctl stop hainan-dashboard

# 备份当前版本
sudo cp /opt/hainan-dashboard/dashboard-backend-1.0.0.jar \
        /opt/hainan-dashboard/dashboard-backend-1.0.0.jar.backup

# 部署新版本
sudo cp target/dashboard-backend-1.1.0.jar /opt/hainan-dashboard/

# 更新服务配置
sudo systemctl start hainan-dashboard
```

### 3. 验证升级
```bash
# 检查应用版本
curl http://localhost:8080/actuator/info

# 验证功能
curl http://localhost:8080/api/v1/dashboard/config?cityCode=haikou
```

---

**部署完成后，记得更改默认密码并定期进行安全更新！**