package m1.uasz.sn.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import m1.uasz.sn.services.FormationService;
import m1.uasz.sn.services.StatistiquesService;

public class FormationPanel extends JPanel {
    private FormationService formationService = new FormationService();
    private StatistiquesService statistiquesService = new StatistiquesService();
    private DefaultTableModel model;

    public FormationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("Liste des Formations");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Colonnes du tableau (ajout des nouvelles colonnes pour la formation)
        String[] columns = {
                "ID", "Nom", "Niveau", "Responsable Nom", "Responsable Email", "Actions"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seules les icônes sont cliquables
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
        TableColumn column = table.getColumnModel().getColumn(5);
        column.setCellRenderer(new TableRenderer());

        // Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter les composants
        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Charger les formations depuis la BD
        chargerFormations();
    }

    private void chargerFormations() {
        List<m1.uasz.sn.models.Formation> formations = formationService.listerFormations();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Formation formation : formations) {
            model.addRow(new Object[]{
                    formation.getId(),
                    formation.getNom(),
                    formation.getNiveau(),
                    formation.getResponsableNom(),
                    formation.getResponsableEmail(),
                    "" // Pour les icônes d'actions
            });
        }
    }
}

