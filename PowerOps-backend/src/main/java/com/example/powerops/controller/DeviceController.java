package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.dto.DeviceRequest;
import com.example.powerops.entity.Device;
import com.example.powerops.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备管理 Controller
 * 提供设备 CRUD 及状态更新接口
 */
@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 分页查询设备列表
     * @param deviceName 设备名称（模糊）
     * @param deviceType 设备类型
     * @param shopId 所属商铺ID
     * @param status 设备状态
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listDevices(
            @RequestParam(required = false) String deviceName,
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = deviceService.listDevices(deviceName, deviceType, shopId, status, pageNum, pageSize);
        return Result.success(data);
    }

    /**
     * 根据ID查询设备详情
     */
    @GetMapping("/{id}")
    public Result<?> getDeviceById(@PathVariable Long id) {
        return Result.success(deviceService.getDeviceById(id));
    }

    /**
     * 根据商铺ID查询该商铺下的所有设备
     */
    @GetMapping("/shop/{shopId}")
    public Result<List<Device>> listDevicesByShopId(@PathVariable Long shopId) {
        return Result.success(deviceService.listDevicesByShopId(shopId));
    }

    /**
     * 新增设备（初始状态默认"运行"）
     */
    @PostMapping
    public Result<?> createDevice(@Valid @RequestBody DeviceRequest request) {
        deviceService.createDevice(request);
        return Result.success();
    }

    /**
     * 修改设备信息
     */
    @PutMapping("/{id}")
    public Result<?> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceRequest request) {
        request.setDeviceId(id);
        deviceService.updateDevice(request);
        return Result.success();
    }

    /**
     * 更新设备状态（运行/故障/检修）
     */
    @PutMapping("/{id}/status")
    public Result<?> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) {
        deviceService.updateDeviceStatus(id, status);
        return Result.success();
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return Result.success();
    }
}
