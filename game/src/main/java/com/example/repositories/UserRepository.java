package com.example.repositories;

import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, 
                new BeanPropertyRowMapper<>(User.class), username.toLowerCase());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username.toLowerCase());
        return count != null && count > 0;
    }

    public User save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class, 
                user.getUsername().toLowerCase(), user.getPassword());
            user.setId(id);
        } else {
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getPassword(), user.getId());
        }
        return user;
    }
}