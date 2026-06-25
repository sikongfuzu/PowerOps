package com.example.powerops.service;

import com.example.powerops.dto.ShopRequest;
import com.example.powerops.vo.ShopVO;

import java.util.Map;

/**
 * 商铺Service
 */
public interface ShopService {

    /**
     * 分页查询商铺列表
     */
    Map<String, Object> listShops(String shopName, String building, String businessType, Integer status, int pageNum, int pageSize);

    /**
     * 根据ID查询商铺
     */
    ShopVO getShopById(Long shopId);

    /**
     * 新增商铺
     */
    void createShop(ShopRequest request);

    /**
     * 更新商铺
     */
    void updateShop(ShopRequest request);

    /**
     * 删除商铺
     */
    void deleteShop(Long shopId);
}
