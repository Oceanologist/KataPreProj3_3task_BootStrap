package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDAO {
    void add(User user);

    void delete(int id);

    void update(User user, int id);

    List<User> viewAllUsers();

    User findUserById(int id);

    User findUserByName(String name);
}