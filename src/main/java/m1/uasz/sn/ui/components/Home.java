package m1.uasz.sn.ui.components;

import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.services.*;
import m1.uasz.sn.ui.MainFrame;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.DecimalFormat;

import m1.uasz.sn.ui.components.PanelShape.*;

public class Home extends RoundedSideBar {
    private final StatistiquesService statistiquesService = new StatistiquesService();

    public Home() {
        super(80, 80, false, false, true, true);
        setPreferredSize(new Dimension(getWidth(), getHeight()));
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154)); // Fond gris clair

        // Titre principal
        JLabel welcomeLabel = new JLabel("BOARD JAMONO SCHOOL", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.decode("#333333"));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setBackground(Color.decode("#F4F4F4"));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Grille des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
//        statsPanel.setBackground(Color.decode("#F4F4F4"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DecimalFormat df = new DecimalFormat("0.00");

        Etudiant meilleurEtudiant = statistiquesService.getEtudiantMeilleureMoyenne();
        String majorantText = (meilleurEtudiant != null) ?
                (meilleurEtudiant.getPrenoms() + " " + meilleurEtudiant.getNom() + " \n" +
                        df.format(statistiquesService.getMeilleureMoyenne()))
                : "Aucun étudiant";

        statsPanel.add(createStatCard("TAUX DE REUSSITE", df.format(statistiquesService.getTauxReussite()) + "%", getResizedIcon("/img/icons/8666782_award_prize_icon.png", 65, 65)));
        statsPanel.add(createStatCard("NOMBRE ADMIS", String.valueOf(statistiquesService.getNombreAdmis()), getResizedIcon("/img/icons/8664877_flag_location_country_icon.png", 65, 65)));
        statsPanel.add(createStatCard("NOMBRE MENTIONS", String.valueOf(statistiquesService.getNombreMentions()), getResizedIcon("/img/icons/8664803_bookmark_icon.png", 65, 65)));
        statsPanel.add(createStatCard("MAJORANT ETABLISSEMENT", majorantText, getResizedIcon("/img/icons/8664909_heart_like_icon.png", 65, 65)));
        statsPanel.add(createStatCard("NOMBRE ETUDIANTS", String.valueOf(statistiquesService.getNombreEtudiants()), getResizedIcon("/img/icons/8666755_users_group_icon.png", 65, 65)));
        statsPanel.add(createStatCard("NOMBRE FORMATIONS", String.valueOf(statistiquesService.getNombreFormations()), getResizedIcon("/img/icons/8666671_briefcase_icon.png", 65, 65)));

        headerPanel.setOpaque(false);
        statsPanel.setOpaque(false);

        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 120));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        card.setBackground(Color.WHITE);
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
