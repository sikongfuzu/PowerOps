<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>用电管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增记录</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="商铺ID">
        <el-input v-model="searchForm.shopId" placeholder="请输入商铺ID" clearable style="width: 140px" />
      </el-form-item>
      <el-form-item label="日期范围">
        <el-date-picker v-model="searchForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="recordId" label="记录ID" width="80" />
      <el-table-column prop="shopId" label="商铺ID" width="80" />
      <el-table-column prop="recordDate" label="日期" width="120" />
      <el-table-column prop="startReading" label="起始读数" width="120" />
      <el-table-column prop="endReading" label="结束读数" width="120" />
      <el-table-column label="用电量(kWh)" width="130">
        <template #default="{ row }">
          {{ ((row.endReading || 0) - (row.startReading || 0)).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="150">
        <template #default="{ row }">
          <el-button type="danger" size="small" icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next" class="pagination" @current-change="fetchData" />
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增用电记录" width="500px" @closed="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="商铺ID" prop="shopId">
        <el-input-number v-model="form.shopId" :min="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="记录日期" prop="recordDate">
        <el-date-picker v-model="form.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
      </el-form-item>
      <el-form-item label="起始读数" prop="startReading">
        <el-input-number v-model="form.startReading" :min="0" :precision="2" :step="10" style="width: 100%" />
      </el-form-item>
      <el-form-item label="结束读数" prop="endReading">
        <el-input-number v-model="form.endReading" :min="0" :precision="2" :step="10" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPowerRecords, createPowerRecord } from '@/api/power'

const loading = ref(false)
const searchForm = reactive({ shopId: '', dateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ shopId: 1, recordDate: '', startReading: 0, endReading: 0 })

const rules = {
  shopId: [{ required: true, message: '请输入商铺ID', trigger: 'blur' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
}

const fetchData = async () => {
  if (!searchForm.shopId) return
  loading.value = true
  try {
    const params = {}
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await getPowerRecords(searchForm.shopId, params)
    tableData.value = Array.isArray(res) ? res : []
    pagination.total = tableData.value.length
  } catch (e) { tableData.value = [] }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.shopId = ''; searchForm.dateRange = []; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.shopId = 1; form.recordDate = ''; form.startReading = 0; form.endReading = 0
  formRef.value?.resetFields()
}

const handleAdd = () => { resetForm(); dialogVisible.value = true }

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      await createPowerRecord(form)
      ElMessage.success('新增成功')
      dialogVisible.value = false
      if (searchForm.shopId) fetchData()
    } catch (e) { ElMessage.error('新增失败') }
    finally { submitLoading.value = false }
  })
}

const handleDelete = async (row) => {
  // Power records don't have a delete API yet
  ElMessage.info('删除功能暂未开放')
}

onMounted(() => {
  searchForm.shopId = 1
  fetchData()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
