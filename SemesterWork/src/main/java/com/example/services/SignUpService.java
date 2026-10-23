package com.example.services;

import com.example.dto.SignUpForm;

import java.sql.SQLException;

public interface SingUpService {
    void singUp (SignUpForm form) throws SQLException;
}
