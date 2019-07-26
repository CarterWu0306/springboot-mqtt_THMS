package com.carter.service.impl;

import com.carter.mapper.AlarmMapper;
import com.carter.pojo.Alarm;
import com.carter.pojo.Device;
import com.carter.service.DeviceService;
import com.carter.service.JudgeAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JudgeAlarmServiceImpl implements JudgeAlarmService {

    @Autowired
    private DeviceService deviceServiceImpl;

    @Resource
    private AlarmMapper alarmMapper;

    @Override
    public void judgeAlarm(Map<String, String> map) {
        //最高湿度
        int maxH=90;
        //湿度变化阈值
        int thresholdH=10;
        //最高温度
        int maxT=60;
        //最低温度
        int minT=-10;
        //温度变化阈值
        int thresholdT=5;
        //最高六氟化硫浓度
        int maxSF6=10;
        //六氟化硫浓度阈值
        int thresholdSF6=2;

        int humidityFlag = (new BigDecimal(map.get("humidityFlag"))).intValue();
        int temperatureFlag = (new BigDecimal(map.get("temperatureFlag"))).intValue();
        int sf6Flag = (new BigDecimal(map.get("sf6Flag"))).intValue();
        int newHumidity = (new BigDecimal(map.get("newHumidity"))).intValue();
        int newTemperature = (new BigDecimal(map.get("newTemperature"))).intValue();
        int newSf6 = (new BigDecimal(map.get("newSf6"))).intValue();
        String clientid = map.get("clientid");
        //根据设备名查出设备id
        Device device = deviceServiceImpl.selectDeviceByClientid(clientid);

        //创建警报对象
        Alarm alarm = new Alarm();
        alarm.setDevcId(device.getDeviceId());

        //湿度过高,警报代码:0000;警报:湿度过高,湿度为
        if (newHumidity>=maxH){
            alarm.setType("00");
            alarm.setCode("0000");
            alarm.setAlarmText("湿度过高,湿度为:"+newHumidity+"%/RH");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //温度过高,警报代码:0001;警报:温度过高,温度为
        if (newTemperature>maxT){
            alarm.setType("01");
            alarm.setCode("0001");
            alarm.setAlarmText("温度过高,温度为:"+newTemperature+"℃");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //六氟化硫浓度过高,警报代码:0002;警报:六氟化硫浓度过高,浓度为
        if (newSf6>maxSF6){
            alarm.setType("02");
            alarm.setCode("0002");
            alarm.setAlarmText("六氟化硫浓度过高,六氟化硫浓度为:"+newSf6+"uL/L");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //温度过低,警报代码:0004;警报:温度过低,温度为
        if (newTemperature<=minT){
            alarm.setType("01");
            alarm.setCode("0004");
            alarm.setAlarmText("温度过低,温度为:"+newTemperature+"℃");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //湿度变化大于阈值,警报代码:0005;警报:湿度骤变,差值为
        if ((newHumidity-humidityFlag)>=thresholdH){
            alarm.setType("00");
            alarm.setCode("0005");
            alarm.setAlarmText("湿度骤变,差值为"+(newHumidity-humidityFlag)+"%/RH");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //温度变化大于阈值,警报代码:0006;警报:温度骤变,差值为
        if ((newTemperature-temperatureFlag)>=thresholdT){
            alarm.setType("01");
            alarm.setCode("0006");
            alarm.setAlarmText("温度骤变,差值为:"+(newTemperature-temperatureFlag)+"℃");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }
        //六氟化硫浓度变化大于阈值,警报代码:0007;警报:六氟化硫浓度骤变,差值为
        if ((newSf6-sf6Flag)>=thresholdSF6){
            alarm.setType("02");
            alarm.setCode("0007");
            alarm.setAlarmText("六氟化硫浓度骤变,差值为:"+(newSf6-sf6Flag)+"uL/L");
            alarm.setStartTime(new Date());
            alarm.setStatus("未解决");
            //发送警报
            alarmMapper.insert(alarm);
            //更改警报状态为警报中
            device.setAlarmStatus("1");
            deviceServiceImpl.deviceUpd(device);
        }

        //如果数据正常,结束当前设备之前的警报
        try {
            if (!(newHumidity>=maxH||newTemperature>maxT||newSf6>maxSF6||newTemperature<=minT||(newHumidity-humidityFlag)>=thresholdH||(newTemperature-temperatureFlag)>=thresholdT||(newSf6-sf6Flag)>=thresholdSF6)){
                System.out.println("数据正常");
                //根据设备id查询该设备所有警报信息
                List<Alarm> alarms = alarmMapper.selectAlarmNoDealByDevcId(device.getDeviceId());
                for (Alarm alarm1:alarms){
                    Date now = new Date();
                    long nowTime = now.getTime();
                    long startTime = alarm1.getStartTime().getTime();
                    long result = (nowTime-startTime);

                    long nd = 1000 * 24 * 60 * 60;
                    long nh = 1000 * 60 * 60;
                    long nm = 1000 * 60;

                    // 计算差多少天
                    long day = result / nd;
                    // 计算差多少小时
                    long hour = result % nd / nh;
                    // 计算差多少分钟
                    long min = result % nd % nh / nm;

                    alarm1.setEndTime(now);
                    alarm1.setHoldTime(day + "天" + hour + "小时" + min + "分钟");
                    alarm1.setStatus("已解决");
                    alarmMapper.updateByPrimaryKeySelective(alarm1);
                    //更改设备警报状态为正常
                    device.setAlarmStatus("0");
                    deviceServiceImpl.deviceUpd(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
