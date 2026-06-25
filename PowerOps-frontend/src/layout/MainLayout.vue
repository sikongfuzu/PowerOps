<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <el-icon :size="28" color="#1677ff"><Monitor /></el-icon>
        <span>PowerOps</span>
      </div>
      <el-menu :default-active="activeMenu" router background-color="#0f172a" text-color="#94a3b8" active-text-color="#1677ff">
        <el-menu-item index="/dashboard"><el-icon><Odometer /></el-icon><span>首页</span></el-menu-item>
        <el-menu-item index="/user"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
        <el-menu-item index="/shop"><el-icon><Shop /></el-icon><span>商铺管理</span></el-menu-item>
        <el-menu-item index="/device"><el-icon><Monitor /></el-icon><span>设备管理</span></el-menu-item>
        <el-menu-item index="/alarm"><el-icon><Bell /></el-icon><span>报警中心</span></el-menu-item>
        <el-menu-item index="/workorder"><el-icon><Document /></el-icon><span>工单管理</span></el-menu-item>
        <el-menu-item index="/power"><el-icon><Lightning /></el-icon><span>用电管理</span></el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <span class="header-title">PowerOps 商铺配电运维管理系统</span>
        <el-dropdown @command="handleCommand">
          <span class="user-dropdown">
            <el-icon><UserFilled /></el-icon>
            {{ username }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activeMenu = computed(() => route.path)

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const username = userInfo.realName || userInfo.username || '管理员'

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.sidebar { background: #0f172a; display: flex; flex-direction: column; }
.logo { height: 60px; display: flex; align-items: center; gap: 10px; padding: 0 20px; color: #f1f5f9; font-size: 18px; font-weight: 600; border-bottom: 1px solid #1e293b; }
.el-menu { border-right: none; }
.header { display: flex; align-items: center; justify-content: space-between; background: linear-gradient(135deg, #0f172a, #1e293b); color: #f1f5f9; height: 60px; padding: 0 24px; }
.header-title { font-size: 16px; font-weight: 500; }
.user-dropdown { display: flex; align-items: center; gap: 6px; cursor: pointer; color: #94a3b8; }
.user-dropdown:hover { color: #f1f5f9; }
.main-content { background: #f0f2f5; padding: 20px; overflow-y: auto; }
:deep(.el-menu-item.is-active) { border-right: 3px solid #1677ff; }
:deep(.el-menu-item:hover) { background: #1e293b !important; }
</style>
