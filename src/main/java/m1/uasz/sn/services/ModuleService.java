package m1.uasz.sn.services;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.dao.ModuleDAO;

import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;

import java.util.List;

public class ModuleService {
    private ModuleDAO moduleDAO;
    private EnseignantDAO enseignantDAO;

    public ModuleService() {
        this.moduleDAO = new ModuleDAO();
        this.enseignantDAO = new EnseignantDAO();
    }

    public void ajouterModule(Module module) {
        moduleDAO.create(module);
    }

    public Module trouverModule(Long id) {
        return moduleDAO.findById(id);
    }

    public List<Module> listerModule() {
        return moduleDAO.findAll();
    }

    public void modifierModule(Module module) {
        moduleDAO.update(module);
    }

    public void supprimerModule(Module module) {
        moduleDAO.delete(module);
    }

    public void modifierModule(Long code, String nouveauNom, int nouveauVolumeHoraire, int nouveauCoefficient, int nouveauxCredits) {
        Module module = moduleDAO.findById(code);

        if (module != null) {
            module.setNom(nouveauNom);
            module.setVolumeHoraire(nouveauVolumeHoraire);
            module.setCoefficient(nouveauCoefficient);
            module.setCredits(nouveauxCredits);
            moduleDAO.update(module);
            System.out.println("Module modifié avec succès.");
        } else {
            System.out.println("Module introuvable.");
        }
    }

    public void affecterEnseignantAuModule(Long moduleCode, String matriculeEnseignant) {
        Module module = moduleDAO.findById(moduleCode);
        Enseignant enseignant = enseignantDAO.findById(matriculeEnseignant);

        if (module != null && enseignant != null) {
            module.setEnseignantResponsable(enseignant);
            moduleDAO.update(module);
            System.out.println("L'enseignant " + enseignant.getNom() + " est maintenant responsable du module " + module.getNom());
        } else {
            System.out.println("Module ou enseignant introuvable.");
        }
    }
}
