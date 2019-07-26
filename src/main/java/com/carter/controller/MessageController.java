package com.carter.controller;

import com.carter.pojo.Device;
import com.carter.pojo.LayUIDataGrid;
import com.carter.pojo.Message;
import com.carter.service.DeviceService;
import com.carter.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageServiceImpl;

    @Autowired
    private DeviceService deviceServiceImpl;

    /**
     * 分页查询历史数据
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("selhistory")
    @ResponseBody
    public  LayUIDataGrid selhistory(int page,int limit,String deviceParam,String typeParam,String startParam,String endParam){
        HashMap<String, String> map = new HashMap<>();
        map.put("deviceParam",deviceParam);
        if (typeParam!=null&&typeParam!=""){
            if (typeParam.equals("湿度")){
                map.put("typeParam","00");
            }else if (typeParam.equals("温度")){
                map.put("typeParam","01");
            }else if (typeParam.equals("六氟化硫浓度")){
                map.put("typeParam","02");
            }
        }
        map.put("startParam",startParam);
        map.put("endParam",endParam);
        LayUIDataGrid show = messageServiceImpl.show(page, limit,map);
        return show;
    }

    /**
     * 实时数据显示页面跳转
     * @return
     */
    @RequestMapping("show")
    public String show(Model model){
        //查询下拉框列表
        List list = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",list);
        return "show";
    }

    /**
     * 初始化温湿度变化曲线图
     * @param devcName
     */
    @RequestMapping("showInit")
    @ResponseBody
    public void showInit(@RequestParam String devcName, HttpServletResponse response){
        Device device = deviceServiceImpl.selectDeviceByDevcName(devcName);
        try {
            response.getWriter().write(device.getClientId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageServiceImpl.showInit(device.getClientId());
    }

    /**
     * 历史数据查询页面跳转
     * @return
     */
    @RequestMapping("historySel")
    public String select(Model model){
        //查询下拉框列表
        List list = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",list);
        return "historySel";
    }

    /**
     * 历史数据统计页面跳转
     * @return
     */
    @RequestMapping("history")
    public String history(Model model){
        //查询下拉框列表
        List list = deviceServiceImpl.selectDevcNameDistinct();
        model.addAttribute("optionText",list);
        return "history";
    }

    /**
     * 根据条件筛选查询历史数据绘图
     * @param deviceParam
     * @param startParam
     * @param endParam
     * @return
     */
    @RequestMapping("showHistory")
    @ResponseBody
    public List<Message> selHistory(String deviceParam, String startParam, String endParam){
        Device device = deviceServiceImpl.selectDeviceByDevcName(deviceParam);
        HashMap<String, String> map = new HashMap<>();
        map.put("deviceParam",device.getClientId());
        map.put("startParam",startParam);
        map.put("endParam",endParam);
        List<Message> messages = messageServiceImpl.selHistory(map);
        return messages;
    }
}
