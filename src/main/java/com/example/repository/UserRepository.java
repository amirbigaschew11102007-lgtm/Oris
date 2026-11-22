package com.example.repository;

import com.example.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository{
        Optional<User> findById(long id) throws SQLException;
        Optional<User> findByLogin(String login) throws SQLException;
        boolean existsByUsernameOrEmail(String username, String email) throws SQLException;
        long create(User user) throws SQLException;
        List<User> findAll();
    }
