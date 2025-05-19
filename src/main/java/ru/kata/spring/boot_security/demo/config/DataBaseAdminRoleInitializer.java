package ru.kata.spring.boot_security.demo.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Set;

@Component
public class DataBaseAdminRoleInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataBaseAdminRoleInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostConstruct
    public void initMethod() {
        initAdminAndUserRoles();
        initAdmin();
    }

    public void initAdminAndUserRoles() {
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
        }
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
            System.err.println("initAdminAndUserRoles");
        }
    }

    public void initAdmin() {
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Роль Admin не найдена"));
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Роль User не найдена"));
            admin.addRole(adminRole);
            admin.addRole(userRole);
            userRepository.save(admin);
        }
    }
}
