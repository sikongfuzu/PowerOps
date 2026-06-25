<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>商铺管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增商铺</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="商铺名称">
        <el-input v-model="searchForm.shopName" placeholder="请输入名称" clearable />
      </el-form-item>
      <el-form-item label="楼栋">
        <el-input v-model="searchForm.building" placeholder="如A栋" clearable style="width: 100px" />
      </el-form-item>
      <el-form-item label="业态类型">
        <el-select v-model="searchForm.businessType" placeholder="请选择" clearable style="width: 110px">
          <el-option label="餐饮" value="餐饮" />
          <el-option label="零售" value="零售" />
          <el-option label="服务" value="服务" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="shopId" label="ID" width="60" />
      <el-table-column prop="shopNo" label="编号" width="80" />
      <el-table-column prop="shopName" label="商铺名称" width="150" />
      <el-table-column prop="building" label="楼栋" width="80" />
      <el-table-column prop="floorNo" label="楼层" width="80" />
      <el-table-column prop="businessType" label="业态" width="80" />
      <el-table-column prop="contactPerson" label="联系人" width="100" />
      <el-table-column prop="contactPhone" label="联系电话" width="140" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success">营业中</el-tag>
          <el-tag v-else-if="row.status === 0" type="danger">停用</el-tag>
          <el-tag v-else type="warning">装修中</el-tag>
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

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px" @closed="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="商铺编号" prop="shopNo">
            <el-input v-model="form.shopNo" placeholder="如S001" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="商铺名称" prop="shopName">
            <el-input v-model="form.shopName" placeholder="请输入名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="楼栋" prop="building">
            <el-input v-model="form.building" placeholder="如A栋" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="楼层" prop="floorNo">
            <el-input v-model="form.floorNo" placeholder="如1层" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="业态类型" prop="businessType">
            <el-select v-model="form.businessType" placeholder="请选择" style="width: 100%">
              <el-option label="餐饮" value="餐饮" />
              <el-option label="零售" value="零售" />
              <el-option label="服务" value="服务" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :value="1">营业中</el-radio>
              <el-radio :value="0">停用</el-radio>
              <el-radio :value="2">装修中</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="联系人" prop="contactPerson">
        <el-input v-model="form.contactPerson" placeholder="请输入联系人" style="width: 240px" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="form.contactPhone" placeholder="请输入手机号" style="width: 240px" />
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
import { getShopList, createShop, updateShop, deleteShop } from '@/api/shop'

const loading = ref(false)
const searchForm = reactive({ shopName: '', building: '', businessType: '' })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const editingId = ref(null)

const form = reactive({ shopNo: '', shopName: '', building: '', floorNo: '', businessType: '', contactPerson: '', contactPhone: '', status: 1 })

const rules = {
  shopName: [{ required: true, message: '请输入商铺名称', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getShopList({
      shopName: searchForm.shopName || undefined,
      building: searchForm.building || undefined,
      businessType: searchForm.businessType || undefined,
      pageNum: pagination.pageNum, pageSize: pagination.pageSize
    })
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.shopName = ''; searchForm.building = ''; searchForm.businessType = ''; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.shopNo = ''; form.shopName = ''; form.building = ''; form.floorNo = ''; form.businessType = ''; form.contactPerson = ''; form.contactPhone = ''; form.status = 1
  editingId.value = null; formRef.value?.resetFields()
}

const handleAdd = () => { isEdit.value = false; dialogTitle.value = '新增商铺'; resetForm(); dialogVisible.value = true }

const handleEdit = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑商铺'; editingId.value = row.shopId
  Object.assign(form, {
    shopNo: row.shopNo, shopName: row.shopName, building: row.building, floorNo: row.floorNo,
    businessType: row.businessType, contactPerson: row.contactPerson, contactPhone: row.contactPhone, status: row.status
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
        await updateShop(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createShop(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false; fetchData()
    } catch (e) { ElMessage.error(isEdit.value ? '更新失败' : '新增失败') }
    finally { submitLoading.value = false }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除商铺「${row.shopName}」？`, '提示', { type: 'warning' })
    await deleteShop(row.shopId)
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
