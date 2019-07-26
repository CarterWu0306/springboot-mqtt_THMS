package com.carter.mapper;

import com.carter.pojo.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {

    Device selectDeviceByDevcName(String devcName);

    Device selectDeviceByClientid(String clientid);

    List<Device> selDevcManageInfo(@Param("params") Map<String, String> map);

    List selectDevcNameDistinct();

    int countDevcStatus(String status);

    int countAlarmStatus(String alarmStatus);

    int deleteByPrimaryKey(Integer deviceId);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer deviceId);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
}