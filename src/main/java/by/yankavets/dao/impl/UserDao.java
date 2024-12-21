package by.yankavets.dao.impl;

import by.yankavets.dao.Dao;
import by.yankavets.entity.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements Dao<Integer, User> {


    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("FROM User", User.class).list();
            session.getTransaction().commit();
            return users;
        }
    }

    @Override
    public Optional<User> findById(Integer id) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            User user = session.find(User.class, id);
            session.getTransaction().commit();
            return Optional.of(user);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            User user = session.find(User.class, id);
            session.remove(user);
            session.getTransaction().commit();
            return true;
        }
    }

    @Override
    public User save(User entity) {
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return User.builder()
                    .id(entity.getId())
                    .build();
        }
    }
}
