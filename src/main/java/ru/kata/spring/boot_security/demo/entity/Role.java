package ru.kata.spring.boot_security.demo.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    @Column(name = "role",nullable = false)
    private String role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user-role_relationship",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns =@JoinColumn(name="user_id"))
    @Transient
    List<User> users =new ArrayList<>();

    public Role() {
    }

    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
               "id=" + id +
               ", role='" + role + '\'' +
               '}';
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
