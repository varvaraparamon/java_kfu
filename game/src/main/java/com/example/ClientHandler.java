package com.example;

import com.example.dto.MessageDto;
import com.example.models.User;
import com.example.servicies.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientHandler extends Thread {
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private Room room;
    private String playerSymbol;
    private String username;
    private boolean authenticated = false;
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    public ClientHandler(Socket socket) {
        initializeClientStreams(socket);
        this.objectMapper = new ObjectMapper();
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

    public String getUsername() {
        return username;
    }

    public void sendMessage(MessageDto message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            toClient.println(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void run() {
        try {
            if (!authenticate()) {
                return;
            }
            gameLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate() throws IOException {
        sendMessage(MessageDto.builder()
                .type("SYSTEM")
                .text("Добро пожаловать! Введите REGISTER или LOGIN")
                .build());

        while (!authenticated) {
            String jsonFromClient = fromClient.readLine();
            if (jsonFromClient == null) return false;

            MessageDto request;
            try {
                request = objectMapper.readValue(jsonFromClient, MessageDto.class);
            } catch (JsonProcessingException e) {
                sendMessage(MessageDto.builder()
                        .type("ERROR")
                        .text("Неверный формат JSON")
                        .build());
                continue;
            }

            String type = request.getType();
            if (type == null) continue;

            String upperType = type.toUpperCase();

            if (upperType.equals("REGISTER")) {
                handleRegister(request);
            } else if (upperType.equals("LOGIN")) {
                if (handleLogin(request)) {
                    return true;
                }
            } else {
                sendMessage(MessageDto.builder()
                        .type("ERROR")
                        .text("Сначала необходимо войти или зарегистрироваться")
                        .build());
            }
        }
        return false;
    }

    private void handleRegister(MessageDto request) {
        String[] creds = parseCredentials(request);
        if (creds == null) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Формат: {\"type\":\"REGISTER\",\"payload\":\"username:password\"}")
                    .build());
            return;
        }

        if (authService.register(creds[0], creds[1])) {
            sendMessage(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Регистрация успешна! Теперь войдите (LOGIN)")
                    .build());
        } else {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Пользователь уже существует")
                    .build());
        }
    }

    private boolean handleLogin(MessageDto request) {
        String[] creds = parseCredentials(request);
        if (creds == null) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Формат: {\"type\":\"LOGIN\",\"payload\":\"username:password\"}")
                    .build());
            return false;
        }

        Optional<User> userOpt = authService.login(creds[0], creds[1]);
        if (userOpt.isPresent()) {
            this.username = userOpt.get().getUsername();
            this.authenticated = true;
            sendMessage(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Вход выполнен! Добро пожаловать, " + username)
                    .build());
            return true;
        } else {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Неверный логин или пароль")
                    .build());
            return false;
        }
    }

    private String[] parseCredentials(MessageDto request) {
        String payload = request.getPayload() != null ? request.getPayload().toString() : 
                        (request.getText() != null ? request.getText() : "");
        
        if (payload.contains(":")) {
            String[] parts = payload.split(":", 2);
            if (parts.length == 2 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
                return parts;
            }
        }
        return null;
    }

    private void gameLoop() throws IOException {
        while (true) {
            String jsonFromClient = fromClient.readLine();
            if (jsonFromClient == null) break;

            MessageDto request;
            try {
                request = objectMapper.readValue(jsonFromClient, MessageDto.class);
            } catch (JsonProcessingException e) {
                sendMessage(MessageDto.builder()
                        .type("ERROR")
                        .text("Неверный формат JSON")
                        .build());
                continue;
            }

            if (room == null) continue;

            String type = request.getType();
            if (type == null) continue;
            
            String messageFromClient = type.toUpperCase();

            if (messageFromClient.equals("RESTART")) {
                if (!room.isGameActive()) {
                    room.restartGame();
                } else {
                    sendMessage(MessageDto.builder()
                            .type("ERROR")
                            .text("Игра еще идет!")
                            .build());
                }
                continue;
            }

            if (!room.isGameActive()) {
                sendMessage(MessageDto.builder()
                        .type("SYSTEM")
                        .text("Игра окончена. Введите RESTART для новой игры.")
                        .build());
                continue;
            }

            if (messageFromClient.equals("MOVE")) {
                handleMove(request);
            } else if (messageFromClient.equals("CHAT") || messageFromClient.equals("MESSAGE")) {
                handleChat(request);
            } else {
                sendMessage(MessageDto.builder()
                        .type("ERROR")
                        .text("Неизвестная команда")
                        .build());
            }
        }
    }

    private void handleMove(MessageDto request) {
        String move = request.getPayload() != null ? request.getPayload().toString() : request.getText();
        if (move != null) move = move.toUpperCase().trim();

        if (!room.isPlayerTurn(this)) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Ошибка: сейчас не ваш ход!")
                    .build());
            return;
        }

        if (!room.isValidMoveFormat(move)) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Ошибка: неверный формат!")
                    .build());
            return;
        }

        if (!room.makeMove(move, playerSymbol, username)) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Ошибка: клетка уже занята!")
                    .build());
            return;
        }

        MessageDto moveMsg = MessageDto.builder()
                .type("MOVE")
                .player(username)
                .text(username + " ходит: " + move)
                .payload(move)
                .build();
        room.broadcast(moveMsg, this);
        
        room.sendBoardToAll();

        String winner = room.checkWinner();
        if (winner != null) {
            room.setGameActive(false);
            if (winner.equals("DRAW")) {
                room.saveGameResult(null, null, "DRAW");
                room.broadcast(MessageDto.builder()
                        .type("GAME_OVER")
                        .text("Ничья!")
                        .build(), null);
                sendMessage(MessageDto.builder()
                        .type("SYSTEM")
                        .text("Ничья! Введите RESTART для новой игры.")
                        .build());
            } else if (winner.equals(playerSymbol)) {
                String opponent = getOpponentUsername();
                room.saveGameResult(username, opponent, "WIN");
                sendMessage(MessageDto.builder()
                        .type("GAME_OVER")
                        .text("Вы победили!")
                        .build());
                room.broadcast(MessageDto.builder()
                        .type("GAME_OVER")
                        .text("Вы проиграли! " + username + " победил!")
                        .build(), this);
            }
        } else {
            room.switchTurn();
            sendMessage(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Ход сделан. Ожидайте противника...")
                    .build());
            room.broadcast(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Ваш ход!")
                    .build(), this);
        }
    }

    private void handleChat(MessageDto request) {
        String text = request.getText();
        if (text == null || text.trim().isEmpty()) {
            sendMessage(MessageDto.builder()
                    .type("ERROR")
                    .text("Пустое сообщение")
                    .build());
            return;
        }
        
        room.saveChatMessage(username, text);
        
        room.broadcast(MessageDto.builder()
                .type("CHAT")
                .player(username)
                .text(username + ": " + text)
                .build(), this);
    }

    private String getOpponentUsername() {
        for (ClientHandler player : room.getPlayers()) {
            if (player != this) {
                return player.getUsername();
            }
        }
        return null;
    }
}