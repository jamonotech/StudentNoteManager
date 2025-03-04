package m1.uasz.sn.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import m1.uasz.sn.services.EtudiantService;

public class EtudiantPanel extends JPanel {
    private EtudiantService etudiantService = new EtudiantService();
    private DefaultTableModel model;

    public EtudiantPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("Liste des Etudiants");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Colonnes du tableau (ajout des nouvelles colonnes pour l'étudiant)
        String[] columns = {
                "INE", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email", "Actions"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Seules les icônes sont cliquables
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);

        // Personnalisation de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));

        // Appliquer TableRenderer pour les icônes
        TableColumn column = table.getColumnModel().getColumn(7);
        column.setCellRenderer(new TableRenderer());

        // Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter les composants
        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Charger les étudiants depuis la BD
        chargerEtudiants();
    }

    private void chargerEtudiants() {
        List<m1.uasz.sn.models.Etudiant> etudiants = etudiantService.listerEtudiants();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Etudiant etu : etudiants) {
            model.addRow(new Object[]{
                    etu.getIne(),
                    etu.getNom(),
                    etu.getPrenoms(),
                    etu.getDateNaissance(), // Format de date si nécessaire
                    etu.getSexe(),
                    etu.getAdresse(),
                    etu.getEmail(),
                    "" // Pour les icônes d'actions
            });
        }
    }
}
