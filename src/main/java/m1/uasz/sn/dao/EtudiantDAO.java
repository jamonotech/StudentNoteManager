package m1.uasz.sn.dao;

import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;

import java.util.List;

public class EtudiantDAO extends GenericDAO<Etudiant, String> {
    public EtudiantDAO() {
        super(Etudiant.class);
    }

    public List<Etudiant> findEtudiantsByFormation(Formation formation) {
        TypedQuery<Etudiant> query = entityManager.createQuery(
                "SELECT e FROM Etudiant e WHERE e.formation = :formation",
                Etudiant.class
        );
        query.setParameter("formation", formation);
        return query.getResultList();
    }

    public List<Module> findModulesByEtudiant(Etudiant etudiant) {
        TypedQuery<Module> query = entityManager.createQuery(
                "SELECT m FROM Module m JOIN m.etudiants e WHERE e = :etudiant",
                Module.class
        );
        query.setParameter("etudiant", etudiant);
        return query.getResultList();
    }
}
