# PowerOps 前端快速启动指南

## 🚀 5分钟快速启动

### 1. 安装依赖（首次运行）

```bash
cd D:\backend\project\powerops-frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

服务器将自动启动在 **http://localhost:5175/**

### 3. 访问系统

打开浏览器访问：http://localhost:5175/

**默认账号**：
- 用户名：`admin`
- 密码：`admin123`

---

## 📦 可用脚本

```bash
# 开发模式
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

---

## 🔧 常见问题

### Q1: 端口被占用怎么办？

Vite会自动尝试下一个可用端口（5173 → 5174 → 5175...）

### Q2: 如何修改后端API地址？

编辑 `src/utils/request.js`：

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api', // 修改这里
  timeout: 10000
})
```

### Q3: 登录失败？

确保后端服务运行在 `http://localhost:8080`

### Q4: 页面空白？

检查浏览器控制台错误信息，通常是：
- 后端API未启动
- 跨域问题（需要后端配置CORS）
- Token失效

---

## 🎯 核心页面导航

| 页面 | 路由 | 说明 |
|------|------|------|
| 首页 Dashboard | `/dashboard` | 统计卡片 + 3个ECharts图表 |
| 用户管理 | `/user` | 用户列表、角色管理 |
| 商铺管理 | `/shop` | 商铺列表、搜索过滤 |
| 设备管理 | `/device` | 设备树 + 设备详情（两栏布局） |
| 用电管理 | `/power` | 用电记录 + 趋势图 |
| 电费账单 | `/bill` | 账单列表、生成账单、缴费 |
| 报警中心 | `/alarm` | 报警列表、确认报警、生成工单 |
| 工单管理 | `/workorder` | 工单状态流、接单、完成 |
| 巡检管理 | `/inspection` | 巡检计划、分配人员 |

---

## 🎨 设计规范速查

### 颜色
- 主色：`#1677ff`（科技蓝）
- 成功：`#52c41a`（绿色）
- 警告：`#faad14`（橙色）
- 错误：`#ff4d4f`（红色）

### 布局
- Header高度：60px
- Sidebar宽度：200px
- Content内边距：20px

### 图标
所有 Element Plus 图标已全局注册，直接使用：
```vue
<el-icon><User /></el-icon>
<el-icon><Shop /></el-icon>
<el-icon><Monitor /></el-icon>
```

---

## 📞 需要帮助？

查看详细文档：
- [FRONTEND_README.md](./FRONTEND_README.md) - 完整的前端项目说明
- [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md) - 项目完成报告

---

**祝使用愉快！** 🎉
