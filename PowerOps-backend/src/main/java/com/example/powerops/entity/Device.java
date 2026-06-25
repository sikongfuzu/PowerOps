package com.example.powerops.entity;

import lombok.Data;

/**
 * 设备实体，对应 DEVICE 表
 * status: 运行 / 故障 / 检修
 */
@Data
public class Device {
    /** 设备ID（主键，自增） */
    private Long deviceId;

    /** 设备编号，如 D001 */
    private String deviceNo;

    /** 设备名称 */
    private String deviceName;

    /** 设备类型：变压器 / 配电柜 / 断路器 / 智能电表 / 漏电保护器 */
    private String deviceType;

    /** 所属商铺ID，关联 SHOP.SHOP_ID */
    private Long shopId;

    /** 设备状态：运行 / 故障 / 检修 */
    private String status;
}
