package com.example.repositories;

import com.example.models.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GameRoomRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GameRoom save(GameRoom room) {
        if (room.getId() == null) {
            String sql = "INSERT INTO game_rooms (room_number, status, player_x, player_o) VALUES (?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                room.getRoomNumber(), room.getStatus(), room.getPlayerX(), room.getPlayerO());
            room.setId(id);
        } else {
            String sql = "UPDATE game_rooms SET status = ?, player_x = ?, player_o = ?, finished_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, room.getStatus(), room.getPlayerX(), 
                room.getPlayerO(), room.getFinishedAt(), room.getId());
        }
        return room;
    }

    public Optional<GameRoom> findById(Long id) {
        String sql = "SELECT * FROM game_rooms WHERE id = ?";
        try {
            GameRoom room = jdbcTemplate.queryForObject(sql, 
                new BeanPropertyRowMapper<>(GameRoom.class), id);
            return Optional.ofNullable(room);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void updateStatus(Long roomId, String status) {
        String sql = "UPDATE game_rooms SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, roomId);
    }
}