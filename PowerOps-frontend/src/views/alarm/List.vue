<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>报警中心</span>
        <el-tag v-if="unhandledCount > 0" type="danger" effect="dark">未处理: {{ unhandledCount }}</el-tag>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="报警等级">
        <el-select v-model="searchForm.alarmLevel" placeholder="请选择" clearable style="width: 110px">
          <el-option label="紧急" value="紧急" />
          <el-option label="严重" value="严重" />
          <el-option label="一般" value="一般" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="未处理" value="未处理" />
          <el-option label="已确认" value="已确认" />
          <el-option label="已解决" value="已解决" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="alarmId" label="ID" width="60" />
      <el-table-column prop="deviceId" label="设备ID" width="80" />
      <el-table-column prop="alarmType" label="报警类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ row.alarmType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="报警等级" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.alarmLevel === '紧急'" type="danger" effect="dark">紧急</el-tag>
          <el-tag v-else-if="row.alarmLevel === '严重'" type="warning">严重</el-tag>
          <el-tag v-else type="info">一般</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="alarmTime" label="报警时间" width="170" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag v-if="row.status === '未处理'" type="danger">未处理</el-tag>
          <el-tag v-else-if="row.status === '已确认'" type="warning">已确认</el-tag>
          <el-tag v-else type="success">已解决</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="220">
        <template #default="{ row }">
          <el-button v-if="row.status === '未处理'" type="warning" size="small" @click="handleConfirm(row)">确认</el-button>
          <el-button v-if="row.status === '已确认'" type="success" size="small" @click="handleHandle(row)">处理</el-button>
          <el-button v-if="row.status === '已解决'" type="info" size="small" disabled>已处理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" class="pagination" @current-change="fetchData" @size-change="fetchData" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAlarmList, confirmAlarm, handleAlarm, getUnhandledAlarmCount } from '@/api/alarm'

const loading = ref(false)
const searchForm = reactive({ alarmLevel: '', status: '' })
const tableData = ref([])
const unhandledCount = ref(0)
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const currentUserId = userInfo.userId || 1

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAlarmList({
      alarmLevel: searchForm.alarmLevel || undefined,
      status: searchForm.status || undefined,
      pageNum: pagination.pageNum, pageSize: pagination.pageSize
    })
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const fetchUnhandledCount = async () => {
  try {
    const res = await getUnhandledAlarmCount()
    unhandledCount.value = res.total || 0
  } catch (e) { /* ignore */ }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.alarmLevel = ''; searchForm.status = ''; pagination.pageNum = 1; fetchData() }

const handleConfirm = async (row) => {
  try {
    await confirmAlarm(row.alarmId, currentUserId)
    ElMessage.success('已确认报警')
    fetchData()
    fetchUnhandledCount()
  } catch (e) { ElMessage.error('确认失败') }
}

const handleHandle = async (row) => {
  try {
    await handleAlarm(row.alarmId, currentUserId)
    ElMessage.success('已处理报警')
    fetchData()
    fetchUnhandledCount()
  } catch (e) { ElMessage.error('处理失败') }
}

onMounted(() => {
  fetchData()
  fetchUnhandledCount()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
