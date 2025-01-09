package by.yankavets.dao.impl;

import by.yankavets.dao.Dao;
import by.yankavets.model.entity.SessionEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.SESSION_UUID;

@Repository
public class SessionDao implements Dao<UUID, SessionEntity> {


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
    public Optional<SessionEntity> findById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            SessionEntity sessionEntity = session.createQuery(
                            "FROM SessionEntity s WHERE s.id = :session_uuid", SessionEntity.class)
                    .setParameter(SESSION_UUID, id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            session.getTransaction().commit();
            return Optional.ofNullable(sessionEntity);
        }
    }

    @Override
    public boolean delete(UUID id) {
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
