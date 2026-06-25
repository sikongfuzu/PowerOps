import axios from 'axios'
import { ElMessage } from 'element-plus'

/**
 * Axios 实例 — 统一封装 HTTP 请求
 * 通过 Vite 代理转发到后端，避免跨域
 */
const request = axios.create({
  baseURL: '/api',           // 代理前缀，Vite 转发到 http://localhost:8080
  timeout: 10000             // 10s 超时
})

// 请求拦截器：自动携带 Token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：解包统一返回格式 { code, message, data }
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端统一返回 Result，业务 code 非 200 视为失败
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data          // 直接返回 data 字段，调用方拿到业务数据
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
