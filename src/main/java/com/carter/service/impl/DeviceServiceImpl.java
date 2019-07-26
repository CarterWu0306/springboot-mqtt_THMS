package com.carter.service.impl;

import com.carter.mapper.DeviceMapper;
import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;
import com.carter.service.DeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Device selectDeviceByDevcName(String devcName) {
        Device device = deviceMapper.selectDeviceByDevcName(devcName);
        return device;
    }

    @Override
    public Device selectDeviceByClientid(String clientid) {
        Device device = deviceMapper.selectDeviceByClientid(clientid);
        return device;
    }

    @Override
    public Device selectByPrimaryKey(Integer deviceId) {
        return deviceMapper.selectByPrimaryKey(deviceId);
    }

    @Override
    public LayUIDataGrid selDevcManageInfo(int page, int limit, Map<String, String> map) {
        PageHelper.startPage(page,limit);
        List<Device> devices = deviceMapper.selDevcManageInfo(map);
        for (Device device:devices){
            if (device.getStatus().equals("0")){
                device.setStatus("设备离线");
            }else if (device.getStatus().equals("1")){
                device.setStatus("设备在线");
            }

            if (device.getAlarmStatus().equals("0")){
                device.setAlarmStatus("正常");
            }else if (device.getAlarmStatus().equals("1")){
                device.setAlarmStatus("警报中");
            }
        }
        //分页代码
        //设置分页条件
        PageInfo<Device> pi = new PageInfo<Device>(devices);

        //放入到实体类中
        LayUIDataGrid dataGrid = new LayUIDataGrid();
        dataGrid.setData(pi.getList());
        dataGrid.setCount(pi.getTotal());
        return dataGrid;
    }

    @Override
    public int deviceAdd(Device device) {
        int index = deviceMapper.insertSelective(device);
        return index;
    }

    @Override
    public int deviceUpd(Device device) {
        int index = deviceMapper.updateByPrimaryKeySelective(device);
        return index;
    }

    @Override
    public int deviceDel(int deviceId) {
        int index = deviceMapper.deleteByPrimaryKey(deviceId);
        return index;
    }

    @Override
    public int countDevcStatus(String status) {
        int count = deviceMapper.countDevcStatus(status);
        return count;
    }

    @Override
    public int countAlarmStatus(String alarmStatus) {
        int count = deviceMapper.countAlarmStatus(alarmStatus);
        return count;
    }

    @Override
    public List selectDevcNameDistinct() {
        List list = deviceMapper.selectDevcNameDistinct();
        return list;
    }

}
