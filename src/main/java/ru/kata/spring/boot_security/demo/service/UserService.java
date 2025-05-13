package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Optional;

public interface UserService {
    void add(User user);

    void delete(Long id);

    void update(User user, Long id);

    Optional<User> findById(Long id);


}

