package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.ResponsablePedagogique;
import m1.uasz.sn.models.Utilisateur;

public class ResponsablePedagogiqueDAO extends GenericDAO<ResponsablePedagogique, Long> {
    public ResponsablePedagogiqueDAO() {
        super(ResponsablePedagogique.class);
    }

    public ResponsablePedagogique findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<ResponsablePedagogique> query = em.createQuery(
                    "SELECT u FROM ResponsablePedagogique u WHERE u.email = :email", ResponsablePedagogique.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
