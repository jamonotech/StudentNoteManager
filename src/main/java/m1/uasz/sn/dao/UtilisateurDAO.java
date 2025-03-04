package m1.uasz.sn.dao;

import jakarta.persistence.NoResultException;
import m1.uasz.sn.models.Utilisateur;

public class UtilisateurDAO extends GenericDAO<Utilisateur, Long> {
    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Utilisateur findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
