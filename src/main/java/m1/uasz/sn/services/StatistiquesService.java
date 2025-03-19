package m1.uasz.sn.services;

import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //    Pour les formations
    public int getEffectifFormation(Formation formation) {
        return etudiantDAO.findEtudiantsByFormation(formation).size();
    }

    public int getNombreAdmisFormation(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);
        return (int) etudiants.stream()
                .filter(e -> etudiantService.calculerMoyenneEtudiant(e) >= 10)
                .count();
    }

    public double getTauxReussiteFormation(Formation formation) {
        int effectif = getEffectifFormation(formation);
        int admis = getNombreAdmisFormation(formation);
        return effectif > 0 ? ((double) admis / effectif) * 100 : 0;
    }

    public int getNombreMentionsFormation(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);
        return (int) etudiants.stream()
                .filter(e -> etudiantService.calculerMoyenneEtudiant(e) >= 12)
                .count();
    }

    public Etudiant getMeilleurEtudiantFormation(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);
        return etudiants.stream()
                .max(Comparator.comparingDouble(etudiantService::calculerMoyenneEtudiant))
                .orElse(null);
    }

    public double getMeilleureMoyenneFormation(Formation formation) {
        Etudiant meilleur = getMeilleurEtudiantFormation(formation);
        return (meilleur != null) ? etudiantService.calculerMoyenneEtudiant(meilleur) : 0.0;
    }

    private String determinerMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Non Admis";
    }
}
