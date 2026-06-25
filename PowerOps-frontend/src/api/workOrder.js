import request from '@/utils/request'

/** 分页查询工单列表 GET /api/workorder/list */
export function getWorkOrderList(params) {
  return request({ url: '/workorder/list', method: 'get', params })
}

/** 根据ID查询工单 GET /api/workorder/{id} */
export function getWorkOrderDetail(orderId) {
  return request({ url: `/workorder/${orderId}`, method: 'get' })
}

/** 创建工单 POST /api/workorder */
export function createWorkOrder(data) {
  return request({ url: '/workorder', method: 'post', data })
}

/** 指派工单 PUT /api/workorder/{id}/assign?handlerId= */
export function assignWorkOrder(orderId, handlerId) {
  return request({ url: `/workorder/${orderId}/assign`, method: 'put', params: { handlerId } })
}

/** 更新工单状态 PUT /api/workorder/{id}/status?status= */
export function updateWorkOrderStatus(orderId, status) {
  return request({ url: `/workorder/${orderId}/status`, method: 'put', params: { status } })
}

/** 完成工单 PUT /api/workorder/{id}/complete */
export function completeWorkOrder(orderId) {
  return request({ url: `/workorder/${orderId}/complete`, method: 'put' })
}

/** 按状态查询（复用列表接口） */
export function getWorkOrdersByStatus(status) {
  return request({ url: '/workorder/list', method: 'get', params: { status, pageNum: 1, pageSize: 999 } })
}

/** 按优先级查询（复用列表接口） */
export function getWorkOrdersByPriority(priority) {
  return request({ url: '/workorder/list', method: 'get', params: { priority, pageNum: 1, pageSize: 999 } })
}

/** 删除工单 DELETE /api/workorder/{id} */
export function deleteWorkOrder(orderId) {
  return request({ url: `/workorder/${orderId}`, method: 'delete' })
}

/** 获取待处理工单数量（取列表 total） */
export function getPendingWorkOrderCount() {
  return request({ url: '/workorder/list', method: 'get', params: { status: '待处理', pageNum: 1, pageSize: 1 } })
}
