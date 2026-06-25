package com.example.powerops.mapper;

import com.example.powerops.entity.PowerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PowerRecordMapper {
    int insert(PowerRecord record);
    List<PowerRecord> listByShopId(@Param("shopId") Long shopId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
