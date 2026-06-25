# PowerOps 前端项目

商铺配电运维管理系统 - 前端框架（Vue3 + Element Plus）

## 📦 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Element Plus** - 基于 Vue 3 的组件库
- **Pinia** - Vue 状态管理库
- **Vue Router** - Vue.js 官方路由
- **Axios** - HTTP 客户端
- **ECharts** - 数据可视化图表库

## 🚀 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 📁 项目结构

```
powerops-frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口层
│   │   ├── user.js        # 用户管理 API
│   │   ├── shop.js        # 商铺管理 API
│   │   ├── device.js      # 设备管理 API
│   │   ├── power.js       # 用电记录 API
│   │   ├── bill.js        # 账单管理 API
│   │   ├── alarm.js       # 报警管理 API
│   │   ├── workorder.js   # 工单管理 API
│   │   └── inspection.js  # 巡检计划 API
│   ├── assets/            # 静态资源
│   ├── components/        # 公共组件
│   ├── layout/            # 布局组件
│   │   └── MainLayout.vue # 主布局（Header + Sidebar + Content）
│   ├── router/            # 路由配置
│   │   └── index.js       # 路由定义
│   ├── store/             # 状态管理
│   │   └── user.js        # 用户状态
│   ├── utils/             # 工具函数
│   │   └── request.js     # Axios 请求封装
│   ├── views/             # 页面视图
│   │   ├── dashboard/     # 首页 Dashboard
│   │   ├── user/          # 用户管理
│   │   ├── shop/          # 商铺管理
│   │   ├── device/        # 设备管理
│   │   ├── power/         # 用电管理
│   │   ├── bill/          # 账单管理
│   │   ├── alarm/         # 报警中心
│   │   ├── workorder/     # 工单管理
│   │   └── inspection/    # 巡检管理
│   ├── App.vue            # 根组件
│   └── main.js            # 应用入口
├── package.json
├── vite.config.js
└── README.md
```

## 🎨 页面功能

### 1. 登录页面 (`/login`)
- 用户名/密码登录
- Token 存储
- 自动跳转到首页

### 2. 首页 Dashboard (`/dashboard`)
- **统计卡片**：总商铺数、运行设备数、未处理报警、待处理工单
- **ECharts 图表**：
  - 用电趋势折线图（近7天）
  - 报警类型分布饼图
  - 工单状态统计柱状图

### 3. 用户管理 (`/user`)
- 用户列表表格
- 搜索过滤（用户名、角色）
- 新增/编辑/删除用户

### 4. 商铺管理 (`/shop`)
- 商铺列表表格
- 搜索过滤（楼栋、类型）
- 新增/编辑/删除商铺

### 5. 设备管理 (`/device`)
- **两栏布局**：左侧设备树 + 右侧设备详情
- 设备拓扑树（变压器 → 配电柜 → 电表）
- 设备详情展示（电压、电流、健康评分等）

### 6. 用电管理 (`/power`)
- 用电记录表格
- 筛选条件（商铺、时间范围）
- 用电趋势图表

### 7. 电费账单 (`/bill`)
- 账单列表表格
- 按月查询
- 生成账单功能
- 缴费状态更新

### 8. 报警中心 (`/alarm`)
- 报警列表表格
- 筛选（等级、状态）
- 确认报警
- 一键生成工单

### 9. 工单管理 (`/workorder`)
- 工单列表表格
- 状态流：待处理 → 处理中 → 已完成
- 操作：接单、开始处理、完成工单

### 10. 巡检管理 (`/inspection`)
- 巡检计划列表
- 创建/编辑/删除计划
- 分配人员

## 🎯 设计规范

### 颜色方案
- **主色**：`#1677ff`（科技蓝）
- **背景色**：
  - 深色主题：`#0f172a`
  - 浅色主题：`#f5f7fa`
- **状态色**：
  - 成功/正常：`#52c41a`（绿色）
  - 警告：`#faad14`（橙色）
  - 错误/报警：`#ff4d4f`（红色）

### 布局规范
- **Header**：高度 60px，显示系统名称、当前用户、登出按钮
- **Sidebar**：宽度 200px，左侧菜单导航
- **Content**：自适应，内边距 20px

### 图标库
使用 `@element-plus/icons-vue`，所有图标已全局注册。

## 🔧 核心配置

### Axios 请求封装
- 基础 URL：`http://localhost:8080/api`
- 超时时间：10秒
- 请求拦截器：自动添加 Token
- 响应拦截器：统一错误处理

### 路由守卫
- 登录验证：未登录自动跳转到登录页
- 已登录访问登录页自动跳转到首页

### 状态管理（Pinia）
- 存储用户信息、Token
- 提供登录/登出方法

## 📊 ECharts 图表

已在 Dashboard 页面集成 3 个核心图表：
1. **用电趋势图**：折线图，支持周/月切换
2. **报警类型分布**：饼图，环形设计
3. **工单状态统计**：柱状图，展示各状态工单数量

## 🔗 后端 API 对接

确保后端服务运行在 `http://localhost:8080`

主要接口模块：
- `/api/user` - 用户管理
- `/api/shop` - 商铺管理
- `/api/device` - 设备管理
- `/api/power` - 用电记录
- `/api/bill` - 账单管理
- `/api/alarm` - 报警管理
- `/api/workorder` - 工单管理
- `/api/inspection` - 巡检计划

## 📝 开发建议

1. **API 调用**：统一使用 `src/api/` 下的接口函数
2. **样式规范**：使用 scoped CSS，避免全局污染
3. **组件复用**：通用组件放在 `src/components/`
4. **路由权限**：在 `router/index.js` 中配置 `meta.requiresAuth`
5. **错误处理**：使用 Element Plus 的 `ElMessage` 提示

## 🐛 常见问题

### 1. 跨域问题
后端需要配置 CORS 允许前端访问。

### 2. Token 失效
检查 `localStorage` 中的 token 是否过期，重新登录获取新 token。

### 3. 图表不显示
确保容器有明确的高度和宽度。

## 📞 联系方式

如有问题，请联系开发团队。

---

**版本**: v1.0.0  
**更新日期**: 2024-06-17
