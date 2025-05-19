package ru.kata.spring.boot_security.demo.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class AppController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello_page")
    public String showHelloPage() {
        return "hello_page";
    }

    @GetMapping("/dashboard")
    public String doAvtorisation(Authentication authentication, Model model) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("users", userService.viewAll());
            return "admin_page";
        } else {
            User user = userService.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("Ошибка поиска пользователя в методе doAvtorisation"));
            model.addAttribute("user", user);
            return "user_page";

        }
    }

    @GetMapping
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.viewAll());
        return "allUsers";
    }

    @GetMapping("admin/new")
    public String showFormAddUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("admin/new")
    @Transactional
    public String createUser(@ModelAttribute("user") User user) {
        if (userService.findByName(user.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));
        user.getRoles().add(userRole);
        userService.add(user);
        return "redirect:/dashboard";
    }

    @GetMapping("admin/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id).orElseThrow();
        if (user == null) {
            System.err.println("User not found with id: " + id);
        }
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("admin/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user); // Обновляем целиком
        return "redirect:/dashboard";
    }

    @PostMapping("admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/dashboard";
    }

    @GetMapping("admin/user_page")
    public String showUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id).orElseThrow();
        if (user == null) {
            System.err.println("User not found with id: " + id);
        }
        model.addAttribute("user", user);
        return "user_page";
    }
}
