package com.carter.service.impl;

import com.carter.mapper.MessageMapper;
import com.carter.pojo.Message;
import com.carter.service.SaveMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SaveMessageImpl implements SaveMessage {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public int save(Message message) {
        int index = messageMapper.insert(message);
        return index;
    }
}
