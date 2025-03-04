package m1.uasz.sn.dao;

import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Formation;

public class FormationDAO extends GenericDAO<Formation, Long> {
    public FormationDAO() {
        super(Formation.class);
    }

    public Formation findByName(String name) {
        TypedQuery<Formation> query = entityManager.createQuery(
                "SELECT f FROM Formation f WHERE f.nom = :nom", Formation.class);
        query.setParameter("nom", name);

        return query.getResultStream().findFirst().orElse(null);
    }
}
