package com.example.repositories;

import com.example.models.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameResultRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(GameResult result) {
        String sql = "INSERT INTO game_results (room_id, winner_username, loser_username, result) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, result.getRoomId(), result.getWinnerUsername(), 
            result.getLoserUsername(), result.getResult());
    }
}