package m1.uasz.sn.services;

import java.util.ArrayList;
import java.util.List;

import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;

public class EtudiantService {
    private EtudiantDAO etudiantDAO;
    private FormationDAO formationDAO;
    private ModuleDAO moduleDAO;
    private NoteDAO noteDAO;

    private String determinerMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Non Admis";
    }

    public EtudiantService() {
        this.etudiantDAO = new EtudiantDAO();
        this.formationDAO = new FormationDAO();
        this.moduleDAO = new ModuleDAO();
        this.noteDAO = new NoteDAO();
    }

    public void ajouterEtudiant(Etudiant etudiant) {
        etudiantDAO.create(etudiant);
    }

    public Etudiant trouverEtudiant(String ine) {
        return etudiantDAO.findById(ine);
    }

    public List<Etudiant> listerEtudiantsParFormation(Formation formation) {
        return etudiantDAO.findEtudiantsByFormation(formation);
    }

    public List<Etudiant> listerEtudiants() {
        return etudiantDAO.findAll();
    }

    public void modifierEtudiant(Etudiant etudiant) {
        etudiantDAO.update(etudiant);
    }

    public void supprimerEtudiant(Etudiant etudiant) {
        etudiantDAO.delete(etudiant);
    }

    public void inscrireEtudiantFormation(String ine, Long formationId) {
        Etudiant etudiant = etudiantDAO.findById(ine);
        Formation formation = formationDAO.findById(formationId);

        if (etudiant != null && formation != null) {
            etudiant.setFormation(formation);
            etudiantDAO.update(etudiant);
            System.out.println("L'étudiant " + etudiant.getNom() + " a été inscrit à la formation " + formation.getNom());
        } else {
            System.out.println("Étudiant ou formation introuvable.");
        }
    }

    public void desinscrireEtudiantFormation(String ine) {
        Etudiant etudiant = etudiantDAO.findById(ine);

        if (etudiant != null) {
            etudiant.setFormation(null);
            etudiantDAO.update(etudiant);
            System.out.println("L'étudiant " + etudiant.getNom() + " a été désinscrit de sa formation.");
        } else {
            System.out.println("Étudiant introuvable.");
        }
    }

    public void inscrireEtudiantModule(String ine, Long moduleCode) {
        Etudiant etudiant = etudiantDAO.findById(ine);
        Module module = moduleDAO.findById(moduleCode);

        if (etudiant != null && module != null) {
            etudiant.getModules().add(module);
            etudiantDAO.update(etudiant);
            System.out.println("L'étudiant " + etudiant.getNom() + " a été inscrit au module " + module.getNom());
        } else {
            System.out.println("Étudiant ou module introuvable.");
        }
    }

    public void desinscrireEtudiantModule(String ine, Long moduleCode) {
        Etudiant etudiant = etudiantDAO.findById(ine);
        Module module = moduleDAO.findById(moduleCode);

        if (etudiant != null && module != null) {
            etudiant.getModules().remove(module);
            etudiantDAO.update(etudiant);
            System.out.println("L'étudiant " + etudiant.getNom() + " a été désinscrit du module " + module.getNom());
        } else {
            System.out.println("Étudiant ou module introuvable.");
        }
    }

    private double calculerMoyenneModule(Etudiant etudiant, Module module) {
        Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);

        if (note != null) {
            return (note.getNoteCC() * 0.4) + (note.getNoteExamen() * 0.6);
        }

        return 0.0; // Si l'étudiant n'a pas de note, on considère une moyenne de 0.
    }

    private double calculerMoyenneGenerale(Etudiant etudiant) {
        List<Module> modules = etudiant.getModules();
        if (modules.isEmpty()) return 0.0;

        double sommeMoyennes = 0;
        int totalCoefficients = 0;

        for (Module module : modules) {
            double moyenneModule = calculerMoyenneModule(etudiant, module);
            sommeMoyennes += moyenneModule * module.getCoefficient();
            totalCoefficients += module.getCoefficient();
        }

        return totalCoefficients > 0 ? sommeMoyennes / totalCoefficients : 0.0;
    }

    public void delibererEtudiants() {
        List<Etudiant> etudiants = etudiantDAO.findAll();
        List<String> resultats = new ArrayList<>();

        for (Etudiant etudiant : etudiants) {
            double moyenneGenerale = calculerMoyenneGenerale(etudiant);
            String mention = determinerMention(moyenneGenerale);
            String statut = moyenneGenerale >= 10 ? "Admis" : "Non Admis";

            resultats.add(String.format("%s %s | Moyenne: %.2f | Mention: %s | Statut: %s",
                    etudiant.getPrenoms(), etudiant.getNom(), moyenneGenerale, mention, statut));
        }

        // Trier les étudiants par ordre de mérite (moyenne décroissante)
        resultats.sort((a, b) -> Double.compare(
                Double.parseDouble(b.split("\\|")[1].replace("Moyenne:", "").trim()),
                Double.parseDouble(a.split("\\|")[1].replace("Moyenne:", "").trim())
        ));

        System.out.println("=== Résultats de la délibération ===");
        resultats.forEach(System.out::println);
    }

    public String genererReleveNotes(Etudiant etudiant) {
        List<Module> modules = etudiant.getModules();
        StringBuilder releve = new StringBuilder();

        releve.append("Relevé de notes de : ").append(etudiant.getPrenoms()).append(" ").append(etudiant.getNom()).append("\n");
        releve.append("INE : ").append(etudiant.getIne()).append("\n");
        releve.append("------------------------------------------------------\n");
        releve.append(String.format("%-20s %-10s %-10s %-10s\n", "Module", "CC", "Examen", "Moyenne"));

        for (Module module : modules) {
            Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);
            double moyenneModule = (note.getNoteCC() * 0.4) + (note.getNoteExamen() * 0.6);
            releve.append(String.format("%-20s %-10.2f %-10.2f %-10.2f\n", module.getNom(), note.getNoteCC(), note.getNoteExamen(), moyenneModule));
        }

        double moyenneGenerale = calculerMoyenneGenerale(etudiant);
        String mention = determinerMention(moyenneGenerale);

        releve.append("------------------------------------------------------\n");
        releve.append(String.format("Moyenne Générale : %.2f  |  Mention : %s\n", moyenneGenerale, mention));

        return releve.toString();
    }

    public String genererStatistiques(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);
        int totalEtudiants = etudiants.size();
        int admis = 0;
        int passable = 0, assezBien = 0, bien = 0, tresBien = 0;

        StringBuilder stats = new StringBuilder();
        stats.append("📊 Statistiques de la formation : ").append(formation.getNom()).append("\n");
        stats.append("Nombre total d'étudiants : ").append(totalEtudiants).append("\n\n");
        stats.append("Liste des admis : \n");
        stats.append(String.format("%-15s %-20s %-10s %-15s\n", "INE", "Nom", "Moyenne", "Mention"));
        stats.append("------------------------------------------------------\n");

        for (Etudiant etudiant : etudiants) {
            double moyenne = calculerMoyenneGenerale(etudiant);
            if (moyenne >= 10) {
                admis++;
                String mention = determinerMention(moyenne);

                switch (mention) {
                    case "Passable" -> passable++;
                    case "Assez Bien" -> assezBien++;
                    case "Bien" -> bien++;
                    case "Très Bien" -> tresBien++;
                }

                stats.append(String.format("%-15s %-20s %-10.2f %-15s\n", etudiant.getIne(), etudiant.getNom(), moyenne, mention));
            }
        }

        double tauxReussite = (totalEtudiants > 0) ? ((double) admis / totalEtudiants) * 100 : 0;

        stats.append("\n📈 Taux de Réussite : ").append(String.format("%.2f%%", tauxReussite)).append("\n");
        stats.append("📌 Mentions obtenues :\n");
        stats.append("   - Passable : ").append(passable).append("\n");
        stats.append("   - Assez Bien : ").append(assezBien).append("\n");
        stats.append("   - Bien : ").append(bien).append("\n");
        stats.append("   - Très Bien : ").append(tresBien).append("\n");

        return stats.toString();
    }

    public double calculerMoyenneEtudiant(Etudiant etudiant) {
        List<Module> modules = etudiant.getModules();
        if (modules.isEmpty()) return 0.0;

        double totalPoints = 0.0;
        int totalCoefficients = 0;

        for (Module module : modules) {
            Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);
            if (note == null) return -1; // Indique qu'une note est manquante

            double moyenneModule = (note.getNoteCC() * 0.4) + (note.getNoteExamen() * 0.6);
            totalPoints += moyenneModule * module.getCoefficient();
            totalCoefficients += module.getCoefficient();
        }

        return totalCoefficients == 0 ? 0.0 : totalPoints / totalCoefficients;
    }
}
