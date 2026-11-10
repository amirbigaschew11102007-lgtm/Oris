package com.example.service;

import com.example.dto.SignUpForm;
import com.example.models.User;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class SignUpServiceImpl implements SignUpService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SignUpServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=new BCryptPasswordEncoder();
    }

    @Override
    public void signUp(SignUpForm form) throws SQLException {
        User user = User.builder()
                .name(form.getLogin())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .build();
        userRepository.save(user);
    }
}
