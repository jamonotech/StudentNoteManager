package m1.uasz.sn.ui.components.Action_Validation;

import m1.uasz.sn.dao.*;
import m1.uasz.sn.models.*;
import m1.uasz.sn.services.*;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.FormActionStrategy;
import m1.uasz.sn.ui.components.Module.*;
import m1.uasz.sn.ui.components.Enseignant.*;
import m1.uasz.sn.ui.components.Formation.*;
import m1.uasz.sn.ui.components.Etudiant.*;
import m1.uasz.sn.ui.components.Note.*;
import m1.uasz.sn.ui.components.Users.*;

import javax.swing.*;
import java.lang.Module;
import java.time.LocalDate;
import java.util.Date;

public class ComponentAction implements FormActionStrategy {
    private EtudiantService etudiantService = new EtudiantService();
    private ModuleService moduleService = new ModuleService();
    private FormationService formationService = new FormationService();
    private NoteService noteService = new NoteService();
    private EnseignantService enseignantService = new EnseignantService();
    private ResponsablePedagogiqueService responsablePedagogiqueService = new ResponsablePedagogiqueService();
    private UtilisateurService utilisateurService = new UtilisateurService();
    private StatistiquesService statistiquesService = new StatistiquesService();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private MainFrame mainFrame;

    public ComponentAction(){}
    public ComponentAction(MainFrame mainFrame){ this.mainFrame = mainFrame;}

    @Override
    public void executerApresValidation(JTextField[] textFields) {}
    @Override
    public void executerApresValidation(JComboBox<String>[] textFields, String callingComponent, String typeForm, String tabComponent, String ojectDetailed){};

    @Override
    public void executerApresValidation(JTextField[] textFields, String callingComponent, String typeForm) {
        if (!isValidComponent(callingComponent)) {
            JOptionPane.showMessageDialog(null, "Action impossible : Composant inconnu !");
            return;
        }

        switch (callingComponent) {
            case "users":
                traiterUtilisateur(textFields, typeForm);
                break;
            case "profile":
                traiterUtilisateurProfile(textFields, typeForm);
                break;
            case "students":
                traiterEtudiant(textFields, typeForm);
                break;
            case "teachers":
                traiterEnseignant(textFields, typeForm);
                break;
            case "trainings":
                traiterFormation(textFields, typeForm);
                break;
            case "modules":
                traiterModule(textFields, typeForm);
                break;
            case "marks":
                traiterNote(textFields, typeForm);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Action inconnue !");
                break;
        }
    }

    private void traiterUtilisateur(JTextField[] textFields, String typeForm) {
        String typeUser = textFields[0].getText();
        if (utilisateurService.getUtilisateurConnecte() == null) {
            typeUser = "ENSEIGNANT";
        }
        String nom = textFields[1].getText();
        String prenom = textFields[2].getText();
        String email = textFields[3].getText();
        String password = textFields[4].getText();

        switch (typeForm) {
            case "create":
                if (typeUser.equalsIgnoreCase("enseignant")){
                    Enseignant enss = new Enseignant(null, prenom, nom, null, null, null, email, null, null, null, null);
                    enss.setPassword(password);
                    enss.setRole("ENSEIGNANT");
                    enss.setNom(nom);
                    enss.setPrenom(prenom);
                    utilisateurService.enregistrerUtilisateur(enss, password);
                    enseignantService.ajouterEnseignant(enss);
                }
                else {
                    Utilisateur user = new ResponsablePedagogique(nom, prenom, email, password);
                    user.setNom(nom);
                    user.setPrenom(prenom);
                    user.setRole(typeUser.toUpperCase());
                    user.setPassword(password);
                    utilisateurService.enregistrerUtilisateur(user, password);
                }

                JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès !");
                break;
            case "update":
                Utilisateur user2 = utilisateurDAO.findByEmail(email);
                if (user2 == null) {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur avec cet email trouvé !");
                    break;
                }

                user2.setNom(nom);
                user2.setPrenom(prenom);
                user2.setRole(typeUser.toUpperCase());
                user2.setPassword(password);
                utilisateurService.modifierUtilisateur(user2);

                JOptionPane.showMessageDialog(null, "Utilisateur mis à jour avec succès !");
                mainFrame.updateMainContent(new UtilisateurPanel(mainFrame));
                break;
            case "delete":
                Utilisateur user1 = utilisateurDAO.findByEmail(email);
                utilisateurService.supprimerUtilisateur(user1);
                JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'utilisateur !");
                break;
        }
    }

    private void traiterUtilisateurProfile(JTextField[] textFields, String typeForm) {
        String typeUser = textFields[0].getText();
        String nom = textFields[1].getText();
        String prenom = textFields[2].getText();
        String email = textFields[3].getText();
        String password = textFields[4].getText();

        switch (typeForm) {
            case "create":
                Utilisateur user = new ResponsablePedagogique(nom, prenom, email, password);
                user.setNom(nom);
                user.setPrenom(prenom);
                user.setRole(typeUser.toUpperCase());
                user.setPassword(password);
                utilisateurService.enregistrerUtilisateur(user, password);

                JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès !");
                break;
            case "update":
                Utilisateur user2 = utilisateurDAO.findByEmail(email);
                if (user2 == null) {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur avec cet email trouvé !");
                    break;
                }

                user2.setNom(nom);
                user2.setPrenom(prenom);
                user2.setRole(typeUser.toUpperCase());
                user2.setPassword(password);
                utilisateurService.modifierUtilisateur(user2);

                JOptionPane.showMessageDialog(null, "Utilisateur mis à jour avec succès !");
                mainFrame.updateMainContent(new ProfileUser(mainFrame, user2));
                break;
            case "delete":
                Utilisateur user1 = utilisateurDAO.findByEmail(email);
                utilisateurService.supprimerUtilisateur(user1);
                JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'utilisateur !");
                break;
        }
    }

    private void traiterEnseignant(JTextField[] textFields, String typeForm) {
        String matricule = textFields[0].getText();
        String nom = textFields[1].getText();
        String prenom = textFields[2].getText();
        LocalDate dateNaiss = LocalDate.parse(textFields[3].getText());
        String sexe = textFields[4].getText();
        String adresse = textFields[5].getText();
        String email = textFields[6].getText();
        String grade = textFields[7].getText();
        String specialite = textFields[8].getText();
        String bureau = textFields[9].getText();
        String institution = textFields[10].getText();
//        String password = textFields[11].getText();

        switch (typeForm) {
            case "create":
                Enseignant user = new Enseignant(matricule, prenom, nom, dateNaiss, sexe, adresse, email, grade, specialite, bureau, institution);
                user.setPassword(matricule);
                user.setRole("ENSEIGNANT");
                user.setNom(nom);
                user.setPrenom(prenom);
                utilisateurService.enregistrerUtilisateur(user, matricule);
                enseignantService.ajouterEnseignant(user);
                Utilisateur ens1 = utilisateurDAO.findByEmail(email);
                Enseignant ens2 = enseignantDAO.findByEmail(email);
                System.out.println("user: "+ens1.getNom()+" "+ens1.getPrenom()+" ens: "+ens2.getNom()+" "+ens2.getPrenom());
                JOptionPane.showMessageDialog(null, "Enseignant ajouté avec succès !");
                break;
            case "update":
                Enseignant enseignant1 = enseignantDAO.findByEmail(email);
                if (enseignant1 == null) {
                    JOptionPane.showMessageDialog(null, "Aucun enseignant avec cet email trouvé !");
                    break;
                }

                enseignant1.setNom(nom); enseignant1.setPrenom(prenom); enseignant1.setDateNaissance(dateNaiss);
                enseignant1.setSexe(sexe); enseignant1.setAdresse(adresse); enseignant1.setGrade(grade);
                enseignant1.setSpecialite(specialite); enseignant1.setBureau(bureau); enseignant1.setInstitution(institution);

//                utilisateurService.modifierUtilisateur(user1);
                enseignantService.modifierEnseignant(enseignant1);
                JOptionPane.showMessageDialog(null, "Enseignant mis à jour avec succès !");
                mainFrame.updateMainContent(new EnseignantPanel(mainFrame));
                break;
            case "delete":
                Enseignant enseignant2 = new Enseignant(matricule, prenom, nom, dateNaiss, sexe, adresse, email, grade, specialite, bureau, institution);
                enseignantService.supprimerEnseignant(enseignant2);
                JOptionPane.showMessageDialog(null, "Enseignant supprimé avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'enseignant !");
                break;
        }
    }

    private void traiterEtudiant(JTextField[] textFields, String typeForm) {
        String ine = textFields[0].getText();
        String prenom = textFields[1].getText();
        String nom = textFields[2].getText();
        LocalDate dateNaiss = LocalDate.parse(textFields[3].getText());
        String sexe = textFields[4].getText();
        String adresse = textFields[5].getText();
        String email = textFields[6].getText();
        String formationNom = textFields[7].getText();
        Formation formation = formationService.trouverFormationParNom(formationNom);

        switch (typeForm) {
            case "create":
                Etudiant etudiant = new Etudiant(ine, prenom, nom, dateNaiss, sexe, adresse, email, formation);
                etudiantService.ajouterEtudiant(etudiant);
                JOptionPane.showMessageDialog(null, "Étudiant ajouté avec succès !");
                break;
            case "update":
                Etudiant etudiant1 = etudiantService.trouverEtudiant(ine);

                if (etudiant1 == null){
                    JOptionPane.showMessageDialog(null, "Aucun étudiant avec cet identifiant trouvé !");
                    break;
                }

                etudiant1.setNom(nom); etudiant1.setPrenoms(prenom); etudiant1.setDateNaissance(dateNaiss);
                etudiant1.setSexe(sexe); etudiant1.setAdresse(adresse); etudiant1.setEmail(email);
                etudiant1.setFormation(formation);

                etudiantService.modifierEtudiant(etudiant1);
                JOptionPane.showMessageDialog(null, "Étudiant mis à jour avec succès !");
                mainFrame.updateMainContent(new EtudiantPanel(mainFrame));
                break;
            case "delete":
                Etudiant etudiant2 = new Etudiant(ine, prenom, nom, dateNaiss, sexe, adresse, email, formation);
                etudiantService.supprimerEtudiant(etudiant2);
                JOptionPane.showMessageDialog(null, "Étudiant supprimé avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'étudiant !");
                break;
        }
    }

    private void traiterFormation(JTextField[] textFields, String typeForm) {
        String nomFormation = textFields[0].getText();
        String niveau = textFields[1].getText();
        String responsableNom = textFields[2].getText();
        String responsableEmail = textFields[3].getText();
        ResponsablePedagogique resp = new ResponsablePedagogiqueDAO().findByEmail(responsableEmail) != null ? new ResponsablePedagogiqueDAO().findByEmail(responsableEmail): null;

        switch (typeForm) {
            case "create":
                Formation formation = new Formation(nomFormation, niveau, responsableNom, responsableEmail);
                formation.setResponsablePedagogique(resp);
                formationService.ajouterFormation(formation);
                JOptionPane.showMessageDialog(null, "Formation ajoutée avec succès !");
                break;
            case "update":
                Formation formation1 = formationService.trouverFormationParNom(nomFormation);

                if (formation1 == null){
                    JOptionPane.showMessageDialog(null, "Aucune Formation trouvée avec ce nom !");
                    break;
                }
                formation1.setNiveau(niveau); formation1.setResponsableEmail(responsableEmail);
                formation1.setResponsableNom(responsableNom); formation1.setResponsablePedagogique(resp);

                formationService.modifierFormation(formation1);
                JOptionPane.showMessageDialog(null, "Formation mise à jour avec succès !");
                mainFrame.updateMainContent(new FormationPanel(mainFrame));
                break;
            case "delete":
                Formation formation2 = new Formation(nomFormation, niveau, responsableNom, responsableEmail);
                formationService.supprimerFormation(formation2);
                JOptionPane.showMessageDialog(null, "Formation supprimée avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour la formation !");
                break;
        }
    }

    private void traiterModule(JTextField[] textFields, String typeForm) {
        String code = textFields[0].getText();
        String nomModule = textFields[1].getText();
        int volumeHoraire = Integer.parseInt(textFields[2].getText());
        double coefficient = Double.parseDouble(textFields[3].getText());
        int credits = Integer.parseInt(textFields[4].getText());
        String enseignantMatricule = textFields[5].getText();
        String formationNom = textFields[6].getText();
        Enseignant enseignant = enseignantService.trouverEnseignant(enseignantMatricule);
        Formation formation = formationService.trouverFormationParNom(formationNom);

        switch (typeForm) {
            case "create":
                m1.uasz.sn.models.Module module1 = new m1.uasz.sn.models.Module(code, nomModule, volumeHoraire, coefficient, credits, enseignant, formation);
                moduleService.ajouterModule(module1);
                JOptionPane.showMessageDialog(null, "Module ajouté avec succès !");
                break;
            case "update":
                m1.uasz.sn.models.Module module2 = moduleService.trouverModuleParCode(code);

                if (module2 == null){
                    JOptionPane.showMessageDialog(null, "Aucun Module trouvé !");
                    break;
                }

                module2.setNom(nomModule); module2.setVolumeHoraire(volumeHoraire); module2.setCoefficient(coefficient);
                module2.setCredits(credits); module2.setEnseignantResponsable(enseignant); module2.setFormation(formation);

                moduleService.modifierModule(module2);
                JOptionPane.showMessageDialog(null, "Module mis à jour avec succès !");
                mainFrame.updateMainContent(new ModulePanel(mainFrame));
                break;
            case "delete":
                m1.uasz.sn.models.Module module3 = new m1.uasz.sn.models.Module(code, nomModule, volumeHoraire, coefficient, credits, enseignant, formation);
                moduleService.supprimerModule(module3);
                JOptionPane.showMessageDialog(null, "Module supprimé avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour le module !");
                break;
        }
    }

    private void traiterNote(JTextField[] textFields, String typeForm) {
        String ine = textFields[2].getText();
        String moduleCode = textFields[3].getText();
        double noteCC = Double.parseDouble(textFields[0].getText());
        double noteExamen = Double.parseDouble(textFields[1].getText());
        m1.uasz.sn.models.Module module = moduleService.trouverModuleParCode(moduleCode);
        Etudiant etudiant = etudiantService.trouverEtudiant(ine);
        boolean existModule = etudiantDAO.findModuleByEtudiant(module, etudiant) != null;

        switch (typeForm) {
            case "create":
                if (existModule){
                    Note note = new Note(noteCC, noteExamen, etudiant, module);
                    noteService.ajouterNote(note);
                    JOptionPane.showMessageDialog(null, "Note ajoutée avec succès !");
                } else {
                    JOptionPane.showMessageDialog(null, "Cet étudiant n'est pas inscrit à ce module !");
                }
                break;
            case "update":
                Note note1 = noteService.trouverNoteParEtudiantModule(etudiant, module);

                if (note1 == null) {
                    JOptionPane.showMessageDialog(null, "Aucune note trouvée !");
                    break;
                }

                note1.setNoteCC(noteCC); note1.setNoteExamen(noteExamen);

                noteService.modifierNote(note1);
                JOptionPane.showMessageDialog(null, "Note mise à jour avec succès !");
                mainFrame.updateMainContent(new NotePanel(mainFrame));
                break;
            case "delete":
                Note note2 = new Note(noteCC, noteExamen, etudiant, module);
                noteService.supprimerNote(note2);
                JOptionPane.showMessageDialog(null, "Note supprimée avec succès !");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opération inconnue pour la note !");
                break;
        }
    }

    private boolean isValidComponent(String callingComponent) {
        return callingComponent != null && (
                callingComponent.equals("users") ||
                        callingComponent.equals("profile") ||
                        callingComponent.equals("students") ||
                        callingComponent.equals("teachers") ||
                        callingComponent.equals("trainings") ||
                        callingComponent.equals("modules") ||
                        callingComponent.equals("marks")
        );
    }
}
