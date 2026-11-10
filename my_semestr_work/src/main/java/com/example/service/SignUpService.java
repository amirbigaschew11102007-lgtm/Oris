package com.example.service;

import com.example.dto.SignUpForm;

import java.sql.SQLException;

public interface SignUpService {
    void signUp (SignUpForm form) throws SQLException;
}
