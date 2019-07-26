package com.carter.mapper;

import com.carter.pojo.Alarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AlarmMapper {
    int count(String type, String startTime, String endTime,int deviceId);

    List<Map<String, String>> selAlarmHistory(@Param("params") Map<String, String> map);

    int deleteByPrimaryKey(Integer alarmId);

    int insert(Alarm record);

    int insertSelective(Alarm record);

    Alarm selectByPrimaryKey(Integer alarmId);

    List<Alarm> selectAlarmByDevcId(Integer devcId);

    List<Alarm> selectAlarmNoDealByDevcId(Integer devcId);

    int updateByPrimaryKeySelective(Alarm record);

    int updateByPrimaryKey(Alarm record);
}