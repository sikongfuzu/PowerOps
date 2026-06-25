import request from '@/utils/request'

/** 用户登录 POST /api/user/login */
export function login(data) {
  return request({ url: '/user/login', method: 'post', data })
}

/** 分页查询用户列表 GET /api/user/list */
export function getUserList(params) {
  return request({ url: '/user/list', method: 'get', params })
}

/** 根据ID查询用户 GET /api/user/{id} */
export function getUserById(id) {
  return request({ url: `/user/${id}`, method: 'get' })
}

/** 新增用户 POST /api/user */
export function createUser(data) {
  return request({ url: '/user', method: 'post', data })
}

/** 更新用户 PUT /api/user/{id} */
export function updateUser(id, data) {
  return request({ url: `/user/${id}`, method: 'put', data })
}

/** 删除用户 DELETE /api/user/{id} */
export function deleteUser(id) {
  return request({ url: `/user/${id}`, method: 'delete' })
}
