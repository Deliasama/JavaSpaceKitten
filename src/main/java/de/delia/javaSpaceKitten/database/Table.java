package de.delia.javaSpaceKitten.database;

import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;

public class Table <T> extends CustomEntityManager {

    private final Class<T> entityClass;

    public Table(Class<T> entityClass, EntityManagerFactory factory) {
        super(factory);
        this.entityClass = entityClass;
    }

    public Optional<T> getById(long id) {
        return getEntityManager(m -> Optional.ofNullable(m.find(entityClass, id)));
    }

    public T save(T entity) {
        return getEntityManager(m -> {
            m.getTransaction().begin();
            m.persist(entity);
            m.getTransaction().commit();
            return entity;
        });
    }

    public void remove(T entity) {
        getEntityManager(m -> {
            m.getTransaction().begin();
            m.remove(m.contains(entity) ? entity : m.merge(entity));
            m.getTransaction().commit();
            return null;
        });
    }
}
