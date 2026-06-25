<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>用户管理</span>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增用户</el-button>
      </div>
    </template>

    <el-form :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="searchForm.roleCode" placeholder="请选择角色" clearable style="width: 140px">
          <el-option label="管理员" value="ADMIN" />
          <el-option label="运维人员" value="OPERATOR" />
          <el-option label="商户用户" value="MERCHANT" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="userId" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="真实姓名" width="120" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column label="角色" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.roleCode === 'ADMIN'" type="danger">管理员</el-tag>
          <el-tag v-else-if="row.roleCode === 'OPERATOR'" type="warning">运维人员</el-tag>
          <el-tag v-else type="info">商户用户</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
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
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="真实姓名" prop="realName">
            <el-input v-model="form.realName" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" :placeholder="isEdit ? '留空则不修改' : '请输入密码'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="角色" prop="roleCode">
        <el-select v-model="form.roleCode" placeholder="请选择角色" style="width: 100%">
          <el-option label="管理员" value="ADMIN" />
          <el-option label="运维人员" value="OPERATOR" />
          <el-option label="商户用户" value="MERCHANT" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
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
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'

const loading = ref(false)
const searchForm = reactive({ username: '', roleCode: '' })
const tableData = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const editingId = ref(null)

const form = reactive({ username: '', password: '', realName: '', phone: '', roleCode: '', status: 1 })

const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码长度至少6位', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请选择角色', trigger: 'change' }]
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      username: searchForm.username || undefined,
      roleCode: searchForm.roleCode || undefined,
      pageNum: pagination.pageNum, pageSize: pagination.pageSize
    })
    tableData.value = res.list || []
    pagination.total = res.total || 0
  } catch (e) { tableData.value = []; pagination.total = 0 }
  finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNum = 1; fetchData() }
const handleReset = () => { searchForm.username = ''; searchForm.roleCode = ''; pagination.pageNum = 1; fetchData() }

const resetForm = () => {
  form.username = ''; form.password = ''; form.realName = ''; form.phone = ''; form.roleCode = ''; form.status = 1
  editingId.value = null; formRef.value?.resetFields()
}

const handleAdd = () => {
  isEdit.value = false; dialogTitle.value = '新增用户'; resetForm()
  rules.password = [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码长度至少6位', trigger: 'blur' }]
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑用户'; editingId.value = row.userId
  Object.assign(form, {
    username: row.username, realName: row.realName, phone: row.phone, roleCode: row.roleCode, status: row.status, password: ''
  })
  rules.password = [] // 编辑时密码可选
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateUser(editingId.value, form)
        ElMessage.success('更新成功')
      } else {
        await createUser(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false; fetchData()
    } catch (e) { ElMessage.error(isEdit.value ? '更新失败' : '新增失败') }
    finally { submitLoading.value = false }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除用户「${row.username}」？`, '提示', { type: 'warning' })
    await deleteUser(row.userId)
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
