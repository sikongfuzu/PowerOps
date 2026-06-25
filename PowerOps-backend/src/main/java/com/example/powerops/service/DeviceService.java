package com.example.powerops.service;

import com.example.powerops.dto.DeviceRequest;
import com.example.powerops.entity.Device;

import java.util.List;
import java.util.Map;

/**
 * 设备管理 Service 接口
 * 简化版：返回 Device 实体而非 DeviceVO，无树形结构
 */
public interface DeviceService {

    /** 分页查询设备列表，返回 {list, total, pageNum, pageSize} */
    Map<String, Object> listDevices(String deviceName, String deviceType, Long shopId, String status, int pageNum, int pageSize);

    /** 根据ID查询设备 */
    Device getDeviceById(Long deviceId);

    /** 根据商铺ID查询该商铺下所有设备 */
    List<Device> listDevicesByShopId(Long shopId);

    /** 新增设备（初始状态默认"运行"） */
    void createDevice(DeviceRequest request);

    /** 修改设备信息 */
    void updateDevice(DeviceRequest request);

    /** 更新设备状态（运行/故障/检修） */
    void updateDeviceStatus(Long deviceId, String status);

    /** 删除设备 */
    void deleteDevice(Long deviceId);
}
