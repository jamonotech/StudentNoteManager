package m1.uasz.sn.services;

import java.util.List;

import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.models.Etudiant;

public class EtudiantService {
    private final EtudiantDAO etudiantDAO;

    public EtudiantService() {
        this.etudiantDAO = new EtudiantDAO(); // Instanciation du DAO
    }

    public void ajouterEtudiant(Etudiant etudiant) {
        etudiantDAO.create(etudiant);
    }

    public void modifierEtudiant(Etudiant etudiant) {
        etudiantDAO.update(etudiant);
    }

    public void supprimerEtudiant(Long id) {
        etudiantDAO.delete(id);
    }

    public Etudiant trouverEtudiant(Long id) {
        return etudiantDAO.findById(id);
    }

    public List<Etudiant> listerEtudiants() {
        return etudiantDAO.findAll();
    }

    public void inscrireEtudiantAFormation(Long etudiantId, Long formationId) {
        etudiantDAO.inscrireEtudiantAFormation(etudiantId, formationId);
    }
    public  void desincrireEtudiantAFormation(long etudiantId,long formationId){
        etudiantDAO.desincrireEtudiantAFormation(etudiantId,formationId);
    }
    public void inscrireEtudiantAModole(Long etudiantId, Long formationId) {
        etudiantDAO.inscrireEtudiantAModole(etudiantId, formationId);
    }
    public  void desincrireEtudiantAModule(long etudiantId,long formationId){
        etudiantDAO.desincrireEtudiantAModule(etudiantId,formationId);
    }
}
