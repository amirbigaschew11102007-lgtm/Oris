package com.example.repository;

import com.example.models.User;
import java.util.List;

public interface UserRepository extends CrubRepository<User> {
    List findAll();
}
