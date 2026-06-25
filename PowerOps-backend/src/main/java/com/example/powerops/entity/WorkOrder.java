package com.example.powerops.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工单实体
 * 对应 WORK_ORDER 表，用于设备维修/抢修任务管理
 * orderType: 维修/抢修
 * priority: 普通/高/紧急
 * status: 待处理/处理中/已完成
 * 简化版去掉了 powerOutageRequired/solution 字段
 */
@Data
public class WorkOrder {
    private Long orderId;
    private String orderNo;
    private String orderType;
    private Long deviceId;
    private Long applicantId;
    private Long handlerId;
    private String priority;
    private String description;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}
