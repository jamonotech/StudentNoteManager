package m1.uasz.sn.services;

import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;

import java.util.List;

public class StatistiquesService {
    private EtudiantDAO etudiantDAO;
    private FormationDAO formationDAO;
    private EtudiantService etudiantService;

    public StatistiquesService() {
        this.etudiantDAO = new EtudiantDAO();
        this.formationDAO = new FormationDAO();
        this.etudiantService = new EtudiantService();
    }

    public int getNombreEtudiants() {
        return etudiantDAO.findAll().size();
    }

    public int getNombreFormations() {
        return formationDAO.findAll().size();
    }

    public int getNombreAdmis() {
        List<Etudiant> etudiants = etudiantDAO.findAll();
        return (int) etudiants.stream()
                .filter(e -> etudiantService.calculerMoyenneEtudiant(e) >= 10)
                .count();
    }

    public double getTauxReussite() {
        int totalEtudiants = getNombreEtudiants();
        int admis = getNombreAdmis();
        return totalEtudiants > 0 ? ((double) admis / totalEtudiants) * 100 : 0;
    }

    public int getNombreMentions() {
        List<Etudiant> etudiants = etudiantDAO.findAll();
        return (int) etudiants.stream()
                .filter(e -> etudiantService.calculerMoyenneEtudiant(e) >= 12)
                .count();
    }

    public Etudiant getEtudiantMeilleureMoyenne() {
        List<Etudiant> etudiants = etudiantDAO.findAll();
        return etudiants.stream()
                .max((e1, e2) -> Double.compare(etudiantService.calculerMoyenneEtudiant(e1), etudiantService.calculerMoyenneEtudiant(e2)))
                .orElse(null);
    }

    public double getMeilleureMoyenne() {
        Etudiant meilleur = getEtudiantMeilleureMoyenne();
        return (meilleur != null) ? etudiantService.calculerMoyenneEtudiant(meilleur) : 0.0;
    }
}
