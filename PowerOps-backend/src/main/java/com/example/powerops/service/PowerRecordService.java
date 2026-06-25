package com.example.powerops.service;

import com.example.powerops.entity.PowerRecord;
import com.example.powerops.mapper.PowerRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
}
