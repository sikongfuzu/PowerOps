package com.example.powerops.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用电记录实体，对应 POWER_RECORD 表
 *
 * 记录每个商铺每日的用电量。简化版去掉了电表关联和起止读数，只保留单一用电量字段。
 * 4 个字段：recordId, shopId, recordDate, consumption
 */
@Data
public class PowerRecord {
    /** 记录ID（主键，自增） */
    private Long recordId;

    /** 所属商铺ID，关联 SHOP.SHOP_ID */
    private Long shopId;

    /** 记录日期（按天粒度） */
    private LocalDate recordDate;

    /** 用电量（kWh） */
    private BigDecimal consumption;
}
