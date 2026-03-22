package com.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private Room room;
    private String playerSymbol;

    public ClientHandler(Socket socket) {
        initializeClientStreams(socket);
    }

    private void initializeClientStreams(Socket client) {
        try {
            this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.toClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setPlayerSymbol(String symbol) {
        this.playerSymbol = symbol;
    }

    public String getPlayerSymbol() {
        return playerSymbol;
    }

    public void sendMessage(String message) {
        toClient.println(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String messageFromClient = fromClient.readLine();
                if (messageFromClient == null) break;
                
                if (room == null) continue;
                
                String upperMsg = messageFromClient.toUpperCase().trim();
                
                if (upperMsg.equals("RESTART")) {
                    if (!room.isGameActive()) {
                        room.restartGame();
                    } else {
                        sendMessage("Игра еще идет!");
                    }
                    continue;
                }
                
                if (!room.isGameActive()) {
                    sendMessage("Игра окончена. Введите RESTART для новой игры.");
                    continue;
                }

                if (messageFromClient.startsWith("MOVE:")) {
                    String move = messageFromClient.substring(5).trim().toUpperCase();
                    
                    if (!room.isPlayerTurn(this)) {
                        sendMessage("Ошибка: сейчас не ваш ход!");
                        continue;
                    }
                    
                    if (!room.isValidMoveFormat(move)) {
                        sendMessage("Ошибка: неверный формат!");
                        continue;
                    }
                    
                    if (!room.makeMove(move, playerSymbol)) {
                        sendMessage("Ошибка: клетка уже занята!");
                        continue;
                    }
                    
                    room.broadcast("Противник ходит: " + move, this);
                    room.sendBoardToAll();
                    
                    String winner = room.checkWinner();
                    if (winner != null) {
                        room.setGameActive(false);
                        if (winner.equals("DRAW")) {
                            room.broadcast("Ничья!", null);
                            sendMessage("Ничья! Введите RESTART для новой игры.");
                        } else if (winner.equals(playerSymbol)) {
                            sendMessage("Вы победили! Введите RESTART для новой игры.");
                            room.broadcast("Вы проиграли! Введите RESTART для новой игры.", this);
                        }
                    } else {
                        room.switchTurn();
                        sendMessage("Ход сделан. Ожидайте противника...");
                        room.broadcast("Ваш ход! (введите MOVE: A1 и т.д.)", this);
                    }
                    
                } else if (messageFromClient.startsWith("MESSAGE:") || messageFromClient.startsWith("message:")) {
                    String text = messageFromClient.substring(8).trim();
                    room.broadcast("Сообщение от " + playerSymbol + ": " + text, this);
                    
                } else {
                    sendMessage("Неизвестная команда. Используйте: MOVE: A1 или MESSAGE: текст");
                }
                
            } catch (IOException e) {
                break;
            }
        }
    }
}