package com.example;

import com.example.dto.MessageDto;
import com.example.models.GameRoom;
import com.example.models.MoveHistory;
import com.example.models.GameResult; 
import com.example.repositories.ChatMessageRepository;
import com.example.repositories.GameResultRepository;
import com.example.repositories.GameRoomRepository;
import com.example.repositories.MoveHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Room {
    private static final int MAX_PLAYERS = 2;
    private List<ClientHandler> players = new ArrayList<>();
    private int roomId;
    private Long dbRoomId; 
    private ClientHandler currentPlayer;
    private String[][] board = new String[3][3];
    private boolean gameActive = false;
    private int movesCount = 0;

    @Autowired
    private GameRoomRepository gameRoomRepository;
    
    @Autowired
    private MoveHistoryRepository moveHistoryRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private GameResultRepository gameResultRepository;

    public Room(int roomId) {
        this.roomId = roomId;
        clearBoard();
    }

    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ".";
            }
        }
        movesCount = 0;
    }

    public synchronized boolean addPlayer(ClientHandler player) {
        if (players.size() < MAX_PLAYERS) {
            players.add(player);
            player.setRoom(this);

            if (players.size() == 1) {
                player.setPlayerSymbol("X");
                GameRoom room = GameRoom.builder()
                    .roomNumber(this.roomId)
                    .status("WAITING")
                    .playerX(player.getUsername())
                    .build();
                GameRoom saved = gameRoomRepository.save(room);
                this.dbRoomId = saved.getId();
                
                player.sendMessage(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Вы в комнате " + roomId + ". Вы играете за X. Ожидаем второго игрока...")
                    .build());
            } else if (players.size() == 2) {
                player.setPlayerSymbol("O");
                GameRoom room = GameRoom.builder()
                    .id(this.dbRoomId)
                    .roomNumber(this.roomId)
                    .status("ACTIVE")
                    .playerX(players.get(0).getUsername())
                    .playerO(player.getUsername())
                    .build();
                gameRoomRepository.save(room);
                
                player.sendMessage(MessageDto.builder()
                    .type("SYSTEM")
                    .text("Вы в комнате " + roomId + ". Вы играете за O.")
                    .build());
                startGame();
            }
            return true;
        }
        return false;
    }

    private void startGame() {
        gameActive = true;
        currentPlayer = players.get(0);
        broadcast(MessageDto.builder().type("SYSTEM").text("Игра началась! Ходит игрок X").build(), null);
        sendBoardToAll();
    }

    public boolean isPlayerTurn(ClientHandler player) {
        return player == currentPlayer && gameActive;
    }

    public boolean isValidMoveFormat(String move) {
        if (move == null || move.length() != 2) return false;
        char letter = Character.toUpperCase(move.charAt(0));
        char digit = move.charAt(1);
        return letter >= 'A' && letter <= 'C' && digit >= '1' && digit <= '3';
    }

    public boolean makeMove(String move, String symbol, String username) {
        int row = move.charAt(1) - '1';
        int col = Character.toUpperCase(move.charAt(0)) - 'A';

        if (!board[row][col].equals(".")) {
            return false;
        }

        board[row][col] = symbol;
        movesCount++;
        
        Integer moveOrder = moveHistoryRepository.getNextMoveOrder(this.dbRoomId);
        MoveHistory history = MoveHistory.builder()
            .roomId(this.dbRoomId)
            .playerUsername(username)
            .playerSymbol(symbol)
            .position(move)
            .moveOrder(moveOrder)
            .build();
        moveHistoryRepository.save(history);
        
        return true;
    }

    public String checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals(".") && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0];
            }
        }
        for (int j = 0; j < 3; j++) {
            if (!board[0][j].equals(".") && board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j])) {
                return board[0][j];
            }
        }
        if (!board[0][0].equals(".") && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0];
        }
        if (!board[0][2].equals(".") && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2];
        }
        if (movesCount == 9) {
            return "DRAW";
        }
        return null;
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == players.get(0)) ? players.get(1) : players.get(0);
    }

    public void sendBoardToAll() {
        StringBuilder sb = new StringBuilder("\n  A B C\n");
        for (int i = 0; i < 3; i++) {
            sb.append(i + 1).append(" ");
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        broadcast(MessageDto.builder().type("BOARD").text(sb.toString()).build(), null);
    }

    public void restartGame() {
        clearBoard();
        gameActive = true;
        currentPlayer = players.get(0);
        gameRoomRepository.updateStatus(this.dbRoomId, "ACTIVE");
        broadcast(MessageDto.builder().type("SYSTEM").text("НОВАЯ ИГРА!!!!!!!!!!!").build(), null);
        broadcast(MessageDto.builder().type("SYSTEM").text("Ходит игрок X").build(), null);
        sendBoardToAll();
    }

    public void saveChatMessage(String username, String text) {
        com.example.models.ChatMessage msg = com.example.models.ChatMessage.builder()
            .roomId(this.dbRoomId)
            .playerUsername(username)
            .message(text)
            .build();
        chatMessageRepository.save(msg);
    }

    public void saveGameResult(String winner, String loser, String result) {
        GameResult gr = GameResult.builder()
            .roomId(this.dbRoomId)
            .winnerUsername(winner)
            .loserUsername(loser)
            .result(result)
            .build();
        gameResultRepository.save(gr);
        
        GameRoom room = GameRoom.builder()
            .id(this.dbRoomId)
            .status("FINISHED")
            .finishedAt(LocalDateTime.now())
            .build();
        gameRoomRepository.save(room);
    }

    public List<ClientHandler> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void broadcast(String message, ClientHandler sender) {
        broadcast(MessageDto.builder().type("SYSTEM").text(message).build(), sender);
    }

    public void broadcast(MessageDto message, ClientHandler sender) {
        for (ClientHandler player : players) {
            if (player != sender) {
                player.sendMessage(message);
            }
        }
    }

    public boolean isGameActive() { return gameActive; }
    public void setGameActive(boolean active) { this.gameActive = active; }
    public ClientHandler getCurrentPlayer() { return currentPlayer; }
    public boolean isFull() { return players.size() >= MAX_PLAYERS; }
    public boolean isEmpty() { return players.isEmpty(); }
    public void removePlayer(ClientHandler player) { players.remove(player); }
}