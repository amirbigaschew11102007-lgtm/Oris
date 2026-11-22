package com.example.service.impl;

import com.example.models.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import java.io.IOException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() throws IOException {
        return userRepository.findAll();
    }
}
