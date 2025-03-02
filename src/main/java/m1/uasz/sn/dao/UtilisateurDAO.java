package m1.uasz.sn.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Utilisateur;
import java.util.Optional;

public class UtilisateurDAO extends GenericDAO<Utilisateur, Long> {

    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Optional<Utilisateur> trouverParEmail(String email) {
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class
            );
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
