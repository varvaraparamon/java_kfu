package com.example;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private static final int MAX_PLAYERS = 2;
    private List<ClientHandler> players = new ArrayList<>();
    private int roomId;
    private ClientHandler currentPlayer;
    private String[][] board = new String[3][3]; 
    private boolean gameActive = false;
    private int movesCount = 0;

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
                player.sendMessage("Вы в комнате " + roomId + ". Вы играете за X. Ожидаем второго игрока...");
            } else if (players.size() == 2) {
                player.setPlayerSymbol("O");
                player.sendMessage("Вы в комнате " + roomId + ". Вы играете за O.");
                startGame();
            }
            return true;
        }
        return false;
    }

    private void startGame() {
        gameActive = true;
        currentPlayer = players.get(0);
        broadcast("Игра началась! Ходит игрок X", null);
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

    public boolean makeMove(String move, String symbol) {
        int row = move.charAt(1) - '1';     
        int col = Character.toUpperCase(move.charAt(0)) - 'A'; 
        
        if (!board[row][col].equals(".")) {
            return false; 
        }
        
        board[row][col] = symbol;
        movesCount++;
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
        broadcast(sb.toString(), null);
    }

    public void restartGame() {
        clearBoard();
        gameActive = true;
        currentPlayer = players.get(0);
        broadcast("НОВАЯ ИГРА!!!!!!!!!!!", null);
        broadcast("Ходит игрок X", null);
        sendBoardToAll();
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler player : players) {
            if (player != sender) {
                player.sendMessage(message);
            }
        }
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }

    public ClientHandler getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void removePlayer(ClientHandler player) {
        players.remove(player);
    }
}