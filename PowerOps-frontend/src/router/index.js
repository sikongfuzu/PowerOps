import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layout/MainLayout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      { path: 'dashboard',  name: 'Dashboard',  component: () => import('@/views/dashboard/Index.vue'),  meta: { title: '首页', icon: 'Odometer' } },
      { path: 'user',       name: 'User',       component: () => import('@/views/user/List.vue'),        meta: { title: '用户管理', icon: 'User' } },
      { path: 'shop',       name: 'Shop',       component: () => import('@/views/shop/List.vue'),        meta: { title: '商铺管理', icon: 'Shop' } },
      { path: 'device',     name: 'Device',     component: () => import('@/views/device/List.vue'),      meta: { title: '设备管理', icon: 'Monitor' } },
      { path: 'alarm',      name: 'Alarm',      component: () => import('@/views/alarm/List.vue'),       meta: { title: '报警中心', icon: 'Bell' } },
      { path: 'workorder',  name: 'WorkOrder',  component: () => import('@/views/workorder/List.vue'),   meta: { title: '工单管理', icon: 'Document' } },
      { path: 'power',      name: 'Power',      component: () => import('@/views/power/List.vue'),       meta: { title: '用电管理', icon: 'Lightning' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
