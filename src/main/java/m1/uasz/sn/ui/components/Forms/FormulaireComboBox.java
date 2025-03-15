package m1.uasz.sn.ui.components.Forms;

import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.components.Action_Validation.FormActionStrategy;
import m1.uasz.sn.ui.components.Action_Validation.FormValidationStrategy;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.util.List;

public class FormulaireComboBox extends JFrame {
    private UtilisateurService utilisateurService = new UtilisateurService();

    public FormulaireComboBox(String name, String typeForm, String callingComponent, String tabComponent, String ojectDetailed, String label, List<String> options, int widthF, int heightF, FormValidationStrategy strategy, FormActionStrategy actionStrategy) {
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(Color.WHITE);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Sélectionner...");
        for (String option : options) {
            comboBox.addItem(option);
        }
        comboBox.setSelectedIndex(0);
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.setBorder(new LineBorder(new Color(200, 200, 200), 2));
        panel.add(comboBox, gbc);

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
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        annulerButton.addActionListener(e -> dispose());

        validerButton.addActionListener(e -> {
            // Vérification du rôle avant validation
            if (!"RESPONSABLE".equalsIgnoreCase(utilisateurService.getUtilisateurConnecte().getRole()) && !"marks".equals(callingComponent)) {
                JOptionPane.showMessageDialog(null, "Vous n'êtes pas autorisé à valider ce formulaire ou à effectuer cette action !");
                return;
            }
            if (comboBox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une option.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Validation réussie !");
                actionStrategy.executerApresValidation(new JComboBox[]{comboBox}, callingComponent, typeForm, tabComponent, ojectDetailed);
                dispose();
            }
        });

        setVisible(true);
    }
}
