package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void add(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует выберите другое имя");
        } else {
            user.addRole("USER");
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {

        return userRepository.findById(id);

    }

    @Override
    public List<User> viewAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void update(Long userId, User updatedUser) {
        updatedUser.setId(userId); // Устанавливаем ID для обновления
        userRepository.save(updatedUser);
    }


    //    public void assignRoleToUser(String userName, Role roleName) {
//        userRepository.findByUsername(userName).addRole(roleName).orElseThrow(() ->
//                new UsernameNotFoundException("User not found: " + userName));
//
//    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username) // Optional<User>
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь " + username + " не найден"));
    }
}