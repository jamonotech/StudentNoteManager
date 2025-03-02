package m1.uasz.sn.services;

import m1.uasz.sn.dao.ModuleDAO;

import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;

import java.util.List;

public class ModuleService {
    private final ModuleDAO moduleDAO;

    public ModuleService() {
        this.moduleDAO = new ModuleDAO();
    }

    public void ajouterModule(Module module) {
        moduleDAO.create(module);
    }

    public void modifierModule(Module module) {
        moduleDAO.update(module);
    }

    public void supprimerModule(Long id) {
        moduleDAO.delete(id);
    }

    public Module trouverModule(Long id) {
        return moduleDAO.findById(id);
    }

    public List<Module> listerModules() {
        return moduleDAO.findAll();
    }

    public List<Etudiant> getEtudiants(Long moduleId) {
        return moduleDAO.getEtudiants(moduleId);
    }
}
