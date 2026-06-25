<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>用电管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增记录</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
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
      <el-table-column prop="recordDate" label="日期" width="140" />
      <el-table-column prop="consumption" label="用电量(kWh)" width="130" />
    </el-table>

    <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" class="pagination" @current-change="fetchData" @size-change="fetchData" />
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增用电记录" width="500px" @closed="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="商铺ID" prop="shopId">
        <el-input-number v-model="form.shopId" :min="1" style="width: 100%" />
      </el-form-item>
      <el-form-item label="记录日期" prop="recordDate">
        <el-date-picker v-model="form.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
      </el-form-item>
      <el-form-item label="用电量(kWh)" prop="consumption">
        <el-input-number v-model="form.consumption" :min="0" :precision="2" :step="10" style="width: 100%" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPowerList, createPowerRecord } from '@/api/power'

const loading = ref(false)
const searchForm = reactive({ dateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ shopId: 1, recordDate: '', consumption: 0 })

const rules = {
  shopId: [{ required: true, message: '请输入商铺ID', trigger: 'blur' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  consumption: [{ required: true, message: '请输入用电量', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { pageNum: pagination.pageNum, pageSize: pagination.pageSize }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await getPowerList(params)
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.dateRange = []; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.shopId = 1; form.recordDate = ''; form.consumption = 0
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
      fetchData()
    } catch (e) { ElMessage.error('新增失败') }
    finally { submitLoading.value = false }
  })
}

onMounted(fetchData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
