package com.example.powerops.service.impl;

import com.example.powerops.common.BusinessException;
import com.example.powerops.common.Code;
import com.example.powerops.entity.Alarm;
import com.example.powerops.mapper.AlarmMapper;
import com.example.powerops.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警管理 Service 实现
 * 简化版：不再自动生成关联工单，handleAlarm 不需要 handleResult
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmMapper alarmMapper;

    @Override
    public Map<String, Object> listAlarms(Long deviceId, String alarmType, String alarmLevel, String status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Alarm> alarms = alarmMapper.listAlarms(deviceId, alarmType, alarmLevel, status, offset, pageSize);
        long total = alarmMapper.countAlarms(deviceId, alarmType, alarmLevel, status);
        Map<String, Object> result = new HashMap<>();
        result.put("list", alarms);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public Alarm getAlarmById(Long alarmId) {
        Alarm alarm = alarmMapper.findById(alarmId);
        if (alarm == null) throw new BusinessException(Code.NOT_FOUND, "报警记录不存在");
        return alarm;
    }

    @Override
    @Transactional
    public void createAlarm(Alarm alarm) {
        alarm.setStatus("未处理");
        int rows = alarmMapper.insert(alarm);
        if (rows == 0) throw new BusinessException("创建报警失败");
        log.info("创建报警成功，报警ID: {}", alarm.getAlarmId());
    }

    @Override
    @Transactional
    public void confirmAlarm(Long alarmId, Long userId) {
        int rows = alarmMapper.confirmAlarm(alarmId, userId);
        if (rows == 0) throw new BusinessException(Code.NOT_FOUND, "报警记录不存在");
        log.info("确认报警成功，报警ID: {}, 确认人: {}", alarmId, userId);
    }

    @Override
    @Transactional
    public void handleAlarm(Long alarmId, Long userId) {
        int rows = alarmMapper.handleAlarm(alarmId, userId);
        if (rows == 0) throw new BusinessException(Code.NOT_FOUND, "报警记录不存在");
        log.info("处理报警成功，报警ID: {}, 处理人: {}", alarmId, userId);
    }
}
