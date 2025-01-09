package by.yankavets.dao.impl;

import by.yankavets.dao.Dao;
import by.yankavets.model.api.entity.Coordinate;
import by.yankavets.model.entity.Location;
import by.yankavets.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static by.yankavets.constant.ParametersAndAttribute.*;

@Repository
public class LocationDao implements Dao<Integer, Location> {

    private final SessionFactory sessionFactory;

    @Autowired
    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Location> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Location> locations = session.createQuery("FROM Location", Location.class).list();
            session.getTransaction().commit();
            return locations;
        }
    }

    @Override
    public Optional<Location> findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Location location = session.createQuery(
                            "FROM Location l WHERE l.id = :id", Location.class)
                    .setParameter(ID, id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            session.getTransaction().commit();
            return Optional.ofNullable(location);
        }
    }

    public List<Location> findByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Location> locations = session.createQuery("FROM Location WHERE user = :user", Location.class)
                    .setParameter(USER, user)
                    .getResultList();

            session.getTransaction().commit();
            return locations;
        }
    }


    @Override
    public boolean delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Location location = session.find(Location.class, id);
            session.remove(location);

            session.getTransaction().commit();
            return true;
        }
    }

    @Override
    public Location save(Location entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
            return Location.builder()
                    .id(entity.getId())
                    .build();
        }
    }


    public Optional<Location> findByCoordinateAndUser(Coordinate coordinate, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Location location = session.createQuery(
                            "FROM Location WHERE user = :user AND latitude = :latitude AND longitude = :longitude", Location.class)
                    .setParameter(USER, user)
                    .setParameter(LATITUDE, coordinate.getLatitude())
                    .setParameter(LONGITUDE, coordinate.getLongitude())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            session.getTransaction().commit();
            return Optional.ofNullable(location);
        }
    }

}
