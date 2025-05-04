package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void add(User user) {
        manager.persist(user);
    }


    @Override
    public void update(User user, int id) {
        User newUser = manager.find(User.class, id);
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setAge(user.getAge());
    }

    @Override
    public List<User> viewAllUsers() {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = manager.createQuery(jpql, User.class);
        return query.getResultList();
    }

    @Override
    public User findUserById(int id) {
        return manager.find(User.class, id);

    }

    @Override
    public void delete(int id) {
        manager.remove(findUserById(id));
    }

    public User findUserByName(String name) {
        String s = "SELECT u FROM User u Where u.name=:name";
        TypedQuery query = manager.createQuery(s, User.class);
        query.setParameter("name", name);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void add(Role role){
        manager.persist();
    }

}