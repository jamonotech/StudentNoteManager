package m1.uasz.sn.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Utilisateur;
import java.util.List;
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

    public Optional<Utilisateur> trouverParId(Long id) {
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
        return Optional.ofNullable(utilisateur);
    }

    public boolean existeEmail(String email) {
        return trouverParEmail(email).isPresent();
    }

    public void supprimerParId(Long id) {
        em.getTransaction().begin();
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
        if (utilisateur != null) {
            em.remove(utilisateur);
        }
        em.getTransaction().commit();
    }

    public void mettreAJour(Utilisateur utilisateur) {
        em.getTransaction().begin();
        em.merge(utilisateur);
        em.getTransaction().commit();
    }
}
