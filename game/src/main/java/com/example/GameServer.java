package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private ServerSocket server;
    private List<Room> rooms = new ArrayList<>();
    private int nextRoomId = 1;

    public void start(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Сервер запущен на порту " + port);
            
            while (true) {
                Socket socket = server.accept();
                ClientHandler client = new ClientHandler(socket);
                
                Room room = findAvailableRoom();
                room.addPlayer(client);
                client.start();
                
                System.out.println("Клиент подключен к комнате " + roomId(room));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private synchronized Room findAvailableRoom() {
        for (Room room : rooms) {
            if (!room.isFull()) {
                return room;
            }
        }
        Room newRoom = new Room(nextRoomId++);
        rooms.add(newRoom);
        return newRoom;
    }

    private int roomId(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i) == room) return i + 1;
        }
        return -1;
    }
}