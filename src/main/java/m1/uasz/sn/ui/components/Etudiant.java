package m1.uasz.sn.ui.components;

import javax.swing.*;
import java.awt.*;

public class Etudiant extends JPanel {
    public Etudiant() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue sur Etudiant", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(welcomeLabel, BorderLayout.CENTER);
        setVisible(true);
    }
}
