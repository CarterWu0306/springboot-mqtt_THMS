package com.carter.mqtt.client;

import com.carter.mqtt.pushcallback.PushCallbackReceiveMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;


@Component
public class ClientReceiveMessage {
    public static final String SERVER_URL = "tcp://115.29.175.26:1883";
    public static final String TOPIC = "humiture";
    public static final String clientid  = "clientReceiveMessage";

    private MqttClient client;
    private MqttConnectOptions options;

    private String userName = "mosquitto";
    private String passWord = "mosquitto";

    private ScheduledExecutorService scheduler;

    @Autowired
    private PushCallbackReceiveMessage pushCallbackReceiveMessage;

    public void start() throws Exception{
        client = new MqttClient(SERVER_URL, clientid, new MemoryPersistence());
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(20);
        options.setKeepAliveInterval(20);
        client.setCallback(pushCallbackReceiveMessage);
        MqttTopic topic = client.getTopic(TOPIC);
        client.connect(options);
        int[] Qos = {1};
        String[] topic1 = {TOPIC};
        client.subscribe(topic1,Qos);
    }


    public String getTOPIC() {
        return TOPIC;
    }
}
