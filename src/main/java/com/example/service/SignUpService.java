package com.example.service;

import com.example.dto.LoginForm;
import com.example.dto.SignUpForm;
import com.example.models.User;

import java.sql.SQLException;

public interface SignUpService {
    User signUp (SignUpForm form) throws SQLException;
    User login (LoginForm form) throws SQLException;
}
