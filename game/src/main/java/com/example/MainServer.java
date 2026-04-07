package com.example;

import com.example.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainServer {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
            GameServer server = context.getBean(GameServer.class);
            server.start(7777);
        }
    }
}