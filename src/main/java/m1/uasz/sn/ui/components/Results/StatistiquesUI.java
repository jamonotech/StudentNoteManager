package m1.uasz.sn.ui.components.Results;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.services.FormationService;
import m1.uasz.sn.services.StatistiquesService;
import m1.uasz.sn.ui.components.PanelShape.RoundedSideBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

public class StatistiquesUI extends RoundedSideBar {
    private final StatistiquesService statistiquesService = new StatistiquesService();
    private JComboBox<String> formationDropdown;
    private FormationService formationService = new FormationService();
    private JPanel statsPanel;

    public StatistiquesUI() {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("📊 Statistiques des Formations");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // Panel du haut
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(4, 125, 154));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centrage + Espacement
        leftPanel.setBackground(new Color(4, 125, 154));

        formationDropdown = new JComboBox<>();
        chargerFormations();
        formationDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formationDropdown.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Ajout d'un padding
        formationDropdown.addActionListener(e -> updateStats());

        leftPanel.add(new JLabel("Formation:") {{
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setForeground(Color.WHITE);
        }});
        leftPanel.add(formationDropdown);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));

        JLabel toFile = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664899_file_word_icon.png");
            if (input != null) {
                BufferedImage showIcon = ImageIO.read(input);
                toFile.setIcon(new ImageIcon(showIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rightPanel.add(toFile);

        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Ajout d'un espace entre le Dropdown et les statistiques
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10, 30));
        spacer.setOpaque(false);

        // Stats Panel (avec un meilleur espacement)
        statsPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        statsPanel.setOpaque(false);

        add(titreLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(spacer, BorderLayout.SOUTH);
        add(statsPanel, BorderLayout.SOUTH);
    }

    private void updateStats() {
        String formationNom = (String) formationDropdown.getSelectedItem();
        if ("Sélectionner une formation".equals(formationNom)) return;

        Formation formation = formationService.trouverFormationParNom(formationNom);
        if (formation == null) return;

        DecimalFormat df = new DecimalFormat("0.00");

        int effectif = statistiquesService.getEffectifFormation(formation);
        double tauxReussite = statistiquesService.getTauxReussiteFormation(formation);
        int nombreAdmis = statistiquesService.getNombreAdmisFormation(formation);
        int nombreMentions = statistiquesService.getNombreMentionsFormation(formation);
        Etudiant meilleurEtudiant = statistiquesService.getMeilleurEtudiantFormation(formation);
        double meilleureMoyenne = statistiquesService.getMeilleureMoyenneFormation(formation);

        String nomMeilleurEtudiant = (meilleurEtudiant != null) ? meilleurEtudiant.getNom() : "N/A";
        String majorantText = (nomMeilleurEtudiant != null) ?
                (nomMeilleurEtudiant + " \n" +
                        df.format(meilleureMoyenne))
                : "Aucun étudiant";

        statsPanel.removeAll();
        statsPanel.add(createStatCard("TAUX DE REUSSITE", df.format(tauxReussite) + "%", getResizedIcon("/img/icons/8666782_award_prize_icon.png", 40, 40)));
        statsPanel.add(createStatCard("NOMBRE ADMIS", String.valueOf(nombreAdmis), getResizedIcon("/img/icons/8664877_flag_location_country_icon.png", 40, 40)));
        statsPanel.add(createStatCard("NOMBRE MENTIONS", String.valueOf(nombreMentions), getResizedIcon("/img/icons/8664803_bookmark_icon.png", 40, 40)));
        statsPanel.add(createStatCard("MAJOR PROMO", majorantText, getResizedIcon("/img/icons/8664909_heart_like_icon.png", 40, 40)));
        statsPanel.add(createStatCard("EFFECTIF FORMATION", String.valueOf(effectif), getResizedIcon("/img/icons/8666755_users_group_icon.png", 40, 40)));
//        statsPanel.add(createStatCard("NOMBRE FORMATIONS", String.valueOf(statistiquesService.getNombreFormations()), getResizedIcon("/img/icons/8666671_briefcase_icon.png", 65, 65)));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 165)); // Augmentation de la taille
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28)); // Légèrement plus grand
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

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
            if (imgStream == null) {
                System.err.println("Image non trouvée: " + resourcePath);
                return new ImageIcon();
            }
            BufferedImage img = ImageIO.read(imgStream);
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private void chargerFormations() {
        List<Formation> formations = formationService.listerFormations();
        formationDropdown.removeAllItems();
        formationDropdown.addItem("Sélectionner une formation");
        for (Formation formation : formations) {
            formationDropdown.addItem(formation.getNom());
        }
    }
}
