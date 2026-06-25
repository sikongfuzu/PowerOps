package com.example.powerops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 商铺新增/修改请求 DTO
 */
@Data
public class ShopRequest {
    private Long shopId;

    @NotBlank(message = "商铺名称不能为空")
    private String shopName;

    private String shopNo;

    private String building;

    private String floorNo;

    private String businessType;

    private String contactPerson;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String contactPhone;

    /** 状态：0-停用, 1-营业中(默认), 2-装修中 */
    private Integer status = 1;
}
