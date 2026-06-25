package com.example.powerops.service;

import com.example.powerops.entity.Alarm;

import java.util.Map;

/**
 * 报警管理 Service 接口
 * 流程：createAlarm(未处理) → confirmAlarm(已确认) → handleAlarm(已解决)
 * 简化版：不再自动生成关联工单，handleAlarm 无需 handleResult
 */
public interface AlarmService {

    /** 分页查询报警列表 */
    Map<String, Object> listAlarms(Long deviceId, String alarmType, String alarmLevel, String status, int pageNum, int pageSize);

    /** 根据ID查询报警 */
    Alarm getAlarmById(Long alarmId);

    /** 新增报警（初始状态"未处理"） */
    void createAlarm(Alarm alarm);

    /** 确认报警：未处理 → 已确认 */
    void confirmAlarm(Long alarmId, Long userId);

    /** 处理报警：已确认 → 已解决 */
    void handleAlarm(Long alarmId, Long userId);
}
