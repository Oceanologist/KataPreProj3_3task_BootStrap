package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entity.AppUser;


public interface UserRepository extends JpaRepository<AppUser, Long> {
     ru.kata.spring.boot_security.demo.entity.AppUser findByUsername(String username);
}