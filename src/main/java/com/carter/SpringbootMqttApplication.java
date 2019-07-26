package com.carter;

import com.carter.mqtt.client.ClientReceiveMessage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.carter.mapper")
public class SpringbootMqttApplication implements CommandLineRunner {


    @Autowired
    ClientReceiveMessage clientReceiveMessage;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringbootMqttApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        clientReceiveMessage.start();
    }
}
