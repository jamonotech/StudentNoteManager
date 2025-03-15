package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Utilisateur;

public class UtilisateurDAO extends GenericDAO<Utilisateur, Long> {
    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Utilisateur findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
