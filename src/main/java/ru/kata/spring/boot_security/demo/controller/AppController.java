package ru.kata.spring.boot_security.demo.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AppController {
    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AppController(UserService userService, RoleService roleRepository) {
        this.userService = userService;
        this.roleService = roleRepository;

    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String exeptionMethod() {
        return "access-denied";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String doAvtorisation(Authentication authentication, Model model) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("users", userService.viewAll());
            return "common-dashboard";
        } else {
            User user = userService.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("Ошибка поиска пользователя в методе doAvtorisation"));
            model.addAttribute("user", user);
            return "user_page";

        }
    }


    @PostMapping("/admin/new")
    @Transactional
    public String createUser(
            @RequestParam("username") String username,
            @RequestParam("lastName") String lastName,
            @RequestParam("age") int age,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("roles") String roleName) {

        // Проверяем, существует ли пользователь с таким email
        if (userService.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists: " + email);
        }


        // Создаем нового пользователя
        User user = new User();
        user.setUsername(username);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        // Преобразуем строку роли в объект Role
        Set<Role> roles = new HashSet<>();
        Role role = roleService.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        roles.add(role);
        user.setRoles(roles);

        userService.add(user);
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/edit")
    public String updateUser(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("lastName") String lastName,
            @RequestParam("age") int age,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "roles", required = false) String roleName) { // Теперь roles — это строка

        // Получаем существующего пользователя из базы
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Обновляем основные поля
        existingUser.setUsername(username);
        existingUser.setLastName(lastName);
        existingUser.setAge(age);
        existingUser.setEmail(email);

        // Обновляем пароль, только если он был изменен
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(password);
        }

        // Обрабатываем роль
        if (roleName != null) {
            Set<Role> roles = new HashSet<>();
            Role role = roleService.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
            existingUser.setRoles(roles);
        }

        userService.update(existingUser);
        return "redirect:/dashboard";
    }

    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.err.println(user.getUsername() + " " + user.getLastName());
        model.addAttribute("user", user);
        return "user_page"; // Имя шаблона для страницы пользователя
    }


    @PostMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("users", userService.viewAll());
        return "common-dashboard"; // Имя шаблона для страницы администратора
    }


}
