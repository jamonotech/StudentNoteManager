package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Formation;

public class FormationDAO extends GenericDAO<Formation, Long> {
    public FormationDAO() {
        super(Formation.class);
    }

    public Formation findByName(String name) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Formation> query = em.createQuery(
                    "SELECT f FROM Formation f WHERE f.nom = :nom", Formation.class);
            query.setParameter("nom", name);
            return query.getResultStream().findFirst().orElse(null);
        }
    }

    public Formation findById(Long name) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Formation> query = em.createQuery(
                    "SELECT f FROM Formation f WHERE f.id = :nom", Formation.class);
            query.setParameter("nom", name);
            return query.getResultStream().findFirst().orElse(null);
        }
    }
}
