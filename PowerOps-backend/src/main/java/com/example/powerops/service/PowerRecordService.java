package com.example.powerops.service;

import com.example.powerops.entity.PowerRecord;
import com.example.powerops.mapper.PowerRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PowerRecordService {
    private final PowerRecordMapper powerRecordMapper;

    @Transactional
    public void createRecord(PowerRecord record) {
        powerRecordMapper.insert(record);
    }

    public List<PowerRecord> listByShopId(Long shopId, String startDate, String endDate) {
        return powerRecordMapper.listByShopId(shopId, startDate, endDate);
    }

    public Map<String, Object> listAll(String startDate, String endDate, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<PowerRecord> records = powerRecordMapper.listAll(startDate, endDate, offset, pageSize);
        long total = powerRecordMapper.countAll(startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        result.put("list", records);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }
}
