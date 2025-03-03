package m1.uasz.sn.ui.components;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.services.UtilisateurService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Home extends JPanel {
    private final UtilisateurService utilisateurService = new UtilisateurService(new UtilisateurDAO());

    public Home() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F4F4F4")); // Fond gris clair

        // Barre de navigation (breadcrumb)
        JPanel breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        breadcrumbPanel.setBackground(Color.decode("#F4F4F4"));

        JLabel breadcrumbLabel = new JLabel("Dashboard > HOME");
        breadcrumbLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        breadcrumbLabel.setForeground(Color.decode("#666666"));

        breadcrumbPanel.add(breadcrumbLabel);
        add(breadcrumbPanel, BorderLayout.NORTH);

        // Titre principal
        JLabel welcomeLabel = new JLabel("Statistiques JAMONO SCHOOL", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.decode("#333333"));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode("#F4F4F4"));
        headerPanel.add(breadcrumbPanel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Grille des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBackground(Color.decode("#F4F4F4"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statsPanel.add(createStatCard("Etudiants", String.valueOf(utilisateurService.findAll().size()), getResizedIcon("/img/icons/8666755_users_group_icon.png", 65, 65)));
        statsPanel.add(createStatCard("Professeurs", String.valueOf(utilisateurService.findAll().size()), getResizedIcon("/img/icons/8664831_user_icon.png", 65, 65)));
        statsPanel.add(createStatCard("Formations", String.valueOf(utilisateurService.findAll().size()), getResizedIcon("/img/icons/8666671_briefcase_icon.png", 65, 65)));
        statsPanel.add(createStatCard("Modules", String.valueOf(utilisateurService.findAll().size()), getResizedIcon("/img/icons/8666759_layers_layer_icon.png", 65, 65)));
        statsPanel.add(createStatCard("Taux de réussite", utilisateurService.findAll().size() + "%", getResizedIcon("/img/icons/8666782_award_prize_icon.png", 65, 65)));
        statsPanel.add(createStatCard("Users", String.valueOf(utilisateurService.findAll().size()), getResizedIcon("/img/icons/8664925_circle_user_person_icon.png", 65, 65)));

        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 120));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        valueLabel.setForeground(Color.decode("#333333"));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        titleLabel.setForeground(Color.decode("#666666"));

        JPanel content = new JPanel(new GridLayout(3, 1));
        content.setOpaque(false);
        content.add(iconLabel);
        content.add(valueLabel);
        content.add(titleLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private ImageIcon getResizedIcon(String resourcePath, int width, int height) {
        try {
            InputStream imgStream = getClass().getResourceAsStream(resourcePath);
            if (imgStream == null) return new ImageIcon();
            BufferedImage img = ImageIO.read(imgStream);
            if (img == null) return new ImageIcon();

            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
}
