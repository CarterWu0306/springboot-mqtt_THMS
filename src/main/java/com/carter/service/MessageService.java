package com.carter.service;

import com.carter.pojo.LayUIDataGrid;
import com.carter.pojo.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {
    /**
     * 历史消息分页查询
     * @param page
     * @param limit
     * @return
     */
    LayUIDataGrid show(int page,int limit, Map<String, String> map);

    /**
     * 查询所有历史消息
     * @return
     */
    List<Message> selAll();

    /**
     * 查询一段时间内消息
     * @param day 天数
     * @return
     */
    List<Message> selByTime(int day);

    /**
     * 查询最近5条消息,用于初始化温湿度变化图
     * @param clientid
     */
    void showInit(String clientid);

    /**
     * 查询历史数据
     * @param map
     * @return
     */
    List<Message> selHistory(Map<String, String> map);
}
