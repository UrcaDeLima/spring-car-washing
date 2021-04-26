package com.boots.service;

import com.boots.entity.User;

import java.util.List;

public interface UserService {
    User findUserById(Long userId);
    List<User> allUsers();
    boolean saveUser(User user);
    void deleteUser(Long userId);
}
