package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.AppUser;

import java.util.Optional;

public interface UserService {
    void add(AppUser user);

    void delete(Long id);

    void update(AppUser user, Long id);

    Optional<AppUser> findById(Long id);


}

