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
        userRepository.save(user);
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

    @Transactional
    @Override
    public void update(User updatedUser, Long id) {
        User userModifiable = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("пользователь не найден"));
        userModifiable.setName(updatedUser.getName());
        userModifiable.setSurname(updatedUser.getSurname());
        userModifiable.setAge(updatedUser.getAge());
    }




    public void assignRoleToUser(String userName, Role roleName) {
        userRepository.findByUsername(userName).addRole(roleName).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + userName));

    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username) // Optional<User>
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь " + username + " не найден"));
    }
}