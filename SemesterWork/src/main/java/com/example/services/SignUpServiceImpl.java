package com.example.services;

import com.example.dto.SignUpForm;
import com.example.models.User;
import com.example.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class SingUpServiceImpl implements SingUpService{

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    public SingUpServiceImpl (UsersRepository usersRepository){
        this.usersRepository=usersRepository;
        this.passwordEncoder=new BCryptPasswordEncoder();
    }

    @Override
    public void singUp(SignUpForm form) throws SQLException {
        User user = User.builder()
                .name(form.getFirstName())
                .surname(form.getLastName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .build();

        usersRepository.save(user);
    }
}
