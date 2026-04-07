package com.example.repositories;

import com.example.models.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ChatMessage message) {
        String sql = "INSERT INTO chat_messages (room_id, player_username, message) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, message.getRoomId(), message.getPlayerUsername(), message.getMessage());
    }
}