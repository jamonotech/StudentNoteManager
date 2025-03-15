package m1.uasz.sn.ui.components.Results;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import m1.uasz.sn.ui.components.PanelShape.*;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.services.EtudiantService;

public class GlobalResult extends RoundedSideBar {
    private JTable table;
    private DefaultTableModel tableModel;
    private EtudiantService etudiantService;

    public GlobalResult() {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        etudiantService = new EtudiantService();

        // ✅ Titre modernisé
        JLabel titreLabel = new JLabel("🎓 Liste des résultats globaux");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // Table initialization with added 'Rang'
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

        // Table header customization
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
        add(tablePanel, BorderLayout.CENTER);

        // Load the results
        chargerResultats();
    }

    private void chargerResultats() {
        List<Etudiant> etudiants = etudiantService.listerEtudiants();
        if (etudiants.isEmpty()) return;

        // Sort students by their average in descending order
        etudiants.sort((e1, e2) -> Double.compare(etudiantService.calculerMoyenneEtudiant(e2), etudiantService.calculerMoyenneEtudiant(e1)));

        // Populate table with the sorted student results
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
}
