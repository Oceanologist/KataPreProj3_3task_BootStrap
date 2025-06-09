package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;

import java.util.Set;
@Data
public class UserRequestDTO {
    private Long id;
    private String username;
    private String lastName;
    private String email;
    private int age;
    private Set<String> roles;
    private String password;

}
