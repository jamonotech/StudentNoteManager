package m1.uasz.sn.ui.components;

import javax.swing.*;
import java.awt.*;

public class Utilisateur extends JPanel {
    public Utilisateur() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue sur Users", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(welcomeLabel, BorderLayout.CENTER);
        setVisible(true);
    }
}
