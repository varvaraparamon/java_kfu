package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {
    private Long id;
    private Integer roomNumber;
    private String status; // 3 шт: WAITING, ACTIVE, FINISHED
    private String playerX;
    private String playerO;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}