package com.carter.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.net.URI;

/**
 * 开启WebSocket支持
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            //testcarter.free.idcfengye.com  10.26.24.22:8080
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://10.26.32.90:8080/websocket/123"),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("[websocket] 连接成功");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("[websocket] 收到消息={"+message+"}");

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("[websocket] 连接错误={"+ex.getMessage()+"}");
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
