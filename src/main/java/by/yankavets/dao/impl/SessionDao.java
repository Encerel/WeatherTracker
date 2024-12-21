package by.yankavets.dao.impl;

import by.yankavets.dao.Dao;
import by.yankavets.entity.SessionEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class SessionDao implements Dao<Integer, SessionEntity> {


    private final SessionFactory sessionFactory;

    @Autowired
    public SessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<SessionEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<SessionEntity> fromSessionEntity = session.createQuery("FROM SessionEntity", SessionEntity.class).list();

            session.getTransaction().commit();
            return fromSessionEntity;
        }
    }

    @Override
    public Optional<SessionEntity> findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            SessionEntity sessionEntity = session.find(SessionEntity.class, id);

            session.getTransaction().commit();
            return Optional.of(sessionEntity);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            SessionEntity sessionEntity = session.find(SessionEntity.class, id);
            session.remove(sessionEntity);

            session.getTransaction().commit();
            return true;
        }
    }

    @Override
    public SessionEntity save(SessionEntity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
            return SessionEntity.builder()
                    .id(entity.getId())
                    .build();
        }

    }
}
