<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <el-icon :size="48" color="#1677ff"><Shop /></el-icon>
            <div class="stat-info">
              <p class="stat-label">商铺数量</p>
              <h3 class="stat-value">{{ stats.shopCount }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <el-icon :size="48" color="#52c41a"><Monitor /></el-icon>
            <div class="stat-info">
              <p class="stat-label">设备数量</p>
              <h3 class="stat-value">{{ stats.deviceCount }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <el-icon :size="48" color="#ff4d4f"><Bell /></el-icon>
            <div class="stat-info">
              <p class="stat-label">未处理报警</p>
              <h3 class="stat-value">{{ stats.alarmCount }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <el-icon :size="48" color="#faad14"><Document /></el-icon>
            <div class="stat-info">
              <p class="stat-label">待处理工单</p>
              <h3 class="stat-value">{{ stats.workOrderCount }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getShopList } from '@/api/shop'
import { getDeviceList } from '@/api/device'
import { getAlarmList } from '@/api/alarm'
import { getWorkOrderList } from '@/api/workOrder'

const stats = reactive({ shopCount: 0, deviceCount: 0, alarmCount: 0, workOrderCount: 0 })

onMounted(async () => {
  try {
    const [shopRes, deviceRes, alarmRes, woRes] = await Promise.all([
      getShopList({ pageNum: 1, pageSize: 1 }),
      getDeviceList({ pageNum: 1, pageSize: 1 }),
      getAlarmList({ status: '未处理', pageNum: 1, pageSize: 1 }),
      getWorkOrderList({ status: '待处理', pageNum: 1, pageSize: 1 })
    ])
    stats.shopCount = shopRes.total || 0
    stats.deviceCount = deviceRes.total || 0
    stats.alarmCount = alarmRes.total || 0
    stats.workOrderCount = woRes.total || 0
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.stats-row { margin-bottom: 20px; }
.stat-card { border-radius: 8px; }
.stat-card:hover { transform: translateY(-4px); transition: all 0.3s; }
.stat-content { display: flex; align-items: center; gap: 20px; }
.stat-info { flex: 1; }
.stat-label { color: #64748b; font-size: 14px; margin: 0 0 8px 0; }
.stat-value { color: #0f172a; font-size: 32px; font-weight: 600; margin: 0; }
</style>
