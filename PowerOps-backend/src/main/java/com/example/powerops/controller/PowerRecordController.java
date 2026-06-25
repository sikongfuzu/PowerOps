package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.entity.PowerRecord;
import com.example.powerops.service.PowerRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用电记录 Controller
 */
@RestController
@RequestMapping("/api/power")
@RequiredArgsConstructor
public class PowerRecordController {
    private final PowerRecordService powerRecordService;

    /**
     * 新增用电记录
     */
    @PostMapping
    public Result<?> createRecord(@RequestBody PowerRecord record) {
        powerRecordService.createRecord(record);
        return Result.success();
    }

    /**
     * 分页查询全部用电记录，支持日期范围筛选
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listAll(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(powerRecordService.listAll(startDate, endDate, pageNum, pageSize));
    }

    /**
     * 按商铺查询用电记录，支持日期范围筛选
     */
    @GetMapping("/shop/{shopId}")
    public Result<List<PowerRecord>> listByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(powerRecordService.listByShopId(shopId, startDate, endDate));
    }
}
