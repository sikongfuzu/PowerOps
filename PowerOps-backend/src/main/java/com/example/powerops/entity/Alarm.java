package com.example.powerops.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 报警实体
 * 对应 ALARM 表，记录设备异常报警
 * alarmLevel: 一般/严重/紧急
 * alarmType: 过压/欠压/过流/过载/漏电/温度过高
 * status: 未处理/已确认/已解决
 */
@Data
public class Alarm {
    private Long alarmId;
    private Long deviceId;
    private String alarmType;
    private String alarmLevel;
    private LocalDateTime alarmTime;
    private String status;
    private Long handlerId;
    private LocalDateTime handleTime;
}
