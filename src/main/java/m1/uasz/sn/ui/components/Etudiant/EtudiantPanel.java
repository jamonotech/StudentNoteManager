package m1.uasz.sn.ui.components.Etudiant;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Formation.DetailsFormPanel;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
import m1.uasz.sn.ui.components.PanelShape.*;
import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.services.EtudiantService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Renders.TablesRendererEditor;

public class EtudiantPanel extends RoundedSideBar {
    private EtudiantService etudiantService = new EtudiantService();
    private DefaultTableModel model;

    public EtudiantPanel(MainFrame mainFrame) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel titreLabel = new JLabel("🎓 Liste des Étudiants");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreLabel.setForeground(Color.WHITE);
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(4, 125, 154));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));

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

        ajouterIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ajouterIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String[] champs = {"INE", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email", "Formation"};
                JTextField[] textFields = new JTextField[champs.length];
                Formulaire11ChampsFrame form = new Formulaire11ChampsFrame("Ajouter Étudiant", "create", "students", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerEtudiants();
                    }
                });
            }
        });
        rightPanel.add(ajouterIcon);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        String[] columns = {"INE", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(200, 230, 255));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);

        TablesRendererEditor etudiantEditor = new TablesRendererEditor("students", new ActionHandler() {
            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir utilisateur ID : " + id);
                Etudiant etudiant = etudiantService.trouverEtudiant((String) id);
                JPanel detailsPanel = new DetailsEtudPanel(mainFrame, etudiant);
                mainFrame.updateMainContent(detailsPanel);
            }
            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier étudiant INE : " + id);
                Etudiant etudiant = etudiantService.trouverEtudiant((String) id);
                String[] champs = {"INE", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email", "Formation"};
                JTextField[] textFields = new JTextField[champs.length];

                textFields[0] = new JTextField(etudiant.getIne());
                textFields[1] = new JTextField(etudiant.getNom());
                textFields[2] = new JTextField(etudiant.getPrenoms());
                textFields[3] = new JTextField(etudiant.getDateNaissance().toString());
                textFields[4] = new JTextField(etudiant.getSexe());
                textFields[5] = new JTextField(etudiant.getAdresse());
                textFields[6] = new JTextField(etudiant.getEmail());
                textFields[7] = new JTextField(etudiant.getFormation().getNom());

                new Formulaire11ChampsFrame("Modifier Étudiant", "update", "students", champs, textFields, 800, 570,
                        new ComponentValidation(), new ComponentAction(mainFrame));
                chargerEtudiants();
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                System.out.println("Supprimer étudiant INE : " + id);
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    etudiantService.supprimerEtudiant(etudiantService.trouverEtudiant((String) id));
                    chargerEtudiants();
                }
            }
        });
        TableColumn column = table.getColumnModel().getColumn(7);
        column.setCellRenderer(etudiantEditor);
        column.setCellEditor(etudiantEditor);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE);

        add(titreLabel, BorderLayout.NORTH);
        add(headerPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        chargerEtudiants();
    }

    private void chargerEtudiants() {
        List<Etudiant> etudiants = etudiantService.listerEtudiants();
        model.setRowCount(0);
        for (Etudiant etu : etudiants) {
            model.addRow(new Object[]{etu.getIne(), etu.getNom(), etu.getPrenoms(), etu.getDateNaissance(), etu.getSexe(), etu.getAdresse(), etu.getEmail(), ""});
        }
        SwingUtilities.invokeLater(model::fireTableDataChanged);
    }
}
