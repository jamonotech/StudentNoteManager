package m1.uasz.sn.services;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;

import java.util.List;

public class FormationService {
    private FormationDAO formationDAO;
    private ModuleDAO moduleDAO;

    public FormationService() {
        this.formationDAO = new FormationDAO();
        this.moduleDAO = new ModuleDAO();
    }

    public void ajouterFormation(Formation formation) {
        formationDAO.create(formation);
    }

    public Formation trouverFormation(Long id) {
        return formationDAO.findById(id);
    }

    public Formation trouverFormationParNom(String name) {
        return formationDAO.findByName(name);
    }

    public List<Formation> listerFormations() {
        return formationDAO.findAll();
    }

    public void modifierFormation(Formation formation) {
        formationDAO.update(formation);
    }

    public void supprimerFormation(Formation formation) {
        formationDAO.delete(formation);
    }

    public void ajouterModuleAFormation(Long formationId, Long moduleCode) {
        Formation formation = formationDAO.findById(formationId);
        Module module = moduleDAO.findById(moduleCode);

        if (formation != null && module != null) {
            formation.getModules().add(module);
            module.setFormation(formation);
            formationDAO.update(formation);
            moduleDAO.update(module);
            System.out.println("Module ajouté à la formation avec succès.");
        } else {
            System.out.println("Formation ou module introuvable.");
        }
    }

    public void supprimerModuleDeFormation(Long formationId, Long moduleCode) {
        Formation formation = formationDAO.findById(formationId);
        Module module = moduleDAO.findById(moduleCode);

        if (formation != null && module != null) {
            formation.getModules().remove(module);
            module.setFormation(null);
            formationDAO.update(formation);
            moduleDAO.delete(module);
            System.out.println("Module supprimé de la formation.");
        } else {
            System.out.println("Formation ou module introuvable.");
        }
    }
}
