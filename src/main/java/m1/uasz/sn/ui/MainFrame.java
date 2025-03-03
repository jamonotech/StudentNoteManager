package m1.uasz.sn.ui;

import m1.uasz.sn.ui.components.*;
import m1.uasz.sn.ui.components.Module;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class MainFrame extends JFrame {
    private JPanel activeMenuItem = null;
    private JPanel mainContent = new Home();

    public MainFrame() {
        setTitle("JAMONO SCHOOL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridBagLayout());

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
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(50, 50, 50));
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
        menuPanel.setBackground(new Color(50, 50, 50));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        String[] menuItems = {"Home", "Formations", "Modules", "Étudiants", "Notes", "Résultats", "Enseignants", "Utilisateurs"};
        String[] iconPaths = {"/img/icons/8666691_home_icon.png", "/img/icons/8666671_briefcase_icon.png", "/img/icons/8666759_layers_layer_icon.png", "/img/icons/8666755_users_group_icon.png",
                "/img/icons/8664843_pen_to_square_icon.png", "/img/icons/8666782_award_prize_icon.png", "/img/icons/8666755_users_group_icon.png", "/img/icons/8664925_circle_user_person_icon.png"};

        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItemPanel = new JPanel();
            menuItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10)); // Espacement amélioré
            menuItemPanel.setPreferredSize(new Dimension(180, 40)); // Augmente la hauteur des éléments
            menuItemPanel.setMaximumSize(new Dimension(180, 40));
            menuItemPanel.setBackground(new Color(50, 50, 50));
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
                        menuItemPanel.setBackground(new Color(70, 70, 70)); // Effet au survol
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (menuItemPanel != activeMenuItem) {
                        menuItemPanel.setBackground(new Color(50, 50, 50)); // Retour à la couleur de base
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

        return sidebar;
    }

    private JPanel createTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(new Color(81, 78, 78));
        topbar.setPreferredSize(new Dimension(getWidth(), 63));

        // Left Panel - Navigation Buttons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        ImageIcon directionLeft = getResizedIcon("/img/icons/8664926_circle_left_direction_icon.png", 30, 30);
        ImageIcon directionRight = getResizedIcon("/img/icons/8664924_circle_right_direction_icon.png", 30, 30);
        JLabel iconLabel1 = new JLabel(directionLeft);
        iconLabel1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel iconLabel2 = new JLabel(directionRight);
        iconLabel2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftPanel.add(iconLabel1);
        leftPanel.add(iconLabel2);

        // Center Panel - Search Bar
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        JTextField searchBar = new JTextField(30);
        searchBar.setPreferredSize(new Dimension(300, 30));
        searchBar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ImageIcon search = getResizedIcon("/img/icons/8666693_search_icon.png", 30, 30);
        JLabel iconSearch = new JLabel(search);
        iconSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centerPanel.add(searchBar);
        centerPanel.add(iconSearch);

        // Right Panel - Dropdown Menu with Styled Items
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        ImageIcon menuIcon = getResizedIcon("/img/icons/8666601_more_vertical_icon.png", 30, 30);
        JLabel menuLabel = new JLabel(menuIcon);

            // Création du menu déroulant stylisé
        JPopupMenu userMenu = new JPopupMenu();
        userMenu.setPreferredSize(new Dimension(180, 150)); // Ajustement de la taille
        userMenu.setBackground(new Color(81, 78, 78)); // Fond gris foncé

        String[] menuOptions = {"Connexion", "Déconnexion", "S'inscrire", "Profil"};
        String[] menuIcons = {"/img/icons/8666692_power_icon.png", "/img/icons/8666757_lock_security_icon.png",
                "/img/icons/8666546_user_plus_icon.png", "/img/icons/8664913_sun_sunny_weather_icon.png"};

        for (int i = 0; i < menuOptions.length; i++) {
            JMenuItem item = new JMenuItem(menuOptions[i]);
            item.setFont(new Font("SansSerif", Font.PLAIN, 16));
            item.setForeground(Color.WHITE);
            item.setBackground(new Color(81, 78, 78));
            item.setOpaque(true);
            item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            // Assigner directement l'icône au JMenuItem
            ImageIcon icon = getResizedIcon(menuIcons[i], 25, 25);
            item.setIcon(icon);

            // Effet au survol
            item.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    item.setBackground(new Color(100, 100, 100));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    item.setBackground(new Color(81, 78, 78));
                }
            });

            userMenu.add(item);
        }

        // Afficher le menu au clic sur l'icône
        menuLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                userMenu.show(menuLabel, 0, menuLabel.getHeight());
            }
        });

        rightPanel.add(menuLabel);

        // Add panels to topbar
        topbar.add(leftPanel, BorderLayout.WEST);
        topbar.add(centerPanel, BorderLayout.CENTER);
        topbar.add(rightPanel, BorderLayout.EAST);

        return topbar;
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
                    menuItem.setBackground(new Color(80, 80, 80));
                    menuItem.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE));
                    activeMenuItem = menuItem;

                    // Met à jour le contenu principal
                    switch (panelIndex) {
                        case 0 -> updateMainContent(new Home());
                        case 1 -> updateMainContent(new Formation());
                        case 2 -> updateMainContent(new Module());
                        case 3 -> updateMainContent(new Etudiant());
                        case 4 -> updateMainContent(new Note());
                        case 5 -> updateMainContent(new Resultat());
                        case 6 -> updateMainContent(new Enseignant());
                        case 7 -> updateMainContent(new Utilisateur());
                    }

                } else {
                    menuItem.setBackground(new Color(50, 50, 50));
                    menuItem.setBorder(BorderFactory.createEmptyBorder());
                }
                panelIndex++;
            }
        }
    }

    private void updateMainContent(JPanel newContent) {
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
