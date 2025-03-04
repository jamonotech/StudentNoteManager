package m1.uasz.sn.ui;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.services.UtilisateurService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class LoginFrame extends JFrame {
    private final UtilisateurService utilisateurService = new UtilisateurService(new UtilisateurDAO());

    public LoginFrame() {
        setTitle("JAMONO SCHOOL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("src/main/resources/img/background/b5.jpg");
        panel.setLayout(new GridBagLayout());

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(450, 575));
        loginPanel.setMinimumSize(new Dimension(400, 500));
        loginPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Logo
        ImageIcon logo = getRoundedIcon("/img/logo.jpg", 100, 100);
        JLabel logoLabel = new JLabel(logo);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(logoLabel, gbc);

        // Titre
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        loginPanel.add(titleLabel, gbc);

        // Email
        gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel emailLabel = new JLabel("Email :");
        styleLabel(emailLabel);
        loginPanel.add(emailLabel, gbc);

        gbc.gridy = 3;
        JTextField emailField = new JTextField();
        styleTextField(emailField);
        loginPanel.add(emailField, gbc);

        // Mot de passe
        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Mot de passe :");
        styleLabel(passwordLabel);
        loginPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);
        loginPanel.add(passwordField, gbc);

        // Bouton de connexion
        gbc.gridy = 6; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Connexion");
        styleButton(loginButton);
        loginPanel.add(loginButton, gbc);

        // Liens
        gbc.gridy = 7; gbc.gridwidth = 2;
        JLabel forgotPasswordLabel = new JLabel("<html><a href='#'>Mot de passe oublié ?</a></html>");
        styleLinkLabel(forgotPasswordLabel);
        loginPanel.add(forgotPasswordLabel, gbc);

        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel registerLabel = new JLabel("<html><font color='#FFFFFF'>Pas de compte ?</font> <a href='#'>S'inscrire</a></html>");
        styleLinkLabel(registerLabel);
        loginPanel.add(registerLabel, gbc);

        // Action du bouton de connexion
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (utilisateurService.authentifier(email, password)) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                new MainFrame(); // Ouvre la fenêtre principale
                dispose(); // Ferme la fenêtre de connexion
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginPanel);
        add(panel);
        setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(280, 50));
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
    }

    private void styleLinkLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private ImageIcon resizeIcon(String resourcePath, int width, int height) {
        java.net.URL imgURL = getClass().getResource(resourcePath);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("Image introuvable : " + resourcePath);
            return new ImageIcon();
        }
    }

    private ImageIcon getRoundedIcon(String resourcePath, int width, int height) {
        try {
            // Charge l'image depuis le classpath
            InputStream imgStream = getClass().getResourceAsStream(resourcePath);
            if (imgStream == null) {
                System.err.println("Image introuvable : " + resourcePath);
                return new ImageIcon();
            }

            BufferedImage img = ImageIO.read(imgStream);
            if (img == null) {
                System.err.println("Erreur de lecture de l'image : " + resourcePath);
                return new ImageIcon();
            }

            // Redimensionne l'image
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            // Crée une image arrondie
            BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = roundedImage.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, width, height));
            g2.drawImage(resizedImg, 0, 0, width, height, null);
            g2.dispose();

            return new ImageIcon(roundedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
