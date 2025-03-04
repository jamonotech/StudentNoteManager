package m1.uasz.sn.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.services.EtudiantService;

public class GlobalResult extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private EtudiantService etudiantService;

    public GlobalResult() {
        etudiantService = new EtudiantService();

        setLayout(new BorderLayout());

        // Initialisation du modèle de la table avec le rang ajouté
        String[] columnNames = {"Rang", "INE", "Nom", "Prénom(s)", "Moyenne", "Mention", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Charger les résultats
        chargerResultats();
    }

    private void chargerResultats() {
        List<Etudiant> etudiants = etudiantService.listerEtudiants();
        if (etudiants.isEmpty()) return;

        // Trier les étudiants par moyenne décroissante
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
}
