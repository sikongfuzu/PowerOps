package com.example.powerops.service.impl;

import com.example.powerops.common.BusinessException;
import com.example.powerops.common.Code;
import com.example.powerops.dto.ShopRequest;
import com.example.powerops.entity.Shop;
import com.example.powerops.mapper.ShopMapper;
import com.example.powerops.service.ShopService;
import com.example.powerops.vo.ShopVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商铺Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopMapper shopMapper;

    @Override
    public Map<String, Object> listShops(String shopName, String building, String businessType, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        
        List<Shop> shops = shopMapper.listShops(shopName, building, businessType, status, offset, pageSize);
        long total = shopMapper.countShops(shopName, building, businessType, status);

        List<ShopVO> shopVOList = shops.stream().map(this::convertToVO).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("list", shopVOList);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public ShopVO getShopById(Long shopId) {
        Shop shop = shopMapper.findById(shopId);
        if (shop == null) {
            throw new BusinessException(Code.NOT_FOUND, "商铺不存在");
        }
        return convertToVO(shop);
    }

    @Override
    @Transactional
    public void createShop(ShopRequest request) {
        Shop shop = new Shop();
        BeanUtils.copyProperties(request, shop);
        
        int rows = shopMapper.insert(shop);
        if (rows == 0) {
            throw new BusinessException("创建商铺失败");
        }

        log.info("创建商铺成功: {}", request.getShopName());
    }

    @Override
    @Transactional
    public void updateShop(ShopRequest request) {
        if (request.getShopId() == null) {
            throw new BusinessException("商铺ID不能为空");
        }

        Shop existShop = shopMapper.findById(request.getShopId());
        if (existShop == null) {
            throw new BusinessException(Code.NOT_FOUND, "商铺不存在");
        }

        Shop shop = new Shop();
        BeanUtils.copyProperties(request, shop);
        
        int rows = shopMapper.update(shop);
        if (rows == 0) {
            throw new BusinessException("更新商铺失败");
        }

        log.info("更新商铺成功: {}", request.getShopName());
    }

    @Override
    @Transactional
    public void deleteShop(Long shopId) {
        Shop shop = shopMapper.findById(shopId);
        if (shop == null) {
            throw new BusinessException(Code.NOT_FOUND, "商铺不存在");
        }

        int rows = shopMapper.deleteById(shopId);
        if (rows == 0) {
            throw new BusinessException("删除商铺失败");
        }

        log.info("删除商铺成功: {}", shopId);
    }

    private ShopVO convertToVO(Shop shop) {
        ShopVO vo = new ShopVO();
        BeanUtils.copyProperties(shop, vo);
        return vo;
    }
}
