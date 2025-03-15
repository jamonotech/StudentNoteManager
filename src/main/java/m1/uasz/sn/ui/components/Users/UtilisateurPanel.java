package m1.uasz.sn.ui.components.Users;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import m1.uasz.sn.models.Formation;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Formation.DetailsFormPanel;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.PanelShape.*;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Renders.TablesRendererEditor;

public class UtilisateurPanel extends RoundedSideBar {
    private UtilisateurService utilisateurService = new UtilisateurService();
    private DefaultTableModel model;

    public UtilisateurPanel(MainFrame mainFrame) {
        super(80, 80, false, false, true, true);
        setPreferredSize(new Dimension(getWidth(), getHeight()));
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ Titre modernisé
        JLabel titreLabel = new JLabel("Liste des Utilisateurs");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

// ✅ Icône cliquable à droite du titre
        JLabel iconLabel = new JLabel(getResizedIcon("/img/icons/8664849_square_plus_icon.png", 30, 30)); // Remplacez le chemin par le vôtre
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en main pour l'icône cliquable

// Ajout de l'effet au survol de l'icône
        iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String[] champs = {"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
                JTextField[] textFields = new JTextField[champs.length];
                Formulaire11ChampsFrame form = new Formulaire11ChampsFrame("Ajouter utilisateur", "create", "users", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerUtilisateurs(); // Rafraîchir après modification
                    }
                });
            }
        });

// ✅ Panel pour contenir le titre et l'icône à droite
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titreLabel, BorderLayout.CENTER);
        titlePanel.add(iconLabel, BorderLayout.EAST);

// ✅ Ajout du panel avec titre et icône
        add(titlePanel, BorderLayout.NORTH);


        // Table columns
        String[] columns = {"ID", "Nom", "Prénom", "Email", "Rôle", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(2, 119, 232));

        // ✅ Centrage des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ✅ Personnalisation de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // ✅ Ajout du rendu et de l'éditeur des icônes dans la colonne "Actions"
        TablesRendererEditor usersEditor = new TablesRendererEditor("users", new ActionHandler() {

            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir utilisateur ID : " + id);
                Utilisateur utilisateur = utilisateurService.trouverUtilisateur((Long) id);
                JPanel detailsPanel = new DetailsUsersPanel(mainFrame, utilisateur);
                mainFrame.updateMainContent(detailsPanel);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier utilisateur ID : " + id);
                Utilisateur user = utilisateurService.trouverUtilisateur((Long) id);
                // Mapper correctement les valeurs de l'utilisateur
                String[] champs = {"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
                JTextField[] textFields = new JTextField[champs.length];

                textFields[0] = new JTextField();
                textFields[0].setText(user.getRole());
                textFields[1] = new JTextField();
                textFields[1].setText(user.getNom());
                textFields[2] = new JTextField();
                textFields[2].setText(user.getPrenom());
                textFields[3] = new JTextField();
                textFields[3].setText(user.getEmail());
                textFields[4] = new JTextField();
                textFields[4].setText(user.getPassword());
                textFields[5] = new JTextField();
                textFields[5].setText(user.getPassword());

                new Formulaire11ChampsFrame("Modifier utilisateur", "update", "users", champs, textFields, 800, 570,
                        new ComponentValidation(), new ComponentAction(mainFrame));
                chargerUtilisateurs();
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                System.out.println("Supprimer utilisateur ID : " + id);
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    utilisateurService.supprimerUtilisateur(utilisateurService.trouverUtilisateur((Long) id));
                    // Rafraîchir les données ici
                    chargerUtilisateurs();
                }
            }
        });
        TableColumn column = table.getColumnModel().getColumn(5);
        column.setCellRenderer(usersEditor);
        column.setCellEditor(usersEditor);


        // ✅ Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE);

        // ✅ Ajout des composants
//        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Load users from the database
        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        model.setRowCount(0); // Clear the table before populating
        List<Utilisateur> utilisateurs = utilisateurService.listerUtilisateurs();
        for (Utilisateur user : utilisateurs) {
            model.addRow(new Object[]{
                    user.getId(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getEmail(),
                    user.getRole(),
                    "Actions" // Placeholder for action icons
            });
        }

        SwingUtilities.invokeLater(() -> {
            model.fireTableDataChanged();
        });
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
