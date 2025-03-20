package m1.uasz.sn.dao;

import jakarta.persistence.*;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;

import java.util.List;

public class EtudiantDAO extends GenericDAO<Etudiant, String> {
    public EtudiantDAO() {
        super(Etudiant.class);
    }

    public Etudiant findById(String ine) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT f FROM Etudiant f WHERE f.ine = :nom", Etudiant.class);
            query.setParameter("nom", ine);
            return query.getResultStream().findFirst().orElse(null);
        }
    }

    public Etudiant findByEmail(String email) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT u FROM Etudiant u WHERE u.email = :email", Etudiant.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Etudiant> findEtudiantsByFormation(Formation formation) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e WHERE e.formation = :formation", Etudiant.class);
            query.setParameter("formation", formation);
            return query.getResultList();
        }
    }

    public List<Module> findModulesByEtudiant(Etudiant etudiant) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Module> query = em.createQuery(
                    "SELECT m FROM Module m JOIN m.etudiants e WHERE e.id = :etudiantId", Module.class);
            query.setParameter("etudiantId", etudiant.getIne());
            return query.getResultList();
        }
    }

    public void supprimerInscription(String ine, Long moduleCode) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Query query1 = em.createNativeQuery(
                    "DELETE FROM Etudiant_Module WHERE Etudiant_ine = ? AND modules_id = ?");
            query1.setParameter(1, ine);
            query1.setParameter(2, moduleCode);

            Query query2 = em.createNativeQuery(
                    "DELETE FROM Module_Etudiant WHERE etudiants_ine = ? AND Module_id = ?");
            query2.setParameter(1, ine);
            query2.setParameter(2, moduleCode);

            int deletedRows1 = query1.executeUpdate();
            int deletedRows2 = query2.executeUpdate();

            if (deletedRows1 > 0 && deletedRows2 > 0) {
                System.out.println("Désinscription réussie !");
            } else {
                System.out.println("Aucune inscription trouvée.");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em.isOpen()) {  // ✅ Vérification avant fermeture
                em.close();
            }
        }
    }

    public Module findModuleByEtudiant(Module module, Etudiant etudiant) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Module> query = em.createQuery(
                    "SELECT e FROM Module e JOIN e.etudiants m WHERE m.ine = :ine AND e.id = :id", Module.class);
            query.setParameter("id", module.getId());  // On passe l'ID pour éviter les problèmes d'entités détachées
            query.setParameter("ine", etudiant.getIne());
            return query.getResultStream().findFirst().orElse(null);
        }
    }
}
