package com.example.powerops.mapper;

import com.example.powerops.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备Mapper
 */
@Mapper
public interface DeviceMapper {

    /**
     * 根据ID查询设备
     */
    Device findById(@Param("deviceId") Long deviceId);

    /**
     * 查询设备列表（分页）
     */
    List<Device> listDevices(@Param("deviceName") String deviceName,
                            @Param("deviceType") String deviceType,
                            @Param("shopId") Long shopId,
                            @Param("status") String status,
                            @Param("offset") int offset,
                            @Param("limit") int limit);

    /**
     * 查询设备总数
     */
    long countDevices(@Param("deviceName") String deviceName,
                     @Param("deviceType") String deviceType,
                     @Param("shopId") Long shopId,
                     @Param("status") String status);

    /**
     * 根据商铺ID查询设备列表
     */
    List<Device> listByShopId(@Param("shopId") Long shopId);

    /**
     * 新增设备
     */
    int insert(Device device);

    /**
     * 更新设备
     */
    int update(Device device);

    /**
     * 删除设备
     */
    int deleteById(@Param("deviceId") Long deviceId);
}
