package m1.uasz.sn.ui.components.Results;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.FormationDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.services.EtudiantService;
import m1.uasz.sn.services.FormationService;
import m1.uasz.sn.ui.components.PanelShape.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resultat extends RoundedSideBar {
    private JComboBox<String> formationDropdown;
    private JTable table;
    private DefaultTableModel tableModel;
    private EtudiantService etudiantService;
    private FormationService formationService;
    private Map<String, Long> formationsMap = new HashMap<>();

    public Resultat() {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        etudiantService = new EtudiantService();
        formationService = new FormationService();

        // ✅ Titre modernisé
        JLabel titreLabel = new JLabel("🎓 Liste des résultats Formations");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // Top Panel with Formation Dropdown and Icons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(4, 125, 154));

        // Left Panel with Formation Dropdown
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setBackground(new Color(4, 125, 154));

        formationDropdown = new JComboBox<>();
        chargerFormations();
        formationDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formationDropdown.addActionListener(e -> chargerResultats());

        // Mettre en gras le label "Formation"
        leftPanel.add(new JLabel("Formation:") {{
            setFont(new Font("SansSerif", Font.BOLD, 14));  // Mettre en gras
        }});
        leftPanel.add(formationDropdown);

        // Ajouter des icônes pour interagir avec les résultats
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));

        // Icon for "Afficher Résultats"
        JLabel toFile = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664899_file_word_icon.png");
            if (input != null) {
                BufferedImage showIcon = ImageIO.read(input);
                toFile.setIcon(new ImageIcon(showIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        toFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportTableToCSV();  // Appelle la méthode pour exporter en CSV
            }
        });

        toFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rightPanel.add(toFile);

        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Table for displaying student results
        String[] columnNames = {"Rang", "INE", "Nom", "Prénom(s)", "Moyenne", "Mention", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(new Color(200, 200, 200));

        // Center the table values
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);

        // Scrollable Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE);

        // Add components to main panel
        add(titreLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }

    private void chargerFormations() {
        List<Formation> formations = formationService.listerFormations();
        formationDropdown.removeAllItems();
        formationDropdown.addItem("Sélectionner une formation");
        formationsMap.clear();
        for (Formation formation : formations) {
            formationDropdown.addItem(formation.getNom());
            formationsMap.put(formation.getNom(), formation.getId());
        }
    }

    private void chargerResultats() {
        String formationNom = (String) formationDropdown.getSelectedItem();
        if ("Sélectionner une formation".equals(formationNom)) return;

        Formation formation = formationService.trouverFormationParNom(formationNom);
        if (formation == null) return;

        List<Etudiant> etudiants = etudiantService.listerEtudiantsParFormation(formation);
        etudiants.sort((e1, e2) -> Double.compare(etudiantService.calculerMoyenneEtudiant(e2), etudiantService.calculerMoyenneEtudiant(e1)));

        tableModel.setRowCount(0);
        int rang = 1;
        for (Etudiant etudiant : etudiants) {
            double moyenne = etudiantService.calculerMoyenneEtudiant(etudiant);
            String mention = determinerMention(moyenne);
            String statut = moyenne >= 10 ? "Admis" : "Non Admis";
            tableModel.addRow(new Object[]{rang++, etudiant.getIne(), etudiant.getNom(), etudiant.getPrenoms(),
                    String.format("%.2f", moyenne), mention, statut});
        }
    }

    private String determinerMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Non Admis";
    }

    private void exportTableToCSV() {
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
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i) + (i < tableModel.getColumnCount() - 1 ? ";" : "\n"));
                }

                // Écriture des données
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.write(tableModel.getValueAt(i, j).toString() + (j < tableModel.getColumnCount() - 1 ? ";" : "\n"));
                    }
                }

                JOptionPane.showMessageDialog(this, "CSV généré avec succès : " + filePath, "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation en CSV", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
