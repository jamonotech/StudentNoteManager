package m1.uasz.sn.ui.components;

import javax.swing.*;
import java.awt.*;

public class Note extends JPanel {
    public Note() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue sur Note", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(welcomeLabel, BorderLayout.CENTER);
        setVisible(true);
    }
}
