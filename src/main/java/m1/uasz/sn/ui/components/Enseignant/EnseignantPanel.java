package m1.uasz.sn.ui.components.Enseignant;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.services.EnseignantService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.PanelShape.*;
import m1.uasz.sn.ui.components.Renders.TablesRendererEditor;


public class EnseignantPanel extends RoundedSideBar {
    private EnseignantService enseignantService = new EnseignantService();
    private DefaultTableModel model;
    private ModuleDAO moduleDAO = new ModuleDAO();

    public EnseignantPanel(MainFrame mainFrame) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // ✅ Titre stylisé
        JLabel titreLabel = new JLabel("🎓 Liste des Enseignants");
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
                String[] champs = {"Matricule", "Nom", "Prénoms", "Date de Naissance", "Sexe", "Adresse", "Email",
                        "Grade", "Spécialité", "Bureau", "Institution"};
                JTextField[] textFields = new JTextField[champs.length];
                Formulaire11ChampsFrame form = new Formulaire11ChampsFrame("Ajouter utilisateur", "create", "teachers", champs, textFields, 800, 670, new ComponentValidation(), new ComponentAction());
                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerEnseignants(); // Rafraîchir après modification
                    }
                });
            }
        });

        rightPanel.add(ajouterIcon);

        headerPanel.add(rightPanel, BorderLayout.EAST);  // Icône à droite

        // ✅ Colonnes du tableau
        String[] columns = {"Matricule", "Nom", "Prénom", "Date de Naissance", "Adresse", "Email",
                "Grade", "Spécialité", "Bureau", "Institution", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10; // Seules les icônes sont cliquables
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(200, 230, 255));

        // ✅ Centrage du texte dans les cellules
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
        TablesRendererEditor usersEditor = new TablesRendererEditor("teachers", new ActionHandler() {

            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir utilisateur ID : " + id);
                Enseignant ens = enseignantService.trouverEnseignant((String) id);
                List<Module> modules = moduleDAO.findModulesByEnseignant(ens);
                JPanel detailsPanel = new DetailsEnsPanel(mainFrame, ens);
                mainFrame.updateMainContent(detailsPanel);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier enseignant ID : " + id);
                Enseignant ens = enseignantService.trouverEnseignant((String) id);

                // Mapping correct des champs
                String[] champs = {"Matricule", "Nom", "Prénoms", "Date de Naissance", "Sexe", "Adresse", "Email",
                        "Grade", "Spécialité", "Bureau", "Institution"};
                JTextField[] textFields = new JTextField[champs.length];

                textFields[0] = new JTextField(ens.getMatricule());
                textFields[1] = new JTextField(ens.getNom());
                textFields[2] = new JTextField(ens.getPrenom());
                textFields[3] = new JTextField(ens.getDateNaissance().toString());
                textFields[4] = new JTextField(ens.getSexe());
                textFields[5] = new JTextField(ens.getAdresse());
                textFields[6] = new JTextField(ens.getEmail());
                textFields[7] = new JTextField(ens.getGrade());
                textFields[8] = new JTextField(ens.getSpecialite());
                textFields[9] = new JTextField(ens.getBureau());
                textFields[10] = new JTextField(ens.getInstitution());

                new Formulaire11ChampsFrame("Modifier enseignant", "update", "teachers", champs, textFields, 800, 670,
                        new ComponentValidation(), new ComponentAction(mainFrame));
                chargerEnseignants();
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                System.out.println("Supprimer utilisateur ID : " + id);
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    enseignantService.supprimerEnseignant(enseignantService.trouverEnseignant((String) id));
                    // Rafraîchir les données ici
                    chargerEnseignants();
                }
            }
        });
        TableColumn column = table.getColumnModel().getColumn(10);
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

        // Charger les enseignants depuis la BD
        chargerEnseignants();
    }

    private void chargerEnseignants() {
        List<m1.uasz.sn.models.Enseignant> enseignants = enseignantService.listerEnseignants();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Enseignant ens : enseignants) {
            model.addRow(new Object[]{
                    ens.getMatricule(), ens.getNom(), ens.getPrenom(), ens.getDateNaissance(),
                    ens.getAdresse(), ens.getEmail(), ens.getGrade(),
                    ens.getSpecialite(), ens.getBureau(), ens.getInstitution(),
                    "" // Icônes
            });
        }
        SwingUtilities.invokeLater(() -> {
            model.fireTableDataChanged();
        });
    }
}
