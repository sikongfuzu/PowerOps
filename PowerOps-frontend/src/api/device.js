import request from '@/utils/request'

/** 分页查询设备列表 GET /api/device/list */
export function getDeviceList(params) {
  return request({ url: '/device/list', method: 'get', params })
}

/** 根据ID查询设备 GET /api/device/{id} */
export function getDeviceDetail(deviceId) {
  return request({ url: `/device/${deviceId}`, method: 'get' })
}

/** 按商铺查询设备列表 GET /api/device/shop/{shopId} */
export function getDevicesByShop(shopId) {
  return request({ url: `/device/shop/${shopId}`, method: 'get' })
}

/** 新增设备 POST /api/device */
export function createDevice(data) {
  return request({ url: '/device', method: 'post', data })
}

/** 更新设备 PUT /api/device/{id} */
export function updateDevice(deviceId, data) {
  return request({ url: `/device/${deviceId}`, method: 'put', data })
}

/** 删除设备 DELETE /api/device/{id} */
export function deleteDevice(deviceId) {
  return request({ url: `/device/${deviceId}`, method: 'delete' })
}

/** 获取全量设备列表（不分页） */
export function getDeviceTree() {
  return request({ url: '/device/list', method: 'get', params: { pageNum: 1, pageSize: 999 } })
}

/** 按类型查询（复用列表接口） */
export function getDevicesByType(deviceType) {
  return request({ url: '/device/list', method: 'get', params: { deviceType, pageNum: 1, pageSize: 999 } })
}

/** 更新设备状态 PUT /api/device/{id}/status */
export function updateDeviceStatus(deviceId, status) {
  return request({ url: `/device/${deviceId}/status`, method: 'put', params: { status } })
}
