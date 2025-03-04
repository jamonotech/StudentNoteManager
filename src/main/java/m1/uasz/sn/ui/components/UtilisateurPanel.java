package m1.uasz.sn.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.models.Utilisateur;

public class UtilisateurPanel extends JPanel {
    private UtilisateurService utilisateurService = new UtilisateurService();
    private DefaultTableModel model;

    public UtilisateurPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("Liste des Utilisateurs");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Colonnes du tableau
        String[] columns = {"ID", "Email", "Rôle", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Seules les icônes sont cliquables
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
        TableColumn column = table.getColumnModel().getColumn(3);
        column.setCellRenderer(new TableRenderer());

        // Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter les composants
        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Charger les utilisateurs depuis la BD
        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.listerUtilisateurs();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (Utilisateur user : utilisateurs) {
            model.addRow(new Object[]{
                    user.getId(),
                    user.getEmail(),
                    user.getPrenom(),
                    user.getNom(),
                    "" // Pour les icônes d'actions
            });
        }
    }
}

