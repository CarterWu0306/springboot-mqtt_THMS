package com.carter.controller;

import com.carter.pojo.Alarm;
import com.carter.pojo.DataObject;
import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;
import com.carter.service.AlarmService;
import com.carter.service.DeviceService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AlarmController {

    @Autowired
    private AlarmService alarmServiceImpl;

    @Autowired
    private DeviceService deviceServiceImpl;

    /**
     * 警报统计
     * @param model
     * @return
     */
    @RequestMapping("alarm")
    public String test(Model model,String startParam,String endParam,String deviceParam) throws ParseException {

        //查询下拉框列表
        List sleList = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",sleList);

        //各时间段报警总次数统计图代码块
        List<DataObject> list = new ArrayList<>();
        String startTime;
        String endTime;
        int deviceId=-1;

        if (deviceParam!=null&&!deviceParam.equals("")){
           Device device= deviceServiceImpl.selectDeviceByDevcName(deviceParam);
           if (device!=null){
               deviceId = device.getDeviceId();
           }else {
               deviceId=-2;
           }
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

        if (startParam==null||startParam.equals("")){
            Date date = new Date();
            endParam = sdf.format(date);
            date.setTime(date.getTime()-24*60*60*1000);
            startParam=sdf.format(date);
        }

            Date date1 = sdf.parse(startParam);
            Date date2 = sdf.parse(endParam);
            long diff=date2.getTime()-date1.getTime();
            long hour=diff/(60*60*1000);

            int hCount[] = new int[24];
            int tCount[] = new int[24];
            int sCount[] = new int[24];

            for (int j=0;j<hour;j++){
                    if (date1.getTime()==date2.getTime()){
                        System.out.println("break");
                        break;
                    }
                startTime=sdf.format(date1);
                int index = date1.getHours();
                date1.setTime(date1.getTime()+60*60*1000);
                endTime=sdf.format(date1);

                int humidityCountByTime = alarmServiceImpl.count("00",startTime,endTime,deviceId);
                int temperatureCountByTime = alarmServiceImpl.count("01",startTime,endTime,deviceId);
                int sf6CountByTime = alarmServiceImpl.count("02",startTime,endTime,deviceId);
                hCount[index%24]+=humidityCountByTime;
                tCount[index%24]+=temperatureCountByTime;
                sCount[index%24]+=sf6CountByTime;

            }
            for (int k=0;k<24;k++){
                list.add(new DataObject(k,tCount[k],hCount[k],sCount[k]));
            }

        model.addAttribute("data",list);

        //各类型报警总次数统计图代码块
        List<DataObject> listType = new ArrayList<>();
        int humidityCountByType = alarmServiceImpl.count("00", startParam, endParam,deviceId);
        int temperatureCountByType = alarmServiceImpl.count("01",startParam,endParam,deviceId);
        int sf6CountByType = alarmServiceImpl.count("02",startParam,endParam,deviceId);
        listType.add(new DataObject(1,temperatureCountByType,humidityCountByType,sf6CountByType));
        model.addAttribute("dataType",listType);

        //设备状态统计图代码块
        int countOffline = deviceServiceImpl.countDevcStatus("0");
        int countOnline = deviceServiceImpl.countDevcStatus("1");
        model.addAttribute("countOffline",countOffline);
        model.addAttribute("countOnline",countOnline);
        
        //警报状态统计图代码块
        int countNormal = deviceServiceImpl.countAlarmStatus("0");
        int countAlarm = deviceServiceImpl.countAlarmStatus("1");
        model.addAttribute("countNormal",countNormal);
        model.addAttribute("countAlarm",countAlarm);

        return "alarm";

    }

    /**
     * 警报记录查询页面跳转
     * @return
     */
    @RequestMapping("alarmSel")
    public String alarmSel(Model model){
        //查询下拉框列表
        List list = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",list);
        return "alarmSel";
    }

    /**
     * 分页查询报警记录
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("selAlarmHistory")
    @ResponseBody
    public LayUIDataGrid selAlarmHistory(int page, int limit, String deviceParam, String typeParam, String startParam, String endParam,String statusParam){
        HashMap<String, String> map = new HashMap<>();

        Device device = deviceServiceImpl.selectDeviceByDevcName(deviceParam);


        if (deviceParam!=null&&!deviceParam.equals("")){
            if (device!=null){
                map.put("deviceParam",device.getDeviceId().toString());
            }else{
                map.put("deviceParam","-1");
            }
        }
        if (typeParam!=null&&!typeParam.equals("")){
            if (typeParam.equals("湿度警报")){
                map.put("typeParam","00");
            }else if (typeParam.equals("温度警报")){
                map.put("typeParam","01");
            }else if (typeParam.equals("六氟化硫浓度警报")){
                map.put("typeParam","02");
            }
        }
        map.put("startParam",startParam);
        map.put("endParam",endParam);
        map.put("statusParam",statusParam);
        LayUIDataGrid show = alarmServiceImpl.selAlarmHistory(page, limit,map);
        return show;
    }

    @RequestMapping("delAlarm")
    @ResponseBody
    public void deleteByPrimaryKey(Integer alarmId){
        alarmServiceImpl.deleteByPrimaryKey(alarmId);
    }

    /*@RequestMapping("updAlarm")
    @ResponseBody
    public void updateByPrimaryKeySelective(Integer alarmId){
        Alarm alarm = alarmServiceImpl.selectByPrimaryKey(alarmId);
        Date now = new Date();
        long nowTime = now.getTime();
        long startTime = alarm.getStartTime().getTime();
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

        alarm.setEndTime(now);
        alarm.setHoldTime(day + "天" + hour + "小时" + min + "分钟");
        alarm.setStatus("已处理");

        alarmServiceImpl.updateByPrimaryKeySelective(alarm);
    }*/
}
