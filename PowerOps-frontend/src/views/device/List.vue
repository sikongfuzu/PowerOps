<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>设备管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增设备</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="设备名称">
        <el-input v-model="searchForm.deviceName" placeholder="请输入名称" clearable />
      </el-form-item>
      <el-form-item label="设备类型">
        <el-select v-model="searchForm.deviceType" placeholder="请选择" clearable style="width: 130px">
          <el-option label="变压器" value="变压器" />
          <el-option label="配电柜" value="配电柜" />
          <el-option label="智能电表" value="智能电表" />
          <el-option label="断路器" value="断路器" />
          <el-option label="漏电保护器" value="漏电保护器" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="deviceId" label="ID" width="60" />
      <el-table-column prop="deviceNo" label="编号" width="100" />
      <el-table-column prop="deviceName" label="设备名称" width="150" />
      <el-table-column prop="deviceType" label="设备类型" width="110" />
      <el-table-column prop="shopId" label="商铺ID" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === '运行'" type="success">运行</el-tag>
          <el-tag v-else-if="row.status === '故障'" type="danger">故障</el-tag>
          <el-tag v-else type="warning">检修</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" class="pagination" @current-change="fetchData" @size-change="fetchData" />
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @closed="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="设备编号" prop="deviceNo">
            <el-input v-model="form.deviceNo" placeholder="如D001" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备名称" prop="deviceName">
            <el-input v-model="form.deviceName" placeholder="请输入名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="设备类型" prop="deviceType">
            <el-select v-model="form.deviceType" placeholder="请选择" style="width: 100%">
              <el-option label="变压器" value="变压器" />
              <el-option label="配电柜" value="配电柜" />
              <el-option label="智能电表" value="智能电表" />
              <el-option label="断路器" value="断路器" />
              <el-option label="漏电保护器" value="漏电保护器" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属商铺" prop="shopId">
            <el-select v-model="form.shopId" placeholder="请选择商铺" filterable style="width: 100%">
              <el-option v-for="shop in shopOptions" :key="shop.shopId" :label="shop.shopName" :value="shop.shopId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio value="运行">运行</el-radio>
          <el-radio value="故障">故障</el-radio>
          <el-radio value="检修">检修</el-radio>
        </el-radio-group>
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
import { getDeviceList, createDevice, updateDevice, deleteDevice } from '@/api/device'
import { getShopList } from '@/api/shop'

const loading = ref(false)
const shopOptions = ref([])
const searchForm = reactive({ deviceName: '', deviceType: '' })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const editingId = ref(null)

const form = reactive({ deviceNo: '', deviceName: '', deviceType: '', shopId: 1, status: '运行' })

const rules = {
  deviceNo: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  deviceType: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
  shopId: [{ required: true, message: '请选择商铺', trigger: 'change' }, { type: 'number', min: 1, message: '商铺ID必须大于0', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getDeviceList({
      deviceName: searchForm.deviceName || undefined,
      deviceType: searchForm.deviceType || undefined,
      pageNum: pagination.pageNum, pageSize: pagination.pageSize
    })
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.deviceName = ''; searchForm.deviceType = ''; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.deviceNo = ''; form.deviceName = ''; form.deviceType = ''; form.shopId = 1; form.status = '运行'
  editingId.value = null; formRef.value?.resetFields()
}

const handleAdd = () => { isEdit.value = false; dialogTitle.value = '新增设备'; resetForm(); dialogVisible.value = true }

const handleEdit = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑设备'; editingId.value = row.deviceId
  Object.assign(form, {
    deviceNo: row.deviceNo, deviceName: row.deviceName, deviceType: row.deviceType, shopId: row.shopId, status: row.status
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateDevice(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createDevice(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false; fetchData()
    } catch (e) { ElMessage.error(isEdit.value ? '更新失败' : '新增失败') }
    finally { submitLoading.value = false }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除设备「${row.deviceName}」？`, '提示', { type: 'warning' })
    await deleteDevice(row.deviceId)
    ElMessage.success('删除成功'); fetchData()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => {
  fetchData()
  getShopList({ pageNum: 1, pageSize: 999 }).then(res => { shopOptions.value = res.list || [] }).catch(() => {})
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
