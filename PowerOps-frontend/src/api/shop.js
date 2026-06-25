import request from '@/utils/request'

/** 分页查询商铺列表 GET /api/shop/list */
export function getShopList(params) {
  return request({ url: '/shop/list', method: 'get', params })
}

/** 根据ID查询商铺 GET /api/shop/{id} */
export function getShopDetail(shopId) {
  return request({ url: `/shop/${shopId}`, method: 'get' })
}

/** 新增商铺 POST /api/shop */
export function createShop(data) {
  return request({ url: '/shop', method: 'post', data })
}

/** 更新商铺 PUT /api/shop/{id} */
export function updateShop(shopId, data) {
  return request({ url: `/shop/${shopId}`, method: 'put', data })
}

/** 删除商铺 DELETE /api/shop/{id} */
export function deleteShop(shopId) {
  return request({ url: `/shop/${shopId}`, method: 'delete' })
}

/** 按楼栋查询（复用列表接口） */
export function getShopsByBuilding(building) {
  return request({ url: '/shop/list', method: 'get', params: { building, pageNum: 1, pageSize: 999 } })
}

/** 按类型查询（复用列表接口） */
export function getShopsByType(type) {
  return request({ url: '/shop/list', method: 'get', params: { businessType: type, pageNum: 1, pageSize: 999 } })
}
