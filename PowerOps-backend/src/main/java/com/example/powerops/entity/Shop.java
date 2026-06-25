package com.example.powerops.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商铺实体，对应 SHOP 表
 *
 * 为配电运维的基本管理单元，每个商铺关联若干设备、用电记录、报警和工单。
 * status: 0-停用, 1-营业中, 2-装修中
 */
@Data
public class Shop {
    /** 商铺ID（主键，自增） */
    private Long shopId;

    /** 商铺编号，如 S001 */
    private String shopNo;

    /** 商铺名称 */
    private String shopName;

    /** 所属楼栋 */
    private String building;

    /** 所在楼层 */
    private String floorNo;

    /** 业态类型：餐饮 / 零售 / 服务 */
    private String businessType;

    /** 联系人姓名 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 商铺状态：0-停用, 1-营业中, 2-装修中 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
