package m1.uasz.sn.services;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.models.Enseignant;

import java.util.List;

public class EnseignantService {
    private EnseignantDAO enseignantDAO;

    public EnseignantService() {
        this.enseignantDAO = new EnseignantDAO();
    }

    public void ajouterEnseignant(Enseignant enseignant) {
        enseignantDAO.create(enseignant);
    }

    public Enseignant trouverEnseignant(String matricule) {
        return enseignantDAO.findById(matricule);
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
}
