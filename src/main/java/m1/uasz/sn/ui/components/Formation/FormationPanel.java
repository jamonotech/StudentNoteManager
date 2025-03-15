package m1.uasz.sn.ui.components.Formation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Enseignant.DetailsEnsPanel;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.PanelShape.*;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.services.FormationService;
import m1.uasz.sn.services.StatistiquesService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Renders.TablesRendererEditor;

public class FormationPanel extends RoundedSideBar {
    private FormationService formationService = new FormationService();
    private DefaultTableModel model;

    public FormationPanel(MainFrame mainFrame) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // ✅ Titre modernisé
        JLabel titreLabel = new JLabel("📚 Liste des Formations");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panneau pour le titre et l'icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(4, 125, 154)); // Même couleur de fond que votre panneau principal

        // ✅ Icône "Ajouter"
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));

        // ✅ Charger l'icône "+"
        JLabel ajouterIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664849_square_plus_icon.png");
            if (input != null) {
                BufferedImage addIcon = ImageIO.read(input);
                ajouterIcon.setIcon(new ImageIcon(addIcon.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ajouterIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en main pour l'icône cliquable

// Ajout de l'effet au survol de l'icône
        ajouterIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String[] champs = {"Nom", "Niveau", "Responsable Nom", "Responsable Email"};
                JTextField[] textFields = new JTextField[champs.length];
                Formulaire11ChampsFrame form = new Formulaire11ChampsFrame("Ajouter Formation", "create", "trainings", champs, textFields, 500, 430, new ComponentValidation(), new ComponentAction());
                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerFormations(); // Rafraîchir après modification
                    }
                });
            }
        });

        rightPanel.add(ajouterIcon);

        headerPanel.add(rightPanel, BorderLayout.EAST);  // Icône à droite

        // ✅ Colonnes du tableau
        String[] columns = {
                "ID", "Nom", "Niveau", "Responsable Nom", "Responsable Email", "Actions"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seules les icônes sont cliquables
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(200, 230, 255));

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
        TablesRendererEditor usersEditor = new TablesRendererEditor("trainings", new ActionHandler() {

            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir utilisateur ID : " + id);
                Formation formation = formationService.trouverFormation((Long) id);
                JPanel detailsPanel = new DetailsFormPanel(mainFrame, formation);
                mainFrame.updateMainContent(detailsPanel);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier utilisateur ID : " + id);
                Formation user = formationService.trouverFormation((Long) id);
                // Mapper correctement les valeurs de l'utilisateur
                String[] champs = {"Nom", "Niveau", "Responsable Nom", "Responsable Email"};
                JTextField[] textFields = new JTextField[champs.length];

                textFields[0] = new JTextField();
                textFields[0].setText(user.getNom());
                textFields[1] = new JTextField();
                textFields[1].setText(user.getNiveau());
                textFields[2] = new JTextField();
                textFields[2].setText(user.getResponsableNom());
                textFields[3] = new JTextField();
                textFields[3].setText(user.getResponsableEmail());

                new Formulaire11ChampsFrame("Modifier formation", "update", "trainings", champs, textFields, 800, 570,
                        new ComponentValidation(), new ComponentAction(mainFrame));
                chargerFormations();
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                System.out.println("Supprimer utilisateur ID : " + id);
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    formationService.supprimerFormation(formationService.trouverFormation((Long) id));
                    // Rafraîchir les données ici
                    chargerFormations();
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
        add(titreLabel, BorderLayout.NORTH);
        add(headerPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Charger les formations depuis la BD
        chargerFormations();
    }

    private void chargerFormations() {
        List<m1.uasz.sn.models.Formation> formations = formationService.listerFormations();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Formation formation : formations) {
            model.addRow(new Object[]{
                    formation.getId(),
                    formation.getNom(),
                    formation.getNiveau(),
                    formation.getResponsableNom(),
                    formation.getResponsableEmail(),
                    "" // Pour les icônes d'actions
            });
        }
        SwingUtilities.invokeLater(() -> {
            model.fireTableDataChanged();
        });
    }
}
