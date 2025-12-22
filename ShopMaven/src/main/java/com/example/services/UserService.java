package com.example.services;

import java.util.Optional;

import com.example.models.User;

public interface UserService {
    void create(User user);
    Optional<User> getById(Long id);
}
