package ru.kata.spring.boot_security.demo.config;

import jakarta.annotation.PostConstruct;
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
    public DataBaseAdminRoleInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initMethod() {
        initAdminAndUserRoles();
        initAdmin();
    }

    void initAdminAndUserRoles() {
        if (!roleRepository.existsByName("LORE_ADMIN")) {
            Role role = new Role();
            role.setRole("ROLE_ADMIN");
            roleRepository.save(role);
        }
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role role = new Role();
            role.setRole("ROLE_USER");
            roleRepository.save(role);
        }
    }


    void initAdmin() {
        if (!userRepository.findByUsername("admin").isPresent()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Роль АДМИН не создана"));
            User admin = new User();
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }
    }
}
