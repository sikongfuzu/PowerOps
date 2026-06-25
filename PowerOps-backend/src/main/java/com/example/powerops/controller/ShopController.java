package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.dto.ShopRequest;
import com.example.powerops.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商铺管理 Controller
 * 提供商铺 CRUD 接口
 */
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /**
     * 分页查询商铺列表，支持名称模糊/楼栋/类型/状态筛选
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listShops(
            @RequestParam(required = false) String shopName,
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = shopService.listShops(shopName, building, businessType, status, pageNum, pageSize);
        return Result.success(data);
    }

    /**
     * 根据ID查询商铺详情
     */
    @GetMapping("/{id}")
    public Result<?> getShopById(@PathVariable Long id) {
        return Result.success(shopService.getShopById(id));
    }

    /**
     * 新增商铺
     */
    @PostMapping
    public Result<?> createShop(@Valid @RequestBody ShopRequest request) {
        shopService.createShop(request);
        return Result.success();
    }

    /**
     * 修改商铺信息
     */
    @PutMapping("/{id}")
    public Result<?> updateShop(@PathVariable Long id, @Valid @RequestBody ShopRequest request) {
        request.setShopId(id);
        shopService.updateShop(request);
        return Result.success();
    }

    /**
     * 删除商铺
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return Result.success();
    }
}
