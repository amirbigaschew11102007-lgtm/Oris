package com.example.service.impl;

import com.example.dto.LoginForm;
import com.example.dto.SignUpForm;
import com.example.exeptions.BusinessException;
import com.example.exeptions.ValidationExeption;
import com.example.models.Role;
import com.example.models.User;
import com.example.repository.UserRepository;
import com.example.service.SignUpService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User signUp(SignUpForm form) throws SQLException {
        Map<String, String> errors = new LinkedHashMap<>();

        String username = form.getUsername();
        String email = form.getEmail().toLowerCase();
        String password = form.getPassword();
        String passwordRepeat = form.getPasswordRepeat();

        if (!username.matches("[A-Za-z0-9_]{3,50}")) {
            errors.put("username", "Имя пользователя должно содержать от 3 до 50 латинских букв, цифр или знаков _.");
        }
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            errors.put("email", "Укажи корректный адрес электронной почты.");
        }
        if (password == null || password.length() < 8 || password.length() > 64) {
            errors.put("password", "Пароль должен содержать от 8 до 64 символов.");
        }
        if (passwordRepeat == null || !passwordRepeat.equals(password)) {
            errors.put("passwordRepeat", "Пароли не совпадают.");
        }

        if (!errors.containsKey("username") && !errors.containsKey("email")
                && userRepository.existsByUsernameOrEmail(username, email)) {
            errors.put("email", "Такое имя пользователя или email уже зарегистрированы.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationExeption(errors);
        }
        User user = User.builder()
                .name(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .city_id(form.getCity_id())
                .role(Role.USER)
                //короче изначально ставим User, а потом sql меняем на ADMIN
                .build();
        long userId = userRepository.create(user);
        return userRepository.findById(userId)
                .orElseThrow(() -> new SQLException("Пользователь создан, но не найден при повторном чтении."));
    }

    @Override
    public User login(LoginForm form) throws SQLException {
        if(form.getLogin()==null || form.getPassword()==null){
            throw new BusinessException("Отсутствует логин или пароль");
        }
        Optional<User> foundUser = userRepository.findByLogin(form.getLogin());
        if (foundUser.isEmpty()) {
            throw new BusinessException("пользователя не существует");
        }
        User user = foundUser.get();
        if(passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new BusinessException("неверный пароль");
        }
        return user;
    }
}
