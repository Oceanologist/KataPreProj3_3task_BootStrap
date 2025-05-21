package ru.kata.spring.boot_security.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String username;

    @Column(name = "lastName", unique = true)
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String lastName;

    @Column(name = "email", unique = true)
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String email;

    @Column(name = "age", nullable = false)
    @Min(1)
    @Max(150)
    private int age;

    @Size(min = 5, message = "Не меньше 5 знаков")
    @Column(name = "password")
    private String password;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public @Size(min = 2, message = "Не меньше 2 знаков") String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 2, message = "Не меньше 2 знаков") String lastName) {
        this.lastName = lastName;
    }

    public @Size(min = 2, message = "Не меньше 2 знаков") String getEmail() {
        return email;
    }

    public void setEmail(@Size(min = 2, message = "Не меньше 2 знаков") String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(String username, String lastName, String email, int age, String password) {
        this.username = username;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }


    public void addRole(Role role) {
        this.roles.add(role);
        //role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        //role.getUsers().remove(this);
    }

}

