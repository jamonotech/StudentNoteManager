package m1.uasz.sn.dao;

import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;

import java.util.List;

public class ModuleDAO extends GenericDAO<m1.uasz.sn.models.Module, Long> {
    public ModuleDAO() {
        super(Module.class);
    }

    public List<Module> findModulesByFormation(Formation formation) {
        TypedQuery<Module> query = entityManager.createQuery(
                "SELECT e FROM Module e WHERE e.formation = :formation",
                Module.class
        );
        query.setParameter("formation", formation);
        return query.getResultList();
    }

    public List<Module> findModulesByEnseignant(Enseignant enseignant) {
        TypedQuery<Module> query = entityManager.createQuery(
                "SELECT e FROM Module e WHERE e.enseignant = :enseignant",
                Module.class
        );
        query.setParameter("enseignant", enseignant);
        return query.getResultList();
    }

    public List<Etudiant> findEtudiantsByModules(Module module) {
        TypedQuery<Etudiant> query = entityManager.createQuery(
                "SELECT e FROM Etudiant e JOIN e.modules m WHERE m = :module",
                Etudiant.class
        );
        query.setParameter("module", module);
        return query.getResultList();
    }
}
