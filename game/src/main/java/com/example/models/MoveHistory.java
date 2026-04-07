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
public class MoveHistory {
    private Long id;
    private Long roomId;
    private String playerUsername;
    private String playerSymbol;
    private String position;
    private Integer moveOrder;
    private LocalDateTime createdAt;
}