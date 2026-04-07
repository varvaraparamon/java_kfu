package com.example;

import com.example.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private ObjectMapper objectMapper;
    private Socket client;
    private boolean authenticated = false;

    public MainClient(String host, int port) {
        try {
            this.client = new Socket(host, port);
            this.toServer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
            this.fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.objectMapper = new ObjectMapper();
        startReceive();
    }

    private void startReceive() {
        new Thread(() -> {
            while (true) {
                try {
                    String jsonFromServer = fromServer.readLine();
                    if (jsonFromServer != null) {
                        try {
                            MessageDto msg = objectMapper.readValue(jsonFromServer, MessageDto.class);
                            System.out.println("[" + msg.getType() + "] " + (msg.getText() != null ? msg.getText() : ""));
                            
                            if (msg.getType().equals("SYSTEM") && msg.getText().contains("Вход выполнен")) {
                                authenticated = true;
                                printGameCommands();
                            }
                        } catch (JsonProcessingException e) {
                            System.out.println(jsonFromServer);
                        }
                    } else {
                        break;
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }).start();
    }

    private void printGameCommands() {
        System.out.println("\n=== КОМАНДЫ ИГРЫ ===");
        System.out.println("move A1    - сделать ход");
        System.out.println("chat текст - отправить сообщение");
        System.out.println("restart    - начать новую игру");
        System.out.println("===================");
    }

    private void sendMessage(MessageDto message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            toServer.println(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void main(String[] args) {
        MainClient client = new MainClient("127.0.0.1", 7777);
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Команды:");
            System.out.println("register username:password - регистрация");
            System.out.println("login username:password    - вход");

            while (true) {
                String input = scanner.nextLine().trim();
                String[] parts = input.split(" ", 2);
                String command = parts[0].toLowerCase();
                String creds = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "register":
                        client.sendMessage(MessageDto.builder()
                                .type("REGISTER")
                                .payload(creds)
                                .build());
                        break;
                        
                    case "login":
                        client.sendMessage(MessageDto.builder()
                                .type("LOGIN")
                                .payload(creds)
                                .build());
                        break;
                        
                    case "move":
                        if (!client.authenticated) {
                            System.out.println("Сначала войдите!");
                            continue;
                        }
                        client.sendMessage(MessageDto.builder()
                                .type("MOVE")
                                .payload(creds.toUpperCase())
                                .build());
                        break;
                        
                    case "chat":
                        if (!client.authenticated) {
                            System.out.println("Сначала войдите!");
                            continue;
                        }
                        client.sendMessage(MessageDto.builder()
                                .type("CHAT")
                                .text(creds)
                                .build());
                        break;
                        
                    case "restart":
                        client.sendMessage(MessageDto.builder()
                                .type("RESTART")
                                .build());
                        break;
                        
                    default:
                        System.out.println("Неизвестная команда");
                }
            }
        }
    }
}