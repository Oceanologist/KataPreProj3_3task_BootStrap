package ru.kata.spring.boot_security.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

@Component
public class DataBaseAdminRoleInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataBaseAdminRoleInitializer(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    void initAdminAndUserRoles() {
        if (!roleRepository.existsByName("ADMIN")) {
            Role role = new Role();
            role.setRole("ADMIN");
            roleRepository.save(role);
        }
        if (!roleRepository.existsByName("USER")) {
            Role role = new Role();
            role.setRole("USER");
            roleRepository.save(role);
        }
    }


    @PostConstruct
    void initAdmin(){
        if(!userRepository.findByUsername("admin").isPresent()){
            User admin=new User();
            admin.setName("admin");
            admin.setPassword("admin123");
            admin.setRoles(roleRepository.findByName("ADMIN"));
            userRepository.save(admin);
        }
    }
}
