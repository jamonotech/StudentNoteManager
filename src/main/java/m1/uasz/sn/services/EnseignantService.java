package m1.uasz.sn.services;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;
import org.hibernate.Hibernate;

import java.util.List;

public class EnseignantService {
    private EnseignantDAO enseignantDAO;
    private ModuleDAO moduleDAO;

    public EnseignantService() {
        this.enseignantDAO = new EnseignantDAO();
        this.moduleDAO = new ModuleDAO();
    }

    public void ajouterEnseignant(Enseignant enseignant) {
        enseignantDAO.createEns(enseignant);
    }

    public Enseignant trouverEnseignantParId(Long matricule) {
        return enseignantDAO.findById(String.valueOf(matricule));
    }

    public Enseignant trouverEnseignant(String matricule) {
        return enseignantDAO.findByMatricule(matricule);
    }

    public List<Enseignant> listerEnseignants() {
        return enseignantDAO.findAll();
    }

    public void modifierEnseignant(Enseignant enseignant) {
        enseignantDAO.update(enseignant);
    }

    public void supprimerEnseignant(Enseignant enseignant) {
        enseignantDAO.delete(enseignant);
    }

    public void inscrireEnseignantModule(String ine, String moduleCode) {
        Enseignant enseignant = enseignantDAO.findByMatricule(ine);
        m1.uasz.sn.models.Module module = moduleDAO.findByCode(moduleCode);

        if (enseignant != null && module != null) {
            Hibernate.initialize(enseignant.getModules());
            enseignant.getModules().add(module);
            module.setEnseignantResponsable(enseignant);
            enseignantDAO.update(enseignant);
            moduleDAO.update(module);
            System.out.println("L'étudiant " + enseignant.getNom() + " a été inscrit au module " + module.getNom());
        } else {
            System.out.println("Étudiant ou module introuvable.");
        }
    }

    public void desinscrireEnseignantModule(String ine, String moduleCode) {
        Enseignant enseignant = enseignantDAO.findByMatricule(ine);
        m1.uasz.sn.models.Module module = moduleDAO.findByCode(moduleCode);

        if (enseignant != null && module != null) {
            Hibernate.initialize(enseignant.getModules());
            enseignant.getModules().remove(module);
            module.setEnseignantResponsable(null);
            enseignantDAO.update(enseignant);
            moduleDAO.update(module);
            System.out.println("L'étudiant " + enseignant.getNom() + " a été désinscrit du module " + module.getNom());
        } else {
            System.out.println("Étudiant ou module introuvable.");
        }
    }
}
