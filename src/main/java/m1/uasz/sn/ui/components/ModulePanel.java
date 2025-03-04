package m1.uasz.sn.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import m1.uasz.sn.services.ModuleService;

public class ModulePanel extends JPanel {
    private ModuleService moduleService = new ModuleService();
    private DefaultTableModel model;

    public ModulePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("Liste des Modules");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Colonnes du tableau (ajout des nouvelles colonnes pour le module)
        String[] columns = {
                "ID", "Code", "Nom", "Volume Horaire", "Coefficient", "Crédits", "Actions"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seules les icônes sont cliquables
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
        TableColumn column = table.getColumnModel().getColumn(6);
        column.setCellRenderer(new TableRenderer());

        // Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter les composants
        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Charger les modules depuis la BD
        chargerModules();
    }

    private void chargerModules() {
        List<m1.uasz.sn.models.Module> modules = moduleService.listerModule();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Module module : modules) {
            model.addRow(new Object[]{
                    module.getId(),
                    module.getCode(),
                    module.getNom(),
                    module.getVolumeHoraire(),
                    module.getCoefficient(),
                    module.getCredits(),
                    "" // Pour les icônes d'actions
            });
        }
    }
}

