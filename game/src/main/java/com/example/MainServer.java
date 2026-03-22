package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainServer {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example")) {
            GameServer server = context.getBean(GameServer.class);
            server.start(7777);
        }
    }

}