package com.carter.service.impl;

import com.carter.mapper.AlarmMapper;
import com.carter.mapper.DeviceMapper;
import com.carter.pojo.Alarm;
import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;
import com.carter.service.AlarmService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Resource
    private AlarmMapper alarmMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public int count(String type,String startTime,String endTime,int deviceId) {
        int count = alarmMapper.count(type,startTime,endTime,deviceId);
        return count;
    }

    @Override
    public LayUIDataGrid selAlarmHistory(int page,int limit,Map<String, String> map) {
        PageHelper.startPage(page,limit);
        List<Map<String, String>> alarms = alarmMapper.selAlarmHistory(map);

        for (Map<String, String> alarm:alarms){
            if (alarm.get("type").equals("00")){
                alarm.put("type","湿度警报");
            }else if (alarm.get("type").equals("01")){
                alarm.put("type","温度警报");
            }else if (alarm.get("type").equals("02")){
                alarm.put("type","六氟化硫浓度警报");
            }
            //Device device = deviceMapper.selectByPrimaryKey(Integer.parseInt(alarm.get("devcId")));
            //alarm.put("devcName",device.getName());
        }
        //分页代码
        //设置分页条件
        PageInfo<Map<String, String>> pi = new PageInfo<Map<String, String>>(alarms);

        //放入到实体类中
        LayUIDataGrid dataGrid = new LayUIDataGrid();
        dataGrid.setData(pi.getList());
        dataGrid.setCount(pi.getTotal());
        return dataGrid;
    }

    @Override
    public int deleteByPrimaryKey(Integer alarmId) {
        int index = alarmMapper.deleteByPrimaryKey(alarmId);
        return index;
    }

    @Override
    public int updateByPrimaryKeySelective(Alarm record) {
        int index = alarmMapper.updateByPrimaryKeySelective(record);
        return index;
    }

    @Override
    public Alarm selectByPrimaryKey(Integer alarmId) {
        Alarm alarm = alarmMapper.selectByPrimaryKey(alarmId);
        return alarm;
    }
}
