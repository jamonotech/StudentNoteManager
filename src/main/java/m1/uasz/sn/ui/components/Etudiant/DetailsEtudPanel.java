package m1.uasz.sn.ui.components.Etudiant;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.EnseignantService;
import m1.uasz.sn.services.EtudiantService;
import m1.uasz.sn.services.ModuleService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentDetailsAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.FormulaireComboBox;
import m1.uasz.sn.ui.components.PanelShape.*;
import m1.uasz.sn.ui.components.Renders.TableDetailsRendererEditor;
import org.hibernate.Hibernate;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailsEtudPanel extends RoundedSideBar {
    private JTabbedPane tabbedPane;
    private JPanel infoPanel;
    private JPanel modulesPanel;
    private JPanel etudPanel;
    private DefaultTableModel model;
    private DefaultTableModel modelEtud;
    private JTable table;
    private ModuleService moduleService = new ModuleService();
    private EnseignantService enseignantService = new EnseignantService();
    private ModuleDAO moduleDAO = new ModuleDAO();
    private EtudiantService etudiantService = new EtudiantService();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private NoteDAO noteDAO = new NoteDAO();

    public DetailsEtudPanel(MainFrame mainFrame, Etudiant etudiant) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ✅ Titre général "DETAILS COMPOSANT"
        JLabel titreComposantLabel = new JLabel("🛠️ DETAILS ETUDIANT");
        titreComposantLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titreComposantLabel.setForeground(Color.WHITE);
        titreComposantLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreComposantLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // ✅ Panneau du titre avec couleur de fond
        JPanel titrePanel = new JPanel(new BorderLayout());
        titrePanel.setBackground(new Color(4, 125, 154));
        titrePanel.add(titreComposantLabel, BorderLayout.CENTER);

        // ✅ Ajout du titre avant les tabbed panes
        add(titrePanel, BorderLayout.NORTH);



        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);



        // Onglet Informations
        // Onglet Informations avec deux colonnes (Label à gauche, Valeur à droite)
//        infoPanel = new JPanel(new GridBagLayout());
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(4, 125, 154), 2),
                new EmptyBorder(20, 40, 20, 40)) // Augmenter l'espacement
        );

        // Ajout des champs en deux colonnes
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 20, 10)); // Espacement de 20px entre colonnes
        fieldsPanel.setBackground(Color.WHITE);
        infoPanel.add(fieldsPanel, BorderLayout.CENTER);

        String formation = etudiant.getFormation() != null ? etudiant.getFormation().getNom() : "";

        ajouterChamp(fieldsPanel, "INE : ", etudiant.getIne());
        ajouterChamp(fieldsPanel, "Nom : ", etudiant.getNom());
        ajouterChamp(fieldsPanel, "Prénom : ", etudiant.getPrenoms());
        ajouterChamp(fieldsPanel, "Date de naissance : ", etudiant.getDateNaissance().toString());
        ajouterChamp(fieldsPanel, "Sexe : ", etudiant.getSexe());
        ajouterChamp(fieldsPanel, "Adresse : ", etudiant.getAdresse());
        ajouterChamp(fieldsPanel, "Email : ", etudiant.getEmail());
        ajouterChamp(fieldsPanel, "Formation : ", formation);

        // Ajouter le panneau à l'onglet
        tabbedPane.addTab("Informations", infoPanel);


//------------------------------------------------------------------------------------------------------//



        // Onglet Modules Assignés
        modulesPanel = new JPanel(new BorderLayout());
        modulesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        modulesPanel.setBackground(Color.WHITE);

        // ✅ Titre modernisé
        JLabel titreModLabel = new JLabel("📚 Liste des Modules");
        titreModLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreModLabel.setForeground(Color.WHITE);
        titreModLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreModLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panneau pour le titre et l'icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(4, 125, 154));

        // ✅ Icône "Ajouter"
        JLabel ajouterIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664849_square_plus_icon.png");
            if (input != null) {
                BufferedImage addIcon = ImageIO.read(input);
                ajouterIcon.setIcon(new ImageIcon(addIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ajouterIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ Gestion de l'événement au clic
        ajouterIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String champs = "Modules"; // Seul le champ comboBox est nécessaire
                List<String> comboBoxes = new ArrayList<>();
                List<Module> moduleList = moduleService.listerModule();
                // Vérifier que la liste des modules n'est pas vide
                if (moduleList != null && !moduleList.isEmpty()) {
                    for (Module module : moduleList) {
                        comboBoxes.add(module.getCode() + " " + module.getNom());
                    }
                }

                FormulaireComboBox form = new FormulaireComboBox(
                        "Ajouter module",
                        "create",
                        "students",
                        "modules",
                        etudiant.getIne(),
                        champs,
                        comboBoxes,
                        500,
                        300,
                        new ComponentValidation(),
                        new ComponentDetailsAction()
                );

                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerModules(etudiant);
                    }
                });
            }
        });

        // ✅ Ajout de l'icône à droite du header
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));
        rightPanel.add(ajouterIcon);
        headerPanel.add(titreModLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // ✅ Ajout du header au panneau principal des modules
        modulesPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Code", "Nom", "Volume Horaire", "Coefficient", "Crédits", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 230, 255));

        // ✅ Centrage des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);

        TableDetailsRendererEditor editor = new TableDetailsRendererEditor("students", new ActionHandler() {
            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir module ID : " + id);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier module ID : " + id);
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");

                if (confirmation == JOptionPane.YES_OPTION) {
                    m1.uasz.sn.models.Module module = moduleService.trouverModule((Long) id);

                    if (module == null) {
                        JOptionPane.showMessageDialog(null, "Erreur : Module introuvable !");
                        return;
                    }

                    etudiantService.desinscrireEtudiantModule(etudiant.getIne(), module.getCode());
                    JOptionPane.showMessageDialog(null, "Module désaffecté avec succès !");

                    chargerModules(etudiant);
                }
            }
        });
        table.getColumnModel().getColumn(6).setCellRenderer(editor);
        table.getColumnModel().getColumn(6).setCellEditor(editor);

        modulesPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tabbedPane.addTab("Modules Assignés", modulesPanel);


        chargerModules(etudiant);



        //------------------------------------------------------------------------------------------------------//



        // Onglet Relevé de note
        etudPanel = new JPanel(new BorderLayout());
        etudPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        etudPanel.setBackground(Color.WHITE);

        // ✅ Titre modernisé
        JLabel titreEtudLabel = new JLabel("📜 Relevé de Notes");
        titreEtudLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreEtudLabel.setForeground(Color.WHITE);
        titreEtudLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreEtudLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panneau pour le titre et l'icône
        JPanel headerEtudPanel = new JPanel(new BorderLayout());
        headerEtudPanel.setBackground(new Color(4, 125, 154));

        // ✅ Icône "Ajouter"
        JLabel exportIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664849_square_plus_icon.png");
            if (input != null) {
                BufferedImage addIcon = ImageIO.read(input);
                exportIcon.setIcon(new ImageIcon(addIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        exportIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ Gestion de l'événement au clic
        exportIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

            }
        });

        // ✅ Ajout de l'icône à droite du header
        JPanel rightEtudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightEtudPanel.setBackground(new Color(4, 125, 154));
        rightEtudPanel.add(exportIcon);
        headerEtudPanel.add(titreEtudLabel, BorderLayout.CENTER);
        headerEtudPanel.add(rightEtudPanel, BorderLayout.EAST);

        // ✅ Ajout du header au panneau principal des modules
        etudPanel.add(headerEtudPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Relevé de Notes", etudPanel);
        add(tabbedPane, BorderLayout.CENTER);
        chargerReleveDeNotes(etudiant);

    }

    private void ajouterChamp(JPanel panel, String label, String value) {
        JLabel champLabel = new JLabel(label );
        champLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        champLabel.setForeground(new Color(33, 33, 33));

        JLabel champValue = new JLabel(value);
        champValue.setFont(new Font("SansSerif", Font.PLAIN, 16));

        panel.add(champLabel);
        panel.add(champValue);
    }

    private void chargerModules(Etudiant etudiant) {
        List<Module> modules = etudiantDAO.findModulesByEtudiant(etudiant);
        model.setRowCount(0);
        for (Module module : modules) {
            model.addRow(new Object[]{module.getId(), module.getCode(), module.getNom(), module.getVolumeHoraire(), module.getCoefficient(), module.getCredits()});
        }
    }

    private void chargerReleveDeNotes(Etudiant etudiant) {
        etudPanel.removeAll(); // Nettoyer l'onglet avant d'ajouter du contenu

        // ✅ Titre modernisé
        JLabel titreEtudLabel = new JLabel("📜 Relevé de Notes");
        titreEtudLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreEtudLabel.setForeground(Color.WHITE);
        titreEtudLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreEtudLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panneau pour le titre et l'icône
        JPanel headerEtudPanel = new JPanel(new BorderLayout());
        headerEtudPanel.setBackground(new Color(4, 125, 154));

        // ✅ Icône "Exporter"
        JLabel exportIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664899_file_word_icon.png");
            if (input != null) {
                BufferedImage addIcon = ImageIO.read(input);
                exportIcon.setIcon(new ImageIcon(addIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        exportIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ Gestion de l'événement au clic
        exportIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Action d'exportation du relevé
                exporterReleveDeNotesCSV(etudiant);
            }
        });

        // ✅ Ajout de l'icône à droite du header
        JPanel rightEtudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightEtudPanel.setBackground(new Color(4, 125, 154));
        rightEtudPanel.add(exportIcon);
        headerEtudPanel.add(titreEtudLabel, BorderLayout.CENTER);
        headerEtudPanel.add(rightEtudPanel, BorderLayout.EAST);

        // Ajout du header à l'onglet
        etudPanel.add(headerEtudPanel, BorderLayout.NORTH);

        // Initialiser les modules de l'étudiant
        Hibernate.initialize(etudiant.getModules());
        List<Module> modules = etudiant.getModules();

        // Définir les colonnes de la table des relevés de notes
        String[] columnsReleve = {"INE", "Nom", "Prénom", "Code Module", "Nom Module", "Note Controle", "Note Examen", "Moyenne", "Mention"};
        DefaultTableModel modelReleve = new DefaultTableModel(columnsReleve, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Récupérer les notes et les ajouter au modèle de la table
        for (Module module : modules) {
            Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);
            if (note != null) {
                double moyenne = (note.getNoteCC() + note.getNoteExamen()) / 2.0;
                modelReleve.addRow(new Object[]{
                        etudiant.getIne(),
                        etudiant.getNom(),
                        etudiant.getPrenoms(),
                        module.getCode(),
                        module.getNom(),
                        note.getNoteCC(),
                        note.getNoteExamen(),
                        String.format("%.2f", moyenne),
                        determinerMention(moyenne)
                });
            } else {
                modelReleve.addRow(new Object[]{
                        etudiant.getIne(),
                        etudiant.getNom(),
                        etudiant.getPrenoms(),
                        module.getCode(),
                        module.getNom(),
                        "N/A", "N/A", "N/A", "N/A"
                });
            }
        }

        // Calculer la moyenne générale et la mention
        double moyenneGenerale = etudiantService.calculerMoyenneGenerale(etudiant);
        String mentionGenerale = determinerMention(moyenneGenerale);

        // Création de la table
        JTable tableReleve = new JTable(modelReleve);
        tableReleve.setRowHeight(40);
        tableReleve.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableReleve.setSelectionBackground(new Color(200, 230, 255));

        // Centrer le texte dans les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableReleve.getColumnCount(); i++) {
            tableReleve.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader headerReleve = tableReleve.getTableHeader();
        headerReleve.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerReleve.setBackground(new Color(2, 119, 232));
        headerReleve.setForeground(Color.WHITE);

        // Ajout du JLabel pour afficher la moyenne générale sous le tableau
        JLabel lblMoyenneGenerale = new JLabel("Moyenne Générale : " + String.format("%.2f", moyenneGenerale) + " - Mention : " + mentionGenerale);
        lblMoyenneGenerale.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMoyenneGenerale.setHorizontalAlignment(SwingConstants.CENTER);
        lblMoyenneGenerale.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Création du panneau principal contenant la table et la moyenne générale
        JPanel panelReleve = new JPanel(new BorderLayout());
        panelReleve.add(new JScrollPane(tableReleve), BorderLayout.CENTER);
        panelReleve.add(lblMoyenneGenerale, BorderLayout.SOUTH);

        // Ajouter le panneau au panel principal
        etudPanel.add(panelReleve, BorderLayout.CENTER);
        etudPanel.revalidate();
        etudPanel.repaint();
    }

    // Méthode pour déterminer la mention selon la moyenne
    private String determinerMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }

    private void exporterReleveDeNotesCSV(Etudiant etudiant) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choisissez l'emplacement pour enregistrer le fichier");
        fileChooser.setSelectedFile(new java.io.File("Releve_Notes_" + etudiant.getIne() + ".csv"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.append("INE,Nom,Prénom,Code Module,Nom Module,Note Controle,Note Examen,Moyenne,Mention\n");

                double sommeMoyennes = 0;
                int nombreModules = 0;

                for (Module module : etudiant.getModules()) {
                    Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);
                    double moyenne = note != null ? (note.getNoteCC() + note.getNoteExamen()) / 2.0 : -1;

                    if (moyenne >= 0) {
                        sommeMoyennes += moyenne;
                        nombreModules++;
                    }

                    writer.append(etudiant.getIne()).append(",")
                            .append(etudiant.getNom()).append(",")
                            .append(etudiant.getPrenoms()).append(",")
                            .append(module.getCode()).append(",")
                            .append(module.getNom()).append(",")
                            .append(note != null ? String.valueOf(note.getNoteCC()) : "N/A").append(",")
                            .append(note != null ? String.valueOf(note.getNoteExamen()) : "N/A").append(",")
                            .append(moyenne >= 0 ? String.format("%.2f", moyenne) : "N/A").append(",")
                            .append(moyenne >= 0 ? determinerMention(moyenne) : "N/A").append("\n");
                }

                // Calcul et ajout de la moyenne générale
                double moyenneGenerale = (nombreModules > 0) ? sommeMoyennes / nombreModules : 0;
                String mentionGenerale = determinerMention(moyenneGenerale);

                writer.append("\nMoyenne Générale, , , , , , , ")
                        .append(String.format("%.2f", moyenneGenerale)).append(",")
                        .append(mentionGenerale).append("\n");

                JOptionPane.showMessageDialog(null, "Exporté sous : " + filePath);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

}
