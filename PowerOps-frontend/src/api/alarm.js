import request from '@/utils/request'

/** 分页查询报警列表 GET /api/alarm/list */
export function getAlarmList(params) {
  return request({ url: '/alarm/list', method: 'get', params })
}

/** 根据ID查询报警 GET /api/alarm/{id} */
export function getAlarmDetail(alarmId) {
  return request({ url: `/alarm/${alarmId}`, method: 'get' })
}

/** 确认报警 PUT /api/alarm/{id}/confirm?userId= */
export function confirmAlarm(alarmId, userId) {
  return request({ url: `/alarm/${alarmId}/confirm`, method: 'put', params: { userId } })
}

/** 处理报警 PUT /api/alarm/{id}/handle */
export function handleAlarm(alarmId, userId) {
  return request({ url: `/alarm/${alarmId}/handle`, method: 'put', params: { userId } })
}

/** 新增报警（后端自动生成工单） POST /api/alarm */
export function createAlarm(data) {
  return request({ url: '/alarm', method: 'post', data })
}

/** 按等级查询（复用列表接口） */
export function getAlarmsByLevel(level) {
  return request({ url: '/alarm/list', method: 'get', params: { alarmLevel: level, pageNum: 1, pageSize: 999 } })
}

/** 按状态查询（复用列表接口） */
export function getAlarmsByStatus(status) {
  return request({ url: '/alarm/list', method: 'get', params: { status, pageNum: 1, pageSize: 999 } })
}

/** 获取未处理报警数量（取列表 total） */
export function getUnhandledAlarmCount() {
  return request({ url: '/alarm/list', method: 'get', params: { status: '未处理', pageNum: 1, pageSize: 1 } })
}
