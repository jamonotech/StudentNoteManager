package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.ResponsablePedagogique;

import java.util.List;

public abstract class GenericDAO<T, ID> {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("club_persistence_unit");

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public void create(T entity) {
        executeTransaction(em -> em.persist(entity));
    }

    public T findById(ID id) {
        try (EntityManager em = getEntityManager()) {
            return em.find(entityClass, id);
        }
    }

    public List<T> findAll() {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        }
    }

    public void update(T entity) {
        executeTransaction(em -> em.merge(entity));
    }

    public void delete(T entity) {
        executeTransaction(em -> {
            T attachedEntity = entity;
            if (!em.contains(attachedEntity)) {
                attachedEntity = em.merge(attachedEntity);
            }
            em.remove(attachedEntity);
        });
    }

    public void deleteById(ID id) {
        executeTransaction(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    public void createEns(Enseignant enseignant) {
        executeTransaction(em -> {
            if (enseignant.getId() != null && em.find(Enseignant.class, enseignant.getId()) != null) {
                em.merge(enseignant);
            } else {
                em.persist(enseignant);
            }
        });
    }

    public void createResp(ResponsablePedagogique enseignant) {
        executeTransaction(em -> {
            if (enseignant.getId() != null) {
                em.merge(enseignant);
            } else {
                em.persist(enseignant);
            }
        });
    }

    public void executeTransaction(TransactionAction action) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            action.execute(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    public interface TransactionAction {
        void execute(EntityManager em);
    }
}
