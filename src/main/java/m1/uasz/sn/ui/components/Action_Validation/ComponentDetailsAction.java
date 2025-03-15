package m1.uasz.sn.ui.components.Action_Validation;

import m1.uasz.sn.dao.EnseignantDAO;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.*;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Enseignant.EnseignantPanel;
import m1.uasz.sn.ui.components.Etudiant.EtudiantPanel;
import m1.uasz.sn.ui.components.Formation.FormationPanel;
import m1.uasz.sn.ui.components.Module.ModulePanel;
import m1.uasz.sn.ui.components.Note.NotePanel;
import m1.uasz.sn.ui.components.Users.UtilisateurPanel;
import org.hibernate.Hibernate;

import javax.swing.*;
import java.time.LocalDate;

public class ComponentDetailsAction implements FormActionStrategy {
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

    public ComponentDetailsAction() {}
    public ComponentDetailsAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void executerApresValidation(JTextField[] textFields) {}
    @Override
    public void executerApresValidation(JTextField[] comboBoxes, String callingComponent, String typeForm) {}

    @Override
    public void executerApresValidation(JComboBox<String>[] comboBoxes, String callingComponent, String typeForm, String tabComponent, String ojectDetailed) {
        if (!isValidComponent(callingComponent)) {
            JOptionPane.showMessageDialog(null, "Action impossible : Composant inconnu !");
            return;
        }

        switch (callingComponent) {
            case "users":
                traiterUtilisateur(comboBoxes, typeForm);
                break;
            case "students":
                traiterEtudiant(comboBoxes, typeForm, ojectDetailed);
                break;
            case "teachers":
                traiterEnseignant(comboBoxes, typeForm, ojectDetailed);
                break;
            case "trainings":
                traiterFormation(comboBoxes, typeForm, tabComponent, ojectDetailed);
                break;
            case "modules":
                traiterModule(comboBoxes, typeForm, ojectDetailed);
                break;
            case "marks":
                traiterNote(comboBoxes, typeForm);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Action inconnue !");
                break;
        }
    }

    private void traiterUtilisateur(JComboBox[] comboBoxes, String typeForm) {
        String typeUser = comboBoxes[0].getSelectedItem().toString();

        if (typeForm.equals("create")) {
            Utilisateur user = new ResponsablePedagogique("", "", "", "");
            user.setRole(typeUser.toUpperCase());
            utilisateurService.enregistrerUtilisateur(user, "defaultPassword");
            JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Opération inconnue pour l'utilisateur !");
        }
    }

    private void traiterEnseignant(JComboBox[] comboBoxes, String typeForm, String ojectDetailed) {
        if (!comboBoxes[0].getSelectedItem().toString().equals("Sélectionner...")) {
            String codeModule = comboBoxes[0].getSelectedItem().toString().split(" ")[0];

            // Récupérer l'enseignant à partir de son matricule (INE)
            Enseignant enseignantObj = enseignantService.trouverEnseignant(ojectDetailed);
            Module module = moduleService.trouverModuleParCode(codeModule);

            if (enseignantObj == null) {
                JOptionPane.showMessageDialog(null, "Erreur : Enseignant introuvable !");
                return;
            }

            // Vérifier si l'enseignant est déjà inscrit au module
            Hibernate.initialize(enseignantObj.getModules());
            boolean moduleDejaAssigne = enseignantObj.getModules().stream()
                    .anyMatch(m -> m.getCode().equals(codeModule));
            boolean moduleEns = module.getEnseignantResponsable() != null;
            Enseignant resp = module.getEnseignantResponsable();

            if (moduleDejaAssigne) {
                JOptionPane.showMessageDialog(null, "L'enseignant est déjà inscrit à ce module !");
            } else if (moduleEns) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Module déjà assigné à l'enseignant "+
                        resp.getPrenom()+" "+resp.getNom()+" ! Confirmez-vous le changement?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    enseignantService.inscrireEnseignantModule(ojectDetailed, codeModule);
                    JOptionPane.showMessageDialog(null, "Module réassigné avec succès !");
                }
            } else if (typeForm.equals("create")) {
                enseignantService.inscrireEnseignantModule(ojectDetailed, codeModule);
                JOptionPane.showMessageDialog(null, "Module assigné avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'enseignant !");
            }
        }
    }

    private void traiterEtudiant(JComboBox[] comboBoxes, String typeForm, String objectDetailed) {
        if (!comboBoxes[0].getSelectedItem().toString().equals("Sélectionner...")) {
            String codeModule = comboBoxes[0].getSelectedItem().toString().split(" ")[0];

            // Récupérer l'enseignant à partir de son matricule (INE)
            Etudiant etudiantObj = etudiantService.trouverEtudiant(objectDetailed);
            Module module = moduleService.trouverModuleParCode(codeModule);

            if (etudiantObj == null) {
                JOptionPane.showMessageDialog(null, "Erreur : Etudiant introuvable !");
                return;
            }

            // Vérifier si l'enseignant est déjà inscrit au module
            Hibernate.initialize(etudiantObj.getModules());
            boolean moduleDejaAssigne = etudiantObj.getModules().stream()
                    .anyMatch(m -> m.getCode().equals(codeModule));

            boolean isFormationValid = module.getFormation().getNom().equals(etudiantObj.getFormation().getNom());

            if (moduleDejaAssigne) {
                JOptionPane.showMessageDialog(null, "L'etudiant est déjà inscrit à ce module !");
            } else if(!isFormationValid) {
                JOptionPane.showMessageDialog(null, "L'etudiant est déjà inscrit à une autre formation différente de celle du module !");
            } else if (typeForm.equals("create")) {
                etudiantService.inscrireEtudiantModule(objectDetailed, codeModule);
                JOptionPane.showMessageDialog(null, "Module assigné avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'etudiant !");
            }
        }
    }

    private void traiterFormation(JComboBox[] comboBoxes, String typeForm, String tabComponent, String ojectDetailed) {
        if (!comboBoxes[0].getSelectedItem().toString().equals("Sélectionner...")) {
            if (tabComponent.equals("modules")){
                String moduleCode = comboBoxes[0].getSelectedItem().toString().split(" ")[0];

                // Récupérer le module à partir de son code
                Module module = moduleService.trouverModuleParCode(moduleCode);
                Formation formation = formationService.trouverFormationParNom(ojectDetailed);

                if (module == null || formation == null) {
                    JOptionPane.showMessageDialog(null, "Erreur : Module ou Formation introuvable !");
                    return;
                }

                // Vérifier si le module est déjà assigné à la formation
                Hibernate.initialize(formation.getModules());
                boolean moduleDejaAssigne = formation.getModules().stream()
                        .anyMatch(m -> m.getCode().equals(module.getCode()));
                boolean moduleForm = module.getFormation() != null;
                Formation formation1 = module.getFormation();

                if (moduleDejaAssigne) {
                    JOptionPane.showMessageDialog(null, "Ce module est déjà assigné à cette formation !");
                } else if (moduleForm) {
                    int confirmation = JOptionPane.showConfirmDialog(null, "Module déjà assigné à la formation "+
                            formation1.getNom()+" ! Confirmez-vous le changement?");
                    if (confirmation == JOptionPane.YES_OPTION) {
                        moduleService.ajouterModuleFormation(module.getCode(), formation.getId());
                        JOptionPane.showMessageDialog(null, "Module réassigné avec succès !");
                    }
                } else if (typeForm.equals("create")) {
                    moduleService.ajouterModuleFormation(module.getCode(), formation.getId());
                    JOptionPane.showMessageDialog(null, "Module ajouté avec succès !");
                }
            } else if(tabComponent.equals("students")){
                String ine = comboBoxes[0].getSelectedItem().toString().split(" ")[0];

                // Récupérer l'enseignant à partir de son matricule (INE)
                Formation formation = formationService.trouverFormationParNom(ojectDetailed);
                Etudiant etudiant = etudiantService.trouverEtudiant(ine);

                if (etudiant == null || formation == null) {
                    JOptionPane.showMessageDialog(null, "Erreur : Etudiant ou Formation introuvable !");
                    return;
                }

                // Vérifier si l'enseignant est déjà inscrit au module
                boolean isEtud = etudiant.getFormation() != null;
                Formation etudFormation = etudiant.getFormation();
                boolean existant = etudFormation != null && etudFormation.getNom().equals(ojectDetailed);

                if (existant) {
                    JOptionPane.showMessageDialog(null, "L'étudiant est déjà inscrit à cette formation !");
                } else if (isEtud) {
                    int confirmation = JOptionPane.showConfirmDialog(null, "Etudiant déjà inscrit à la formation " +
                            ojectDetailed + " ! Confirmez-vous le changement?");
                    if (confirmation == JOptionPane.YES_OPTION) {
                        etudiantService.inscrireEtudiantFormation(etudiant.getIne(), formation.getId());
                        JOptionPane.showMessageDialog(null, "Étudiant réinscrit avec succès !");
                    }
                } else if (typeForm.equals("create")) {
                    etudiantService.inscrireEtudiantFormation(etudiant.getIne(), formation.getId());
                    JOptionPane.showMessageDialog(null, "Etudiant inscrit avec succès !");
                } else {
                    JOptionPane.showMessageDialog(null, "Opération inconnue pour l'enseignant !");
                }
            }
        }
    }

    private void traiterModule(JComboBox[] comboBoxes, String typeForm, String objectDetailed) {
        if (!comboBoxes[0].getSelectedItem().toString().equals("Sélectionner...")) {
            String ine = comboBoxes[0].getSelectedItem().toString().split(" ")[0];

            // Récupérer l'enseignant à partir de son matricule (INE)
            Etudiant etudiantObj = etudiantService.trouverEtudiant(ine);
            Module module = moduleService.trouverModuleParCode(objectDetailed);

            if (etudiantObj == null || module == null) {
                JOptionPane.showMessageDialog(null, "Erreur : Etudiant ou Module introuvable !");
                return;
            }

            // Vérifier si l'enseignant est déjà inscrit au module
            Hibernate.initialize(etudiantObj.getModules());
            boolean moduleDejaAssigne = etudiantObj.getModules().stream()
                    .anyMatch(m -> m.getCode().equals(objectDetailed));

            boolean isFormationValid = module.getFormation().getNom().equals(etudiantObj.getFormation().getNom());

            if (moduleDejaAssigne) {
                JOptionPane.showMessageDialog(null, "L'etudiant est déjà inscrit à ce module !");
            } else if(!isFormationValid) {
                JOptionPane.showMessageDialog(null, "L'etudiant est déjà inscrit à une autre formation différente de celle du module !");
            } else if (typeForm.equals("create")) {
                etudiantService.inscrireEtudiantModule(ine, objectDetailed);
                JOptionPane.showMessageDialog(null, "Etudiant inscrit au module avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Opération inconnue pour l'etudiant !");
            }
        }
    }

    private void traiterNote(JComboBox[] comboBoxes, String typeForm) {
        if (!comboBoxes[0].getSelectedItem().toString().equals("Sélectionner...")){
            String ine = comboBoxes[0].getSelectedItem().toString();
            String moduleCode = comboBoxes[1].getSelectedItem().toString();
            Etudiant etudiant = etudiantService.trouverEtudiant(ine);
            m1.uasz.sn.models.Module module = moduleService.trouverModuleParCode(moduleCode);

            if (typeForm.equals("create")) {
                Note note = new Note(0, 0, etudiant, module);
                noteService.ajouterNote(note);
                JOptionPane.showMessageDialog(null, "Note ajoutée avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Opération inconnue pour la note !");
            }
        }
    }

    private boolean isValidComponent(String callingComponent) {
        return callingComponent != null && (
                callingComponent.equals("users") ||
                        callingComponent.equals("students") ||
                        callingComponent.equals("teachers") ||
                        callingComponent.equals("trainings") ||
                        callingComponent.equals("modules") ||
                        callingComponent.equals("marks")
        );
    }
}
