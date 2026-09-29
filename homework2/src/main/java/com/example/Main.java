package com.example;

import com.example.model.User;
import com.example.repository.UserRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        userRepository.addSixUsers();

        List<User> users = userRepository.findUsersOlderThan(20);

        System.out.println("Пользователи старше 20 лет:");

        for (User user : users) {
            System.out.println(user);
        }
    }
}