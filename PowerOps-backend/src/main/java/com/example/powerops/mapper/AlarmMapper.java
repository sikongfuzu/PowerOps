package com.example.powerops.mapper;

import com.example.powerops.entity.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {
    Alarm findById(@Param("alarmId") Long alarmId);
    List<Alarm> listAlarms(@Param("deviceId") Long deviceId,
                          @Param("alarmType") String alarmType,
                          @Param("alarmLevel") String alarmLevel,
                          @Param("status") String status,
                          @Param("offset") int offset,
                          @Param("limit") int limit);
    long countAlarms(@Param("deviceId") Long deviceId,
                    @Param("alarmType") String alarmType,
                    @Param("alarmLevel") String alarmLevel,
                    @Param("status") String status);
    int insert(Alarm alarm);
    int confirmAlarm(@Param("alarmId") Long alarmId, @Param("userId") Long userId);
    int handleAlarm(@Param("alarmId") Long alarmId, @Param("userId") Long userId);
}
