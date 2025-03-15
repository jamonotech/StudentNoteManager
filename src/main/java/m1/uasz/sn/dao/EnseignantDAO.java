package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Formation;

public class EnseignantDAO extends GenericDAO<Enseignant, String> {
    public EnseignantDAO() {
        super(Enseignant.class);
    }

    public Enseignant findByMatricule(String matricule) {
        EntityManager em = getEntityManager();
        TypedQuery<Enseignant> query = em.createQuery(
                "SELECT f FROM Enseignant f WHERE f.matricule = :matricule", Enseignant.class);
        query.setParameter("matricule", matricule);
        return query.getResultStream().findFirst().orElse(null);
    }

    public Enseignant findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Enseignant> query = em.createQuery(
                    "SELECT u FROM Enseignant u WHERE u.email = :email", Enseignant.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
