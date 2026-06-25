<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>工单管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">创建工单</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="待处理" value="待处理" />
          <el-option label="处理中" value="处理中" />
          <el-option label="已完成" value="已完成" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级">
        <el-select v-model="searchForm.priority" placeholder="请选择" clearable style="width: 110px">
          <el-option label="紧急" value="紧急" />
          <el-option label="高" value="高" />
          <el-option label="普通" value="普通" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="orderNo" label="工单编号" width="160" />
      <el-table-column label="工单类型" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.orderType === '抢修'" type="danger">{{ row.orderType }}</el-tag>
          <el-tag v-else type="primary">{{ row.orderType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column label="优先级" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.priority === '紧急'" type="danger" effect="dark">紧急</el-tag>
          <el-tag v-else-if="row.priority === '高'" type="warning">高</el-tag>
          <el-tag v-else type="info">普通</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag v-if="row.status === '待处理'" type="danger">待处理</el-tag>
          <el-tag v-else-if="row.status === '处理中'" type="warning">处理中</el-tag>
          <el-tag v-else type="success">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column prop="finishTime" label="完成时间" width="170" />
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="{ row }">
          <el-button v-if="row.status === '待处理'" type="warning" size="small" @click="handleAccept(row)">接单</el-button>
          <el-button v-if="row.status === '处理中'" type="success" size="small" @click="handleComplete(row)">完成</el-button>
          <el-button v-if="row.status === '已完成'" type="info" size="small" disabled>已完成</el-button>
          <el-button type="danger" size="small" icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" class="pagination" @current-change="fetchData" @size-change="fetchData" />
  </el-card>

  <el-dialog v-model="dialogVisible" title="创建工单" width="550px" @closed="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="工单类型" prop="orderType">
        <el-select v-model="form.orderType" placeholder="请选择" style="width: 100%">
          <el-option label="维修" value="维修" />
          <el-option label="抢修" value="抢修" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="form.priority" placeholder="请选择" style="width: 100%">
          <el-option label="普通" value="普通" />
          <el-option label="高" value="高" />
          <el-option label="紧急" value="紧急" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input-number v-model="form.deviceId" :min="0" style="width: 100%" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入工单描述" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确认创建</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getWorkOrderList, createWorkOrder, assignWorkOrder, completeWorkOrder, deleteWorkOrder } from '@/api/workOrder'

const loading = ref(false)
const searchForm = reactive({ status: '', priority: '' })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ orderType: '', priority: '普通', deviceId: 0, description: '' })

const rules = {
  orderType: [{ required: true, message: '请选择工单类型', trigger: 'change' }]
}

const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const currentUserId = userInfo.userId || 1

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getWorkOrderList({
      status: searchForm.status || undefined,
      priority: searchForm.priority || undefined,
      pageNum: pagination.pageNum, pageSize: pagination.pageSize
    })
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.status = ''; searchForm.priority = ''; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.orderType = ''; form.priority = '普通'; form.deviceId = 0; form.description = ''
  formRef.value?.resetFields()
}

const handleAdd = () => { resetForm(); dialogVisible.value = true }

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      await createWorkOrder({ ...form, applicantId: currentUserId })
      ElMessage.success('创建成功')
      dialogVisible.value = false; fetchData()
    } catch (e) { ElMessage.error('创建失败') }
    finally { submitLoading.value = false }
  })
}

const handleAccept = async (row) => {
  try {
    await assignWorkOrder(row.orderId, currentUserId)
    ElMessage.success('已接单')
    fetchData()
  } catch (e) { ElMessage.error('接单失败') }
}

const handleComplete = async (row) => {
  try {
    await completeWorkOrder(row.orderId)
    ElMessage.success('工单已完成')
    fetchData()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除工单「${row.orderNo}」？`, '提示', { type: 'warning' })
    await deleteWorkOrder(row.orderId)
    ElMessage.success('删除成功'); fetchData()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(fetchData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
