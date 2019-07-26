package com.carter.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private Integer id;

    private String topic;

    private String value;

    private String type;

    private Date time;

    private String clientid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid == null ? null : clientid.trim();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") ;
        return "Message{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", clientid='" + clientid + '\'' +
                '}';
    }
}