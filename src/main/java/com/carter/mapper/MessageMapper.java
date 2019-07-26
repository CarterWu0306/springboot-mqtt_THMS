package com.carter.mapper;

import com.carter.pojo.Message;
import com.carter.pojo.MessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessageMapper {

    List<Message> selRecentlyByClientid(String clinetid);

    List<Message> selHistory(@Param("params") Map<String, String> map);

    List<Map<String, String>> selHistoryMap(@Param("params") Map<String, String> map);

    int countByExample(MessageExample example);

    int deleteByExample(MessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    List<Message> selectByExample(MessageExample example);

    Message selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
}