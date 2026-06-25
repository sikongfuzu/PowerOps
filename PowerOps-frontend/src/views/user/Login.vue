<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <el-icon :size="48" color="#1677ff"><Monitor /></el-icon>
        <h2>PowerOps</h2>
        <p>商铺配电运维管理系统</p>
      </div>

      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" size="large" prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="login-btn" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-tips">
        <p>默认账号：admin / admin123</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码长度至少6位', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await login(loginForm)
      localStorage.setItem('token', res.token || '')
      localStorage.setItem('userInfo', JSON.stringify({
        userId: res.userId,
        username: res.username,
        roleCode: res.roleCode,
        realName: res.realName
      }))
      ElMessage.success('登录成功')
      router.push('/')
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  position: relative;
  overflow: hidden;
}
.login-container::before {
  content: '';
  position: absolute;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(22, 119, 255, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}
@keyframes gridMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}
.login-box {
  width: 420px;
  padding: 40px;
  background: rgba(30, 41, 59, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  border: 1px solid #334155;
  z-index: 1;
}
.login-header { text-align: center; margin-bottom: 40px; }
.login-header h2 { color: #f1f5f9; font-size: 28px; margin: 16px 0 8px; font-weight: 600; }
.login-header p { color: #94a3b8; font-size: 14px; }
.login-form { margin-top: 30px; }
.login-btn { width: 100%; height: 44px; font-size: 16px; font-weight: 500; background: #1677ff; border: none; transition: all 0.3s; }
.login-btn:hover { background: #4096ff; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(22, 119, 255, 0.4); }
.login-tips { margin-top: 20px; text-align: center; color: #64748b; font-size: 13px; }
:deep(.el-input__wrapper) { background: #1e293b; border: 1px solid #334155; box-shadow: none; }
:deep(.el-input__wrapper:hover), :deep(.el-input__wrapper.is-focus) { border-color: #1677ff; }
:deep(.el-input__inner) { color: #f1f5f9; }
</style>
