package m1.uasz.sn.ui.components.Forms;

import m1.uasz.sn.ui.components.Action_Validation.FormActionStrategy;
import m1.uasz.sn.ui.components.Action_Validation.FormValidationStrategy;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class Formulaire11ChampsFrame extends JFrame {

    public Formulaire11ChampsFrame(String name, String typeForm, String callingComponent, String[] labels, JTextField[] textFields, int widthF, int heightF, FormValidationStrategy strategy, FormActionStrategy actionStrategy) {
        setTitle(name);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(widthF, heightF);
        setResizable(false);
        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("src/main/resources/img/background/b2.jpg");
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            panel.add(label, gbc);

            gbc.gridx = 1;
            if (textFields[i] == null) {
                textFields[i] = new JTextField(20);
            }
            textFields[i].setPreferredSize(new Dimension(300, 30));
            textFields[i].setBorder(new LineBorder(new Color(200, 200, 200), 2));
            panel.add(textFields[i], gbc);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        JButton validerButton = new JButton("Valider");
        validerButton.setPreferredSize(new Dimension(100, 35));
        validerButton.setBackground(new Color(26, 115, 232));
        validerButton.setForeground(Color.WHITE);
        validerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        validerButton.setBorder(new LineBorder(new Color(26, 115, 232), 2));

        JButton annulerButton = new JButton("Annuler");
        annulerButton.setPreferredSize(new Dimension(100, 35));
        annulerButton.setBackground(new Color(204, 204, 204));
        annulerButton.setForeground(Color.BLACK);
        annulerButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        annulerButton.setBorder(new LineBorder(new Color(204, 204, 204), 2));

        buttonPanel.add(validerButton);
        buttonPanel.add(annulerButton);

        gbc.gridx = 0;
        gbc.gridy = labels.length + 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        annulerButton.addActionListener(e -> {
            for (JTextField field : textFields) {
                field.setText("");
            }
            dispose();
        });

        validerButton.addActionListener(e -> {
            if (strategy.valider(textFields, callingComponent, typeForm)) {
                JOptionPane.showMessageDialog(null, "Validation réussie !");
                actionStrategy.executerApresValidation(textFields, callingComponent, typeForm);
                dispose();
            }
        });

        setVisible(true);
    }
}

