package m1.uasz.sn.ui.components;

import javax.swing.*;
import java.awt.*;

public class Enseignant extends JPanel {
    public Enseignant() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue sur Enseignant", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(welcomeLabel, BorderLayout.CENTER);
        setVisible(true);
    }
}
