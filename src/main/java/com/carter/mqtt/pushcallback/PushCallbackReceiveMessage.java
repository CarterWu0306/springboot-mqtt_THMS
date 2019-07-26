package com.carter.mqtt.pushcallback;

import com.alibaba.fastjson.JSON;
import com.carter.mqtt.client.ClientReceiveMessage;
import com.carter.pojo.Message;
import com.carter.service.JudgeAlarmService;
import com.carter.service.SaveMessage;
import com.carter.websocket.SocketClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class PushCallbackReceiveMessage implements MqttCallback {

    @Autowired
    private SaveMessage saveMessageImpl;

    @Autowired
    private SocketClient socketClient;

    @Autowired
    private JudgeAlarmService judgeAlarmServiceImpl;

    @Autowired
    private ClientReceiveMessage clientReceiveMessage;

    private Message temperatureMessage = new Message();
    private Message humidityMessage = new Message();
    private Message sf6Message = new Message();

    String temperatureFlag="0";
    String humidityFlag="0";
    String sf6Flag="0";

    @Override
    //处理链接断开
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println(throwable.getMessage());
        System.out.println("连接断开，开始重连......");
        try {
            clientReceiveMessage.start();
            System.out.println("重连成功");
        } catch (Exception e) {
            System.out.println("重连失败,原因:"+e.toString());
        }
    }

    @Override
    //处理消息送达
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String msg = new String(mqttMessage.getPayload());
        System.out.println("接收消息主题 : " + s);
        System.out.println("接收消息Qos : " + mqttMessage.getQos());
        System.out.println("接收消息内容 : " + msg);
        String[] msgStr = msg.split(" ");

        //湿度对象设值
        StringBuilder sb = new StringBuilder(msgStr[1]);
        sb.insert(2,".");
        humidityMessage.setTopic(s);
        humidityMessage.setValue(sb.toString());
        humidityMessage.setType("00");
        humidityMessage.setTime(new Date());
        humidityMessage.setClientid(msgStr[0]);

        //温度对象设值
        StringBuilder sb2 = new StringBuilder(msgStr[2]);
        sb2.insert(2,".");
        temperatureMessage.setTopic(s);
        temperatureMessage.setValue(sb2.toString());
        temperatureMessage.setType("01");
        temperatureMessage.setTime(new Date());
        temperatureMessage.setClientid(msgStr[0]);

        //六氟化硫浓度对象设值
        sf6Message.setTopic(s);
        sf6Message.setValue(msgStr[3]);
        sf6Message.setType("02");
        sf6Message.setTime(new Date());
        sf6Message.setClientid(msgStr[0]);

        //判断数据格式是否正确
        //判断湿度是否为0或正数
        boolean hCheck = msgStr[1].matches("^[0-9]\\d*|0$");
        //判断温度是否为数字
        boolean tCheck = msgStr[2].matches("^-?[0-9]\\d*|0$");
        //判断六氟化硫浓度是否为0或正数
        boolean sf6Check = msgStr[3].matches("^[0-9]\\d*|0$");

        if (hCheck&&tCheck&&sf6Check){

            //向socket传值
            socketClient.groupSending(JSON.toJSONString(temperatureMessage));
            socketClient.groupSending(JSON.toJSONString(humidityMessage));
            socketClient.groupSending(JSON.toJSONString(sf6Message));

            if (!msgStr[1].equals(humidityFlag)) {
                //保存湿度数据
                saveMessageImpl.save(humidityMessage);
                System.out.println("湿度已保存");
                humidityFlag=msgStr[1];
            }else{
                System.out.println("湿度没有变化,不保存");
            }

            if (!msgStr[2].equals(temperatureFlag)){
                //保存温度数据
                saveMessageImpl.save(temperatureMessage);
                System.out.println("温度已保存");

                temperatureFlag=msgStr[2];
            }else{
                System.out.println("温度没有变化,不保存");
            }

            if (!msgStr[3].equals(sf6Flag)) {
                //保存湿度数据
                saveMessageImpl.save(sf6Message);
                System.out.println("六氟化硫浓度已保存");
                sf6Flag=msgStr[3];
            }else{
                System.out.println("六氟化硫浓度没有变化,不保存");
            }

            //判断数据是否正常,若异常则发送警报
            HashMap<String,String> map = new HashMap<>();
            map.put("humidityFlag",humidityFlag);
            map.put("temperatureFlag",temperatureFlag);
            map.put("sf6Flag",sf6Flag);
            map.put("newHumidity",msgStr[1]);
            map.put("newTemperature",msgStr[2]);
            map.put("newSf6",msgStr[3]);
            map.put("clientid",msgStr[0]);
            judgeAlarmServiceImpl.judgeAlarm(map);
        }else {
            throw new Exception("数据异常");
        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}