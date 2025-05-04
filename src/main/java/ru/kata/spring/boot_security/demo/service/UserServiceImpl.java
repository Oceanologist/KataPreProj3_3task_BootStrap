package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;

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
    public void add(User user) {
        userRepository.save(user);

    }

//    @Transactional
//    @Override
//    public void delete(int id) {
//        userDAO.delete(id);
//    }
//
//    @Transactional
//    @Override
//    public void update(User user, int id) {
//        userDAO.update(user, id);
//
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public User findById(int id) {
//        return userDAO.findUserById(id);
//
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<User> viewAllUsers() {
//        System.out.println("viewAllUsers method working");
//        List<User> userList = userDAO.viewAllUsers();
//        for (User user : userList) {
//            user.toString();
//        }
//
//        return userList;
//    }
//
//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails findUser = userRepository.findByUsername(username);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return findUser;
    }
//
//    void assignRoleToUser(String userName, Role roleName) {
//        userDAO.findUserByName(userName).addRole(roleName);
//
//    }

    void removeRoleFromUser(int userId, String roleName) {

    }
}