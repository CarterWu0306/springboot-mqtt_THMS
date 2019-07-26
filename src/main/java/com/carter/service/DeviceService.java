package com.carter.service;

import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    Device selectDeviceByDevcName(String devcName);

    Device selectDeviceByClientid(String clientid);

    Device selectByPrimaryKey(Integer deviceId);

    LayUIDataGrid selDevcManageInfo(int page, int limit, Map<String, String> map);

    int deviceAdd(Device device);

    int deviceUpd(Device device);

    int deviceDel(int deviceId);

    int countDevcStatus(String status);

    int countAlarmStatus(String alarmStatus);

    List selectDevcNameDistinct();

}
