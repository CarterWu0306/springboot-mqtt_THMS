package com.carter.service.impl;

import com.alibaba.fastjson.JSON;
import com.carter.mapper.MessageMapper;
import com.carter.pojo.LayUIDataGrid;
import com.carter.pojo.Message;
import com.carter.pojo.MessageExample;
import com.carter.service.MessageService;
import com.carter.websocket.SocketClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Autowired
    private SocketClient socketClient;

    @Override
    public LayUIDataGrid show(int page, int limit, Map<String, String> map) {
        PageHelper.startPage(page,limit);
        //查询全部
        //List<Message> messageList = messageMapper.selectByExample(new MessageExample());
        //List<Message> messageList = messageMapper.selHistory(map);

        List<Map<String, String>> messageList = messageMapper.selHistoryMap(map);

        for (Map<String, String> message:messageList){
            if (message.get("type").equals("00")){
                message.put("type","湿度/RH");
            }else if (message.get("type").equals("01")){
                message.put("type","温度/℃");
            }else if (message.get("type").equals("02")){
                message.put("type","六氟化硫浓度uL/L");
            }
        }
        //分页代码
        //设置分页条件
        PageInfo<Map<String, String>> pi = new PageInfo<Map<String, String>>(messageList);

        //放入到实体类中
        LayUIDataGrid dataGrid = new LayUIDataGrid();
        dataGrid.setData(pi.getList());
        dataGrid.setCount(pi.getTotal());
        return dataGrid;
    }

    /**
     * 查询全部
     * @return
     */
    @Override
    public  List<Message> selAll() {
        List<Message> messageList = messageMapper.selectByExample(new MessageExample());
        return messageList;
    }

    /**
     * 查询指定时间段内消息
     * @return
     */
    @Override
    public List<Message> selByTime(int day) {
        int range=1000*60*60*24*day;
        MessageExample messageExample = new MessageExample();
        MessageExample.Criteria criteria = messageExample.createCriteria();
        Date startTime = new Date();
        startTime.setTime(startTime.getTime()-range);
        Date endTime = new Date();
        criteria.andTimeBetween(startTime,endTime);
        List<Message> messages = messageMapper.selectByExample(messageExample);
        return messages;
    }

    @Override
    public void showInit(String clientid) {
        List<Message> messages = messageMapper.selRecentlyByClientid(clientid);
        for (Message message:messages){
            socketClient.groupSending(JSON.toJSONString(message));
        }
    }

    @Override
    public List<Message> selHistory(Map<String, String> map) {
        List<Message> messages = messageMapper.selHistory(map);
        return messages;
    }

}
