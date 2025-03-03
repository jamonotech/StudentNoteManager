package m1.uasz.sn.services;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.models.*;

import java.util.List;

public class EnseignantService {
    private final EnseignantDAO enseignantDAO;

    public EnseignantService() {
        this.enseignantDAO = new EnseignantDAO();
    }

    public void ajouterEnseignant(Enseignant enseignant) {
        enseignantDAO.create(enseignant);
    }

    public void modifierEnseignant(Enseignant enseignant) {
        enseignantDAO.update(enseignant);
    }

    public void supprimerEnseignant(Long id) {
        enseignantDAO.delete(id);
    }

    public Enseignant trouverEnseignant(Long id) {
        return enseignantDAO.findById(id);
    }

    public List<Enseignant> listerEnseignants() {
        return enseignantDAO.findAll();
    }

    public void affecterEnseignantAModule(Long enseignantId, Long moduleId) {
        enseignantDAO.affecterEnseignantAModule(enseignantId, moduleId);
    }
}
