package m1.uasz.sn.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class GenericDAO<T, ID> {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("club_persistence_unit");
    protected EntityManager em = emf.createEntityManager();
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    public T findById(Long id) {  // Modification pour accepter tout type d'ID
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    public void update(T entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    public void delete(Long id) {  // Modification pour accepter tout type d'ID
        em.getTransaction().begin();
        T entity = em.find(entityClass, id);
        if (entity != null) em.remove(entity);
        em.getTransaction().commit();
    }
}
