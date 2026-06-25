package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.entity.Alarm;
import com.example.powerops.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 报警管理 Controller
 * 报警流程：创建(未处理) → 确认(已确认) → 处理(已解决)
 */
@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * 分页查询报警列表，支持设备/类型/等级/状态筛选
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listAlarms(
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String alarmLevel,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(alarmService.listAlarms(deviceId, alarmType, alarmLevel, status, pageNum, pageSize));
    }

    /**
     * 根据ID查询报警详情
     */
    @GetMapping("/{id}")
    public Result<?> getAlarmById(@PathVariable Long id) {
        return Result.success(alarmService.getAlarmById(id));
    }

    /**
     * 新增报警（初始状态为"未处理"）
     */
    @PostMapping
    public Result<?> createAlarm(@RequestBody Alarm alarm) {
        alarmService.createAlarm(alarm);
        return Result.success();
    }

    /**
     * 确认报警：未处理 → 已确认
     * @param userId 确认人ID
     */
    @PutMapping("/{id}/confirm")
    public Result<?> confirmAlarm(@PathVariable Long id, @RequestParam Long userId) {
        alarmService.confirmAlarm(id, userId);
        return Result.success();
    }

    /**
     * 处理报警：已确认 → 已解决
     * @param userId 处理人ID
     */
    @PutMapping("/{id}/handle")
    public Result<?> handleAlarm(@PathVariable Long id, @RequestParam Long userId) {
        alarmService.handleAlarm(id, userId);
        return Result.success();
    }
}
