package com.carter.controller;

import com.carter.common.ResponseBo;
import com.carter.mapper.AlarmMapper;
import com.carter.mapper.DeviceMapper;
import com.carter.pojo.Alarm;
import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;
import com.carter.service.AlarmService;
import com.carter.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class DeviceController {

    @Autowired
    private AlarmService alarmServiceImpl;

    @Autowired
    private DeviceService deviceServiceImpl;

    @RequestMapping("selDeviceInfo")
    public String selDeviceInfo(Model model, Integer alarmId) {
        Alarm alarm =alarmServiceImpl.selectByPrimaryKey(alarmId);
        Device device =deviceServiceImpl.selectByPrimaryKey(alarm.getDevcId());
        model.addAttribute("devcName", device.getName());
        model.addAttribute("code", device.getCode());
        model.addAttribute("type", device.getType());
        if (device.getStatus().equals("0")) {
            model.addAttribute("status", "运行中");
        } else {
            model.addAttribute("status", "设备离线");
        }
        if (device.getAlarmStatus().equals("0")) {
            model.addAttribute("alarmStatus", "正常");
        } else {
            model.addAttribute("alarmStatus", "警报中");
        }
        model.addAttribute("dutyPerson", device.getDutyPerson());
        model.addAttribute("descript", device.getDescript());
        return "deviceInfo";
    }

    @RequestMapping("devcManage")
    public String devcManage(Model model) {
        //查询下拉框列表
        List list = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",list);
        return "devcmanage";
    }

    @RequestMapping("selDevcManageInfo")
    @ResponseBody
    public LayUIDataGrid selDevcManageInfo(int page, int limit,String devcNameParam,String typeParam,String devcStatusParam,String alarmStatusParam) {
        HashMap<String, String> map = new HashMap<>();

        map.put("devcName",devcNameParam);
        map.put("typeParam",typeParam);
        if (devcStatusParam!=null&&!devcStatusParam.equals("")){
            if (devcStatusParam.equals("设备离线")){
                map.put("devcStatusParam","0");
            }else{
                map.put("devcStatusParam","1");
            }
        }

        if (alarmStatusParam!=null&&!alarmStatusParam.equals("")){
            if (alarmStatusParam.equals("正常")){
                map.put("alarmStatusParam","0");
            }else{
                map.put("alarmStatusParam","1");
            }
        }


        LayUIDataGrid dataGrid = deviceServiceImpl.selDevcManageInfo(page, limit, map);
        return dataGrid;
    }

    @RequestMapping("deviceAdd")
    public String deviceAdd(Model model,int deviceId,String devcName,
                            String clientId,String code,String type,
                            String status,String alarmStatus,
                            String createPerson,String dutyPerson,String descript){
        model.addAttribute("deviceId",deviceId);
        model.addAttribute("devcName",devcName);
        model.addAttribute("clientId",clientId);
        model.addAttribute("code",code);
        model.addAttribute("type",type);
        model.addAttribute("status",status);
        model.addAttribute("alarmStatus",alarmStatus);
        model.addAttribute("createPerson",createPerson);
        model.addAttribute("dutyPerson",dutyPerson);
        model.addAttribute("descript",descript);
        return "deviceAdd";
    }

    @RequestMapping("addConfirm")
    @ResponseBody
    public ResponseBo addConfirm(int deviceId,String devcName,
                                 String clientId,String code,String type,
                                 String status,String alarmStatus,
                                 String createPerson,String dutyPerson,String descript){

        if (devcName==null||devcName.equals("")
            ||clientId==null||clientId.equals("")
            ||code==null||code.equals("")
            ||type==null||type.equals("")
            ||status==null||status.equals("")
            ||alarmStatus==null||alarmStatus.equals("")
            ||createPerson==null||createPerson.equals("")
            ||dutyPerson==null||dutyPerson.equals("")
            ||descript==null||descript.equals("")
        ){
            return ResponseBo.error("请填写所有信息");
        }

        Device device = new Device();
        device.setName(devcName);
        device.setClientId(clientId);
        device.setCode(code);
        device.setType(type);
        device.setStatus(status);
        device.setAlarmStatus(alarmStatus);
        device.setCreatePerson(createPerson);
        device.setCreateTime(new Date());
        device.setDutyPerson(dutyPerson);
        device.setDescript(descript);
        //添加
        if (deviceId==-1){

            //查询设备名称是否重复
            Device deviceCheck = deviceServiceImpl.selectDeviceByDevcName(device.getName());
            if (deviceCheck!=null){
                return ResponseBo.error("设备名已存在");
            }else{
                int index = deviceServiceImpl.deviceAdd(device);
                if (index>0){
                    return ResponseBo.ok("设备添加成功");
                }else{
                    return ResponseBo.error("设备添加失败");
                }
            }
        }else if (deviceId!=-1){
            device.setDeviceId(deviceId);
            //查询设备名称是否重复
            Device deviceCheck = deviceServiceImpl.selectDeviceByDevcName(device.getName());
            if (deviceCheck!=null&&deviceCheck.getDeviceId()!=deviceId) {
                return ResponseBo.error("设备名已存在");
            }else{
                int index = deviceServiceImpl.deviceUpd(device);
                if (index>0){
                    return ResponseBo.ok("设备编辑成功");
                }else {
                    return ResponseBo.error("设备编辑失败");
                }
            }
        }

        return ResponseBo.error("操作异常");
    }

    @RequestMapping("delDevice")
    @ResponseBody
    public ResponseBo delDevice(int deviceId){
        try {
            int index = deviceServiceImpl.deviceDel(deviceId);
            return ResponseBo.ok("设备删除成功");
        } catch (Exception e) {
            return ResponseBo.error("设备删除失败");
        }
    }
}
