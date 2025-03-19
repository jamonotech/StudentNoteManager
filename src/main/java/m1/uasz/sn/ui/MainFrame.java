package m1.uasz.sn.ui;

import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.components.*;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Enseignant.EnseignantPanel;
import m1.uasz.sn.ui.components.Etudiant.EtudiantPanel;
import m1.uasz.sn.ui.components.Formation.FormationPanel;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.Module.ModulePanel;
import m1.uasz.sn.ui.components.Note.NotePanel;
import m1.uasz.sn.ui.components.PanelShape.*;
import m1.uasz.sn.ui.components.Results.GlobalResult;
import m1.uasz.sn.ui.components.Results.Resultat;
import m1.uasz.sn.ui.components.Results.StatistiquesUI;
import m1.uasz.sn.ui.components.Users.ProfileUser;
import m1.uasz.sn.ui.components.Users.UtilisateurPanel;
import m1.uasz.sn.utils.SessionManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class MainFrame extends JFrame {
    private UtilisateurService utilisateurService = new UtilisateurService();

    private JPanel activeMenuItem = null;
    private JPanel mainContent = new Home();

    public MainFrame() {
        setTitle("JAMONO SCHOOL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridBagLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(1, 33, 41));
        getRootPane().setOpaque(false);
//        getRootPane().setBorder(BorderFactory.createLineBorder(Color.GREEN, 11));

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        // Sidebar
        JPanel sidebar = createSidebar();

        // Topbar
        JPanel topbar = createTopbar();

        // Main Content
        mainContent = createMainContent();
//        mainContent.setOpaque(false);

        // Layout Configuration
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(sidebar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(topbar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(mainContent, gbc);

        setVisible(true);
    }

    private JPanel createSidebar() {
//        JPanel sidebar = new JPanel();
        JPanel sidebar = new RoundedSideBar(80, 80, false, true, false, true);
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(1, 33, 41));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        // Logo
        ImageIcon logo = getRoundedIcon("/img/logo.jpg", 100, 100);
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(50, 50, 50));
        logoPanel.add(logoLabel);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(1, 33, 41));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        String[] menuItems = {"Home", "Formations", "Modules", "Étudiants", "Notes", "Délibérations", "Résultats", "Enseignants", "Statistiques", "Utilisateurs"};
        String[] iconPaths = {"/img/icons/8666691_home_icon.png", "/img/icons/8666671_briefcase_icon.png", "/img/icons/8666759_layers_layer_icon.png", "/img/icons/8666755_users_group_icon.png",
                "/img/icons/8664843_pen_to_square_icon.png", "/img/icons/8666782_award_prize_icon.png", "/img/icons/8666782_award_prize_icon.png", "/img/icons/8666755_users_group_icon.png", "/img/icons/8664805_chart_bar_icon.png", "/img/icons/8664925_circle_user_person_icon.png"};

        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItemPanel = new JPanel();
            menuItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10)); // Espacement amélioré
            menuItemPanel.setPreferredSize(new Dimension(180, 40)); // Augmente la hauteur des éléments
            menuItemPanel.setMaximumSize(new Dimension(180, 40));
            menuItemPanel.setBackground(new Color(1, 33, 41));
            menuItemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Ajoute un padding

            // Ajout de l'icône
            ImageIcon icon = getResizedIcon(iconPaths[i], 25, 25);
            JLabel iconLabel = new JLabel(icon);

            // Ajout du texte
            JLabel btn = new JLabel(menuItems[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 15));

            menuItemPanel.add(iconLabel);
            menuItemPanel.add(btn);

            // Gestion du clic et des effets de survol
            final int index = i;
            menuItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    setActiveMenu(menuPanel, index);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (menuItemPanel != activeMenuItem) {
                        menuItemPanel.setBackground(new Color(3, 50, 60)); // Effet au survol
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (menuItemPanel != activeMenuItem) {
                        menuItemPanel.setBackground(new Color(1, 33, 41)); // Retour à la couleur de base
                    }
                }
            });

            menuPanel.add(menuItemPanel);
            menuPanel.add(Box.createVerticalStrut(10)); // Ajoute de l'espace entre les éléments

            if (i == 0) { // Active le premier élément par défaut (Home)
                setActiveMenu(menuPanel, 0);
            }
        }

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        menuPanel.setOpaque(false);
        logoPanel.setOpaque(false);

        return sidebar;
    }

    private JPanel createTopbar() {
        RoundedSideBar topbar = new RoundedSideBar(80, 80, true, true, false, false);
        topbar.setBackground(new Color(4, 125, 154));
        topbar.setPreferredSize(new Dimension(getWidth(), 100));

        // Left Panel - Navigation Buttons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        ImageIcon directionLeft = getResizedIcon("/img/icons/8664926_circle_left_direction_icon.png", 30, 30);
        ImageIcon directionRight = getResizedIcon("/img/icons/8664924_circle_right_direction_icon.png", 30, 30);
        JLabel iconLabel1 = new JLabel(directionLeft);
        iconLabel1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel iconLabel2 = new JLabel(directionRight);
        iconLabel2.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listeners pour les boutons de navigation
        iconLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Bouton gauche cliqué !");
            }
        });

        iconLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Bouton droit cliqué !");
            }
        });

        leftPanel.add(iconLabel1);
        leftPanel.add(iconLabel2);
        leftPanel.setOpaque(false);

        // Center Panel - Search Bar
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        JTextField searchBar = new JTextField(30);
        searchBar.setPreferredSize(new Dimension(300, 30));
        searchBar.setFont(new Font("SansSerif", Font.PLAIN, 16));

        ImageIcon search = getResizedIcon("/img/icons/8666693_search_icon.png", 30, 30);
        JLabel iconSearch = new JLabel(search);
        iconSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listener pour l'icône de recherche
        iconSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Recherche : " + searchBar.getText());
            }
        });

        centerPanel.add(searchBar);
        centerPanel.add(iconSearch);
        centerPanel.setOpaque(false);

        // Right Panel - Dropdown Menu
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        ImageIcon menuIcon = getResizedIcon("/img/icons/8666601_more_vertical_icon.png", 30, 30);
        JLabel menuLabel = new JLabel(menuIcon);
        menuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Création du menu déroulant stylisé
        JPopupMenu userMenu = new JPopupMenu();
        userMenu.setPreferredSize(new Dimension(180, 150));
        userMenu.setBackground(new Color(1, 33, 41));

        String[] menuOptions = {"Connexion", "Déconnexion", "New User", "Profil"};
        String[] menuIcons = {"/img/icons/8666692_power_icon.png", "/img/icons/8666757_lock_security_icon.png",
                "/img/icons/8666546_user_plus_icon.png", "/img/icons/8664913_sun_sunny_weather_icon.png"};

        Utilisateur user = utilisateurService.getUtilisateurConnecte();

        for (int i = 0; i < menuOptions.length; i++) {
            if (user != null && !menuOptions[i].equals("Connexion")) {
                if (user.getRole().equals("RESPONSABLE") || !menuOptions[i].equals("New User")) {
                    JMenuItem item = createStyledMenuItem(menuOptions[i], menuIcons[i]);

                    // Ajout des actions spécifiques aux éléments du menu
                    switch (menuOptions[i]) {
                        case "Déconnexion":
                            item.addActionListener(e -> {
                                utilisateurService.deconnexion();

                                // Fermer toutes les fenêtres ouvertes et ouvrir LoginFrame
                                Window[] windows = Window.getWindows();
                                for (Window window : windows) {
                                    if (window instanceof JFrame) {
                                        window.dispose();
                                    }
                                }

                                new LoginFrame();
                            });
                            break;
                        case "Profil":
                            item.addActionListener(e -> updateMainContent(new ProfileUser(this, user)));
                            break;
                        case "New User":
                            item.addActionListener(e -> {
                                String[] champs = {"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
                                JTextField[] textFields = new JTextField[champs.length];
                                new Formulaire11ChampsFrame("Ajouter utilisateur", "create", "users", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
                            });
                            break;
                    }

                    userMenu.add(item);
                }
            }
        }

        // Afficher le menu au clic sur l'icône
        menuLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                userMenu.show(menuLabel, 0, menuLabel.getHeight());
            }
        });

        rightPanel.add(menuLabel);
        rightPanel.setOpaque(false);

        // Ajouter les panels à la topbar
        topbar.add(leftPanel, BorderLayout.WEST);
        topbar.add(centerPanel, BorderLayout.CENTER);
        topbar.add(rightPanel, BorderLayout.EAST);

        return topbar;
    }

    /**
     * Méthode pour créer un JMenuItem stylisé avec icône et effet au survol.
     */
    private JMenuItem createStyledMenuItem(String text, String iconPath) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("SansSerif", Font.PLAIN, 16));
        item.setForeground(Color.WHITE);
        item.setBackground(new Color(1, 33, 41));
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Assigner l'icône
        ImageIcon icon = getResizedIcon(iconPath, 25, 25);
        item.setIcon(icon);

        // Effet au survol
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(3, 50, 60));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(1, 33, 41));
            }
        });

        return item;
    }

    private JPanel createMainContent() {
        return mainContent;
    }

    private void setActiveMenu(JPanel menuPanel, int activeIndex) {
        int panelIndex = 0;

        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel menuItem = (JPanel) comp;

                if (panelIndex == activeIndex) {
                    menuItem.setBackground(new Color(3, 50, 60)); // Teinte proche du sidebar
                    menuItem.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(100, 200, 255))); // Bordure bleu clair pour un effet moderne
                    activeMenuItem = menuItem;

                    // Met à jour le contenu principal
                    switch (panelIndex) {
                        case 0 -> updateMainContent(new Home());
                        case 1 -> updateMainContent(new FormationPanel(this));
                        case 2 -> updateMainContent(new ModulePanel(this));
                        case 3 -> updateMainContent(new EtudiantPanel(this));
                        case 4 -> updateMainContent(new NotePanel(this));
                        case 5 -> updateMainContent(new Resultat());
                        case 6 -> updateMainContent(new GlobalResult());
                        case 7 -> updateMainContent(new EnseignantPanel(this));
                        case 8 -> updateMainContent(new StatistiquesUI());
                        case 9 -> updateMainContent(new UtilisateurPanel(this));
                    }

                } else {
                    menuItem.setBackground(new Color(1, 33, 41)); // Couleur de fond du sidebar
                    menuItem.setBorder(BorderFactory.createEmptyBorder());
                }
                panelIndex++;
            }
        }
    }

    public void updateMainContent(JPanel newContent) {
        getContentPane().remove(mainContent);
        mainContent = newContent;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(mainContent, gbc);
        revalidate();
        repaint();
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

    private ImageIcon getRoundedIcon(String resourcePath, int width, int height) {
        try {
            InputStream imgStream = getClass().getResourceAsStream(resourcePath);
            if (imgStream == null) return new ImageIcon();
            BufferedImage img = ImageIO.read(imgStream);
            if (img == null) return new ImageIcon();

            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = roundedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, width, height));
            g2.drawImage(resizedImg, 0, 0, width, height, null);
            g2.dispose();
            return new ImageIcon(roundedImage);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
