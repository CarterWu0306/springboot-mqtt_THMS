package com.carter.service;

import com.carter.pojo.Alarm;
import com.carter.pojo.LayUIDataGrid;

import java.util.Map;

public interface AlarmService {
    int count(String type,String startTime,String endTime,int deviceId);

    LayUIDataGrid selAlarmHistory(int page, int limit, Map<String, String> map);

    int deleteByPrimaryKey(Integer alarmId);

    int updateByPrimaryKeySelective(Alarm record);

    Alarm selectByPrimaryKey(Integer alarmId);


}
