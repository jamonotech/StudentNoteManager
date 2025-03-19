package m1.uasz.sn.ui.components.Note;

import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.*;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.PanelShape.*;
import m1.uasz.sn.ui.components.Renders.TablesRendererEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;

public class NotePanel extends RoundedSideBar {
    private JComboBox<String> formationDropdown;
    private JComboBox<String> moduleDropdown;
    private JTable table;
    private DefaultTableModel model;
    private EtudiantService etudiantService = new EtudiantService();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private ModuleService moduleService = new ModuleService();
    private FormationService formationService = new FormationService();
    private NoteDAO noteDAO = new NoteDAO();
    private NoteService noteService = new NoteService();
    private Map<String, Long> formationsMap = new HashMap<>();
    private Map<String, Long> modulesMap = new HashMap<>();
    private String selectedModuleNom = "";
    private UtilisateurService utilisateurService = new UtilisateurService();

    public NotePanel(MainFrame mainFrame) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ Titre modernisé
        JLabel titreLabel = new JLabel("🎓 Liste des notes Étudiants");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

// ✅ Panel dropdown + icônes cliquables "Ajouter" et "Afficher Notes"
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(4, 125, 154));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setBackground(new Color(4, 125, 154));

        formationDropdown = new JComboBox<>();
        moduleDropdown = new JComboBox<>();

// ✅ Style moderne
        formationDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        moduleDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));

        formationDropdown.addActionListener(e -> updateModules());

        // ✅ Mettre en gras les labels "Formation" et "Module"
        leftPanel.add(new JLabel("Formation:") {{
            setFont(new Font("SansSerif", Font.BOLD, 14));  // Mettre en gras
        }});
        leftPanel.add(formationDropdown);
        leftPanel.add(new JLabel("Module:") {{
            setFont(new Font("SansSerif", Font.BOLD, 14));  // Mettre en gras
        }});
        leftPanel.add(moduleDropdown);

// ✅ Icône "Afficher Notes"
        JLabel validerIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664924_circle_right_direction_icon.png");
            if (input != null) {
                BufferedImage showIcon = ImageIO.read(input);
                validerIcon.setIcon(new ImageIcon(showIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        validerIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadNotes();  // Action quand l'icône est cliquée
            }
        });
        leftPanel.add(validerIcon);

// ✅ Icône "Ajouter"
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));

        JLabel ajouterIcon = new JLabel();
        ajouterIcon.setBackground(new Color(76, 175, 80));
        JLabel importIcon = new JLabel();
        importIcon.setBackground(new Color(76, 175, 80));
        JLabel pdfIcon = new JLabel();
        pdfIcon.setBackground(new Color(76, 175, 80));

// ✅ Charger une icône "Ajouter"
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664849_square_plus_icon.png");
            if (input != null) {
                BufferedImage addIcon = ImageIO.read(input);
                ajouterIcon.setIcon(new ImageIcon(addIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            InputStream input2 = getClass().getResourceAsStream("/img/icons/8664899_file_word_icon.png");
            if (input2 != null) {
                BufferedImage addIcon2 = ImageIO.read(input2);
                importIcon.setIcon(new ImageIcon(addIcon2.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            InputStream input3 = getClass().getResourceAsStream("/img/icons/8664943_file_pdf_document_icon.png");
            if (input3 != null) {
                BufferedImage addIcon3 = ImageIO.read(input3);
                pdfIcon.setIcon(new ImageIcon(addIcon3.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ajouterIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en main pour l'icône cliquable
        importIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en main pour l'icône cliquable
        pdfIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en main pour l'icône cliquable

// Ajout de l'effet au survol de l'icône
        ajouterIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Module moduleId = moduleService.trouverModule(modulesMap.get(selectedModuleNom));

                String[] champs = {"Note Controle", "Note Examen", "INE Etudiant", "Code module"};
                JTextField[] textFields = new JTextField[champs.length];
                textFields[3] = new JTextField();
                textFields[3].setText(moduleId.getCode());

                Formulaire11ChampsFrame form = new Formulaire11ChampsFrame("Ajouter note", "create", "marks", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        loadNotes(); // Rafraîchir après modification
                    }
                });
            }
        });

        importIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importNotesFromCSV();
            }
        });
        pdfIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportTableToPDF();
            }
        });

        rightPanel.add(ajouterIcon);
        rightPanel.add(importIcon);
        rightPanel.add(pdfIcon);

        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        String[] columns = {"INE", "Nom", "Prénom", "Contrôle Continu", "Examen", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(new Color(200, 200, 200));

        // ✅ Centrer les valeurs dans le tableau
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // ✅ Appliquer le centrage à toutes les colonnes sauf la dernière (Actions)
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);

        // ✅ Ajout du rendu et de l'éditeur des icônes dans la colonne "Actions"
        TablesRendererEditor usersEditor = new TablesRendererEditor("marks", new ActionHandler() {
            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir utilisateur ID : " + id);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier utilisateur ID : " + id);
                Module moduleId = moduleService.trouverModule(modulesMap.get(selectedModuleNom));
                Etudiant etudiant = etudiantService.trouverEtudiant((String) id);
                Note user = noteDAO.findNoteByEtudiantAndModule(etudiant, moduleId);
                // Mapper correctement les valeurs de l'utilisateur
                String[] champs = {"Note Controle", "Note Examen", "INE Etudiant", "Code module"};
                JTextField[] textFields = new JTextField[champs.length];

                textFields[0] = new JTextField();
                textFields[0].setText(String.valueOf(user.getNoteCC()));
                textFields[1] = new JTextField();
                textFields[1].setText(String.valueOf(user.getNoteExamen()));
                textFields[2] = new JTextField();
                textFields[2].setText(user.getEtudiant().getIne());
                textFields[3] = new JTextField();
                textFields[3].setText(user.getModule().getCode());

                new Formulaire11ChampsFrame("Modifier note", "update", "marks", champs, textFields, 800, 570,
                        new ComponentValidation(), new ComponentAction(mainFrame));
                loadNotes();
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                Module moduleId = moduleService.trouverModule(modulesMap.get(selectedModuleNom));
                Etudiant etudiant = etudiantService.trouverEtudiant((String) id);
                Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, moduleId);
                if (!utilisateurService.getUtilisateurConnecte().getEmail().equals(moduleId.getEnseignantResponsable().getEmail())) {
                    JOptionPane.showMessageDialog(null, "Ce module ne vous est pas assigné, vous ne pouvez donc pas la supprimer !");
                    return;
                }
                System.out.println("Supprimer utilisateur ID : " + id);
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    noteService.supprimerNote(noteService.trouverNote(note.getId()));
                    // Rafraîchir les données ici
                    loadNotes();
                }
            }
        });
        TableColumn column = table.getColumnModel().getColumn(5);
        column.setCellRenderer(usersEditor);
        column.setCellEditor(usersEditor);


        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE);

        add(titreLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        loadFormations();
    }

    private void loadFormations() {
        List<Formation> formations = formationService.listerFormations();
        formationDropdown.removeAllItems();
        formationsMap.clear();
        for (Formation formation : formations) {
            formationDropdown.addItem(formation.getNom());
            formationsMap.put(formation.getNom(), formation.getId());
        }
    }

    private void updateModules() {
        String selectedFormationNom = (String) formationDropdown.getSelectedItem();
        if (selectedFormationNom != null && formationsMap.containsKey(selectedFormationNom)) {
            moduleDropdown.removeAllItems();
            modulesMap.clear();
            Formation idFormation = formationService.trouverFormation(formationsMap.get(selectedFormationNom));
            List<Module> modules = new ModuleDAO().findModulesByFormation(idFormation);
            for (Module module : modules) {
                moduleDropdown.addItem(module.getNom());
                modulesMap.put(module.getNom(), module.getId());
            }
        }
    }

    private void loadNotes() {
        selectedModuleNom = (String) moduleDropdown.getSelectedItem();
        if (selectedModuleNom != null && modulesMap.containsKey(selectedModuleNom)) {
            Module moduleId = moduleService.trouverModule(modulesMap.get(selectedModuleNom));
            List<Etudiant> etudiants = new ModuleDAO().findEtudiantsByModule(moduleId);
            model.setRowCount(0);
            for (Etudiant etudiant : etudiants) {
                Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, moduleId);
                double noteCC = (note != null) ? note.getNoteCC() : 0.0;
                double noteExam = (note != null) ? note.getNoteExamen() : 0.0;
                model.addRow(new Object[]{
                        etudiant.getIne(),
                        etudiant.getNom(),
                        etudiant.getPrenoms(),
                        noteCC,
                        noteExam,
                        ""
                });
            }
            SwingUtilities.invokeLater(() -> {
                model.fireTableDataChanged();
            });
        }
    }

    private void importNotesFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner un fichier CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            StringBuilder erreurs = new StringBuilder();

            try (Reader reader = new FileReader(selectedFile);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                // Vérification des en-têtes
                if (!csvParser.getHeaderMap().containsKey("INE") ||
                        !csvParser.getHeaderMap().containsKey("Code Module") ||
                        !csvParser.getHeaderMap().containsKey("Note Controle") ||
                        !csvParser.getHeaderMap().containsKey("Note Examen")) {

                    JOptionPane.showMessageDialog(this,
                            "Le fichier CSV ne contient pas les colonnes requises.",
                            "Erreur format CSV",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (CSVRecord record : csvParser) {
                    try {
                        String ine = record.get("INE").trim();
                        String moduleCode = record.get("Code Module").trim();
                        double noteCC = Double.parseDouble(record.get("Note Controle").trim());
                        double noteExamen = Double.parseDouble(record.get("Note Examen").trim());

                        Etudiant etudiant = etudiantService.trouverEtudiant(ine);
                        Module module = moduleService.trouverModuleParCode(moduleCode);

                        if (etudiant == null || module == null) {
                            erreurs.append("Etudiant ou module introuvable : INE=").append(ine).append(", Module=").append(moduleCode).append("\n");
                            continue;
                        }

                        // Vérification 1 : L'enseignant responsable du module est-il l'utilisateur connecté ?
                        if (!module.getEnseignantResponsable().getEmail().equals(utilisateurService.getUtilisateurConnecte().getEmail())) {
                            erreurs.append("Vous n'êtes pas autorisé à noter le module ").append(moduleCode).append("\n");
                            continue;
                        }

                        // Vérification 2 : L'étudiant est-il inscrit au module ?
                        if (etudiantDAO.findModuleByEtudiant(module, etudiant) == null) {
                            erreurs.append("L'étudiant ").append(ine).append(" n'est pas inscrit au module ").append(moduleCode).append("\n");
                            continue;
                        }

                        // Traitement des notes après validation
                        Note note = noteDAO.findNoteByEtudiantAndModule(etudiant, module);
                        if (note == null) {
                            note = new Note();
                            note.setEtudiant(etudiant);
                            note.setModule(module);
                            note.setNoteCC(noteCC);
                            note.setNoteExamen(noteExamen);
                            noteService.ajouterNote(note);
                        } else {
                            note.setNoteCC(noteCC);
                            note.setNoteExamen(noteExamen);
                            noteService.modifierNote(note);
                        }

                    } catch (NumberFormatException e) {
                        erreurs.append("Erreur de format pour la note dans la ligne : ").append(record).append("\n");
                    }
                }

                if (erreurs.length() > 0) {
                    JOptionPane.showMessageDialog(this, erreurs.toString(), "Erreurs d'importation", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Importation terminée avec succès !");
                }

                loadNotes();

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier CSV", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void exportTableToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                // Écriture des en-têtes
                String[] headers = {"INE", "Nom", "Prénom", "Contrôle", "Examen"};
                writer.write(String.join(";", headers) + "\n");

                // Écriture des données
                for (int i = 0; i < model.getRowCount(); i++) {
                    List<String> rowValues = new ArrayList<>();
                    for (int j = 0; j < model.getColumnCount() - 1; j++) { // Exclure la colonne "Actions"
                        rowValues.add(model.getValueAt(i, j).toString());
                    }
                    writer.write(String.join(";", rowValues) + "\n");
                }

                JOptionPane.showMessageDialog(this, "CSV généré avec succès : " + filePath, "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation en CSV", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
