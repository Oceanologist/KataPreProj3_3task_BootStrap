package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.AppUser;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void add(AppUser user) {
        userRepository.save(user);

    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AppUser> findById(Long id) {

        return userRepository.findById(id);

    }

    @Transactional
    @Override
    public void update(AppUser updatedUser, Long id) {
        AppUser userModifiable = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("пользователь не найден"));
        userModifiable.setName(updatedUser.getName());
        userModifiable.setSurname(updatedUser.getSurname());
        userModifiable.setAge(updatedUser.getAge());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails findUser = userRepository.findByUsername(username);
        if (findUser == null) {
            throw new UsernameNotFoundException("пользователь не найден");
        }

        return findUser;
    }

    public void assignRoleToUser(String userName, Role roleName) {
        userRepository.findByUsername(userName).addRole(roleName);

    }

}