package com.example;

public class MainServer {

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start(7777);
    }

}