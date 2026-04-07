package com.example.repositories;

import com.example.models.MoveHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MoveHistoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(MoveHistory move) {
        String sql = "INSERT INTO move_history (room_id, player_username, player_symbol, position, move_order) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, move.getRoomId(), move.getPlayerUsername(), 
            move.getPlayerSymbol(), move.getPosition(), move.getMoveOrder());
    }

    public Integer getNextMoveOrder(Long roomId) {
        String sql = "SELECT COUNT(*) + 1 FROM move_history WHERE room_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomId);
    }
}