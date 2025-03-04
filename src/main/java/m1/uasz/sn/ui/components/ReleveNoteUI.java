package m1.uasz.sn.ui.components;

import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.services.EtudiantService;

import javax.swing.*;
import java.awt.*;

public class ReleveNoteUI extends JFrame {
    private JTextArea textArea;

    public ReleveNoteUI(Etudiant etudiant, EtudiantService etudiantService) {
        setTitle("Relevé de Notes");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        afficherReleve(etudiant, etudiantService);
    }

    private void afficherReleve(Etudiant etudiant, EtudiantService etudiantService) {
        String releve = etudiantService.genererReleveNotes(etudiant);
        textArea.setText(releve);
    }
}
