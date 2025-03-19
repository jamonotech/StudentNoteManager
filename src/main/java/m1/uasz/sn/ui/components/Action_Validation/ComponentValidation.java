package m1.uasz.sn.ui.components.Action_Validation;

import m1.uasz.sn.dao.*;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;
import m1.uasz.sn.services.*;

import javax.swing.*;

public class ComponentValidation implements FormValidationStrategy {
    private EtudiantService etudiantService = new EtudiantService();
    private EnseignantService enseignantService = new EnseignantService();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ResponsablePedagogiqueDAO responsablePedagogiqueDAO = new ResponsablePedagogiqueDAO();
    private FormationService formationService = new FormationService();
    private ModuleService moduleService = new ModuleService();
    private ModuleDAO moduleDAO = new ModuleDAO();
    private UtilisateurService utilisateurService = new UtilisateurService();

    @Override
    public boolean valider(JTextField[] fields) {
        return valider(fields, null);
    }

    @Override
    public boolean valider(JTextField[] fields, String calling) {
        return valider(fields, null, null);
    }

    @Override
    public boolean valider(JTextField[] fields, String callingComponent, String typeForm) {
        if (!isValidComponent(callingComponent)) {
            JOptionPane.showMessageDialog(null, "Composant inconnu !");
            return false;
        }

        // Vérification du rôle avant validation
        if (utilisateurService.getUtilisateurConnecte() != null) {
            if (!"RESPONSABLE".equalsIgnoreCase(utilisateurService.getUtilisateurConnecte().getRole()) && !"marks".equals(callingComponent)) {
                JOptionPane.showMessageDialog(null, "Vous n'êtes pas autorisé à valider ce formulaire ou à effectuer cette action !");
                return false;
            }
        }

        switch (callingComponent) {
            case "users", "profile":
                return validerUtilisateur(fields);
            case "students":
                return validerStudent(fields, typeForm);
            case "teachers":
                return validerTeacher(fields, typeForm);
            case "trainings":
                return validerFormation(fields);
            case "modules":
                return validerModule(fields);
            case "marks":
                return validerNote(fields);
            default:
                JOptionPane.showMessageDialog(null, "Validation non définie pour ce composant !");
                return false;
        }
    }

    private boolean validerUtilisateur(JTextField[] fields) {
        String role = fields[0].getText();
        String email = fields[3].getText();
        String password = fields[4].getText();
        String confirmPassword = fields[5].getText();

        if (!email.matches("^[A-Za-z0-9+_.-]+@zig-univ\\.sn$")) {
            JOptionPane.showMessageDialog(null, "Email invalide ! L'email doit se terminer par @zig-univ.sn");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas !");
            return false;
        }
        if (utilisateurService.getUtilisateurConnecte() != null && !role.equalsIgnoreCase("RESPONSABLE") && !role.equalsIgnoreCase("ENSEIGNANT")) {
            JOptionPane.showMessageDialog(null, "Role ou type utilisateur inconnu !");
            return false;
        }

        return true;
    }

    private boolean validerStudent(JTextField[] fields, String typeForm) {
        String matricule = fields[0].getText();
        String email = fields[6].getText();
        String ine = etudiantService.trouverEtudiant(matricule) != null ? etudiantService.trouverEtudiant(matricule).getIne() : null;
        String baseEmail = etudiantDAO.findByEmail(email) != null ? etudiantDAO.findByEmail(email).getEmail() : null;

        if (!email.matches("^[A-Za-z0-9+_.-]+@zig-univ\\.sn$")) {
            JOptionPane.showMessageDialog(null, "Email invalide ! L'email doit se terminer par @zig-univ.sn");
            return false;
        }
        if (baseEmail != null && typeForm.equals("create")) {
            JOptionPane.showMessageDialog(null, "Email déjà existant. Ils doivent être uniques !");
            return false;
        }
        if (ine != null && typeForm.equals("create")) {
            JOptionPane.showMessageDialog(null, "INE déjà existant. Ils doivent être uniques !");
            return false;
        }

        return true;
    }

    private boolean validerTeacher(JTextField[] fields, String typeForm) {
        String matricule = fields[0].getText();
        String email = fields[6].getText();
        String ine = enseignantService.trouverEnseignant(matricule) != null ? enseignantService.trouverEnseignant(matricule).getMatricule() : null;
        String baseEmail = enseignantDAO.findByEmail(email) != null ? enseignantDAO.findByEmail(email).getEmail() : null;

        if (!email.matches("^[A-Za-z0-9+_.-]+@zig-univ\\.sn$")) {
            JOptionPane.showMessageDialog(null, "Email invalide ! L'email doit se terminer par @zig-univ.sn");
            return false;
        }
        if (baseEmail != null && typeForm.equals("create")) {
            JOptionPane.showMessageDialog(null, "Email déjà existant. Ils doivent être uniques !");
            return false;
        }
        if (ine != null && typeForm.equals("create")) {
            JOptionPane.showMessageDialog(null, "INE déjà existant. Ils doivent être uniques !");
            return false;
        }

        return true;
    }

    private boolean validerFormation(JTextField[] fields) {
        String nomFormation = fields[0].getText();
        String niveauFormation = fields[1].getText();
        String responsable = fields[3].getText();
        String baseEmail = responsablePedagogiqueDAO.findByEmail(responsable) != null ? responsablePedagogiqueDAO.findByEmail(responsable).getEmail() : null;

        if (nomFormation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le nom de la formation ne peut pas être vide !");
            return false;
        }
        if (niveauFormation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le niveau de la formation ne peut pas être vide !");
            return false;
        }
        if (responsable.isEmpty()) {
            JOptionPane.showMessageDialog(null, "L'email du responsable de la formation ne peut pas être vide !");
            return false;
        }
        if (baseEmail == null) {
            JOptionPane.showMessageDialog(null, "Aucun enseignant avec cet email trouvé !");
            return false;
        }

        return true;
    }

    private boolean validerModule(JTextField[] fields) {
        String code = fields[0].getText();
        String nomModule = fields[1].getText();
        String volumeHoraire = fields[2].getText();
        String coef = fields[3].getText();
        String credits = fields[4].getText();
        String responsable = fields[5].getText();
        String formation = fields[6].getText();
        Enseignant baseEmail = enseignantService.trouverEnseignant(responsable) != null ? enseignantService.trouverEnseignant(responsable) : null;
        Formation formation1 = formationService.trouverFormationParNom(formation) != null ? formationService.trouverFormationParNom(formation) : null;;

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le code du module ne peut pas être vide !");
            return false;
        }
        if (nomModule.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le nom du module ne peut pas être vide !");
            return false;
        }
        if (volumeHoraire.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le volume horaire du module ne peut pas être vide !");
            return false;
        }
        if (coef.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le coefficient du module ne peut pas être vide !");
            return false;
        }
        if (credits.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le credit du module ne peut pas être vide !");
            return false;
        }
        if (baseEmail == null) {
            JOptionPane.showMessageDialog(null, "Aucun enseignant avec cette matricule trouvé !");
            return false;
        }
        if (formation1 == null) {
            JOptionPane.showMessageDialog(null, "Aucune formation avec ce nom trouvée !");
            return false;
        }

        try {
            int vH = Integer.parseInt(volumeHoraire);
            if (vH < 2) {
                JOptionPane.showMessageDialog(null, "Le volume horaire doit être supérieur à 2h !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un volume horaire valide !");
            return false;
        }
        try {
            int coefs = Integer.parseInt(coef);
            if (coefs < 1) {
                JOptionPane.showMessageDialog(null, "Le coefficient doit être supérieur à 1 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un coefficient valide !");
            return false;
        }
        try {
            int credit = Integer.parseInt(credits);
            if (credit < 1) {
                JOptionPane.showMessageDialog(null, "Le credit doit être supérieur à 1 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un credit valide !");
            return false;
        }

        return true;
    }

    private boolean validerNote(JTextField[] fields) {
        String cc = fields[0].getText();
        String exam = fields[1].getText();
        String ine = fields[2].getText();
        String code = fields[3].getText();
        Etudiant etudiant = etudiantService.trouverEtudiant(ine);
        Module module = moduleService.trouverModuleParCode(code);
        String ine1 = etudiant != null ? etudiant.getIne() : null;
        String codeM = module != null ? module.getCode() : null;

        if (!utilisateurService.getUtilisateurConnecte().getEmail().equals(module.getEnseignantResponsable().getEmail())) {
            JOptionPane.showMessageDialog(null, "Ce module ne vous est pas assigné, vous ne pouvez donc pas la noter !");
            return false;
        }

        if (ine1 == null) {
            JOptionPane.showMessageDialog(null, "Aucun etudiant avec cet identifiant trouvé !");
            return false;
        }
        if (codeM == null) {
            JOptionPane.showMessageDialog(null, "Aucun module avec ce code trouvé !");
            return false;
        }

        Etudiant et1 = moduleDAO.findEtudiantByModule(module, etudiant);
        if (et1 == null){
            JOptionPane.showMessageDialog(null, "Cet étudiant n'est pas inscrit à ce module !");
            return false;
        }

        try {
            double note = Double.parseDouble(cc);
            if (note < 0 || note > 20) {
                JOptionPane.showMessageDialog(null, "La note de controle doit être entre 0 et 20 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer une note valide !");
            return false;
        }
        try {
            double note = Double.parseDouble(exam);
            if (note < 0 || note > 20) {
                JOptionPane.showMessageDialog(null, "La note d'examen doit être entre 0 et 20 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer une note valide !");
            return false;
        }

        return true;
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
