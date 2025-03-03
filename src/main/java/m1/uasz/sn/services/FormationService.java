package m1.uasz.sn.services;

import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;

import java.util.List;

public class FormationService {
    private final FormationDAO formationDAO;

    public FormationService() {
        this.formationDAO = new FormationDAO();
    }

    public void ajouterFormation(Formation formation) {
        formationDAO.create(formation);
    }

    public void modifierFormation(Formation formation) {
        formationDAO.update(formation);
    }

    public void supprimerFormation(Long id) {
        formationDAO.delete(id);
    }

    public Formation trouverFormation(Long id) {
        return formationDAO.findById(id);
    }

    public List<Formation> listerFormations() {
        return formationDAO.findAll();
    }

    public List<Module> getModules(Long formationId) {
        return formationDAO.getModules(formationId);
    }

    public List<Enseignant> getEnseignants(Long formationId) {
        return formationDAO.getEnseignants(formationId);
    }
}
