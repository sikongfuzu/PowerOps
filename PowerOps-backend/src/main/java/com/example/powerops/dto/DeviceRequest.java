package com.example.powerops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备新增/修改请求 DTO
 */
@Data
public class DeviceRequest {
    private Long deviceId;

    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    private String deviceNo;

    @NotBlank(message = "设备类型不能为空")
    private String deviceType;

    @NotNull(message = "商铺ID不能为空")
    private Long shopId;

    private String status;
}
