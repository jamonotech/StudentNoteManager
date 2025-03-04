package m1.uasz.sn.dao;

import java.util.List;

import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;

public class EtudiantDAO extends GenericDAO<Etudiant, String> {
    public EtudiantDAO() {
        super(Etudiant.class);
    }

   public void inscrireEtudiantAFormation(Long etudiantId, Long formationId) {
        em.getTransaction().begin();
        Etudiant etudiant = findById(etudiantId);
        Formation formation = em.find(Formation.class, formationId);
        if (etudiant != null && formation != null) {
            etudiant.setFormation(formation);
            em.merge(etudiant);
        }
        em.getTransaction().commit();
    }


    public void desinscrireEtudiantAFormation(long etudiantId, long formationId) {
        em.getTransaction().begin();
        
        // Suppression de la relation dans la table de jointure
        em.createQuery("DELETE FROM Etudiant_Formation ef WHERE ef.etudiant.id = :etudiantId AND ef.formation.id = :formationId")
          .setParameter("etudiantId", etudiantId)
          .setParameter("formationId", formationId)
          .executeUpdate();
        
        em.getTransaction().commit();
    }
    
    
    public void inscrireEtudiantAModulr(Long etudiantId, Long moduleId) {
        em.getTransaction().begin();
        Etudiant etudiant = findById(etudiantId);
        Module module = em.find(Module.class, moduleId);
        if (etudiant != null && module != null) {
            etudiant.setModule(module);
            em.merge(etudiant);
        }
        em.getTransaction().commit();
    }

    public void desincrireEtudiantAModule(long etudiantId, long formationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desincrireEtudiantAModule'");
    }
}
