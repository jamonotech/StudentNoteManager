package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public abstract class GenericDAO<T, ID> {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("club_persistence_unit");

    protected EntityManager entityManager;
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public void create(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    public void update(T entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    public void delete(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        entityManager.getTransaction().commit();
    }

    public void close() {
        if (entityManager != null) entityManager.close();
    }
}
