package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class AppController {
    private final UserService userService;


    @Autowired
    public AppController(UserService userService) {
        this.userService = userService;

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
            return "user_page";

        }
    }

    @GetMapping
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.viewAll());
        return "allUsers";
    }

    @GetMapping("users/new")
    public String showFormAddUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("users/new")
    public String createUser(@ModelAttribute("user") User user) {
        System.err.println("Creating user: " + user.getUsername() + ", password: " + user.getPassword());
        userService.add(user);
        System.err.println("попытка перехода");
        return "redirect:/dashboard";
    }

    @GetMapping("users/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id).orElseThrow();
        if (user == null) {
            System.err.println("User not found with id: " + id);
        }
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("users/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user); // Обновляем целиком
        return "redirect:/dashboard";
    }

    @PostMapping("users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/dashboard";
    }
}
