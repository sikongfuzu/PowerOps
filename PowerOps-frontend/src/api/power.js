import request from '@/utils/request'

/** 按商铺查询用电记录 GET /api/power/shop/{shopId}?startDate=&endDate= */
export function getPowerRecords(shopId, params) {
  return request({ url: `/power/shop/${shopId}`, method: 'get', params })
}

/** 新增用电记录 POST /api/power */
export function createPowerRecord(data) {
  return request({ url: '/power', method: 'post', data })
}
