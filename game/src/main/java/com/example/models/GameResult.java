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
public class GameResult {
    private Long id;
    private Long roomId;
    private String winnerUsername;
    private String loserUsername;
    private String result; // 2 шт: WIN, DRAW
    private LocalDateTime finishedAt;
}