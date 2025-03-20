package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;

import java.util.List;

public class ModuleDAO extends GenericDAO<Module, Long> {
    public ModuleDAO() {
        super(Module.class);
    }

    public Module findByCode(String code) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Module> query = em.createQuery(
                    "SELECT f FROM Module f WHERE f.code = :code", Module.class);
            query.setParameter("code", code);
            return query.getResultStream().findFirst().orElse(null);
        }
    }

    public List<Module> findModulesByFormation(Formation formation) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Module> query = em.createQuery(
                    "SELECT e FROM Module e WHERE e.formation = :formation", Module.class);
            query.setParameter("formation", formation);
            return query.getResultList();
        }
    }

    public List<Module> findModulesByEnseignant(Enseignant enseignant) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Module> query = em.createQuery(
                    "SELECT e FROM Module e WHERE e.enseignantResponsable = :enseignant", Module.class);
            query.setParameter("enseignant", enseignant);
            return query.getResultList();
        }
    }

    public List<Etudiant> findEtudiantsByModule(Module module) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e JOIN e.modules m WHERE m.id = :moduleId", Etudiant.class);
            query.setParameter("moduleId", module.getId());  // On passe l'ID pour éviter les problèmes d'entités détachées
            return query.getResultList();
        }
    }

    public Etudiant findEtudiantByModule(Module module, Etudiant etudiant) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e JOIN e.modules m WHERE m.id = :moduleId AND e.ine = :ine", Etudiant.class);
            query.setParameter("moduleId", module.getId());  // On passe l'ID pour éviter les problèmes d'entités détachées
            query.setParameter("ine", etudiant.getIne());
            return query.getResultStream().findFirst().orElse(null);
        }
    }
}
