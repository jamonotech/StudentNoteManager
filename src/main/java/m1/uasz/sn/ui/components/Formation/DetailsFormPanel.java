package m1.uasz.sn.ui.components.Formation;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.EnseignantService;
import m1.uasz.sn.services.EtudiantService;
import m1.uasz.sn.services.ModuleService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentDetailsAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.FormulaireComboBox;
import m1.uasz.sn.ui.components.PanelShape.*;
import m1.uasz.sn.ui.components.Renders.TableDetailsRendererEditor;
import org.hibernate.Hibernate;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailsFormPanel extends RoundedSideBar {
    private JTabbedPane tabbedPane;
    private JPanel infoPanel;
    private JPanel modulesPanel;
    private JPanel etudPanel;
    private DefaultTableModel model;
    private DefaultTableModel modelEtud;
    private JTable table;
    private ModuleService moduleService = new ModuleService();
    private EnseignantService enseignantService = new EnseignantService();
    private ModuleDAO moduleDAO = new ModuleDAO();
    private EtudiantService etudiantService = new EtudiantService();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    public DetailsFormPanel(MainFrame mainFrame, Formation formation) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ✅ Titre général "DETAILS COMPOSANT"
        JLabel titreComposantLabel = new JLabel("🛠️ DETAILS FORMATION");
        titreComposantLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titreComposantLabel.setForeground(Color.WHITE);
        titreComposantLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreComposantLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // ✅ Panneau du titre avec couleur de fond
        JPanel titrePanel = new JPanel(new BorderLayout());
        titrePanel.setBackground(new Color(4, 125, 154));
        titrePanel.add(titreComposantLabel, BorderLayout.CENTER);

        // ✅ Ajout du titre avant les tabbed panes
        add(titrePanel, BorderLayout.NORTH);



        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);



        // Onglet Informations
        // Onglet Informations avec deux colonnes (Label à gauche, Valeur à droite)
//        infoPanel = new JPanel(new GridBagLayout());
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(4, 125, 154), 2),
                new EmptyBorder(20, 40, 20, 40)) // Augmenter l'espacement
        );

        // Ajout des champs en deux colonnes
        String enseignantNom = (formation.getResponsablePedagogique() != null) ?
                formation.getResponsablePedagogique().getPrenom() + " " + formation.getResponsablePedagogique().getNom() : "Aucun";

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 20, 10)); // Espacement de 20px entre colonnes
        fieldsPanel.setBackground(Color.WHITE);
        infoPanel.add(fieldsPanel, BorderLayout.CENTER);

        ajouterChamp(fieldsPanel, "Nom de la formation : ", formation.getNom());
        ajouterChamp(fieldsPanel, "Niveau : ", formation.getNiveau());
        ajouterChamp(fieldsPanel, "Nom du responsable : ", enseignantNom);
        ajouterChamp(fieldsPanel, "Email : ", formation.getResponsableEmail());
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");

        // Ajouter le panneau à l'onglet
        tabbedPane.addTab("Informations", infoPanel);



//------------------------------------------------------------------------------------------------------//



        // Onglet Modules Assignés
        modulesPanel = new JPanel(new BorderLayout());
        modulesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        modulesPanel.setBackground(Color.WHITE);

        // ✅ Titre modernisé
        JLabel titreModLabel = new JLabel("📚 Liste des Modules");
        titreModLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreModLabel.setForeground(Color.WHITE);
        titreModLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreModLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panneau pour le titre et l'icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(4, 125, 154));

        // ✅ Icône "Ajouter"
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

        // ✅ Gestion de l'événement au clic
        ajouterIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String champs = "Modules"; // Seul le champ comboBox est nécessaire
                List<String> comboBoxes = new ArrayList<>();
                List<Module> moduleList = moduleService.listerModule();
                // Vérifier que la liste des modules n'est pas vide
                if (moduleList != null && !moduleList.isEmpty()) {
                    for (Module module : moduleList) {
                        comboBoxes.add(module.getCode() + " " + module.getNom());
                    }
                }

                FormulaireComboBox form = new FormulaireComboBox(
                        "Ajouter module",
                        "create",
                        "trainings",
                        "modules",
                        formation.getNom(),
                        champs,
                        comboBoxes,
                        500,
                        300,
                        new ComponentValidation(),
                        new ComponentDetailsAction()
                );

                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerModules(formation);
                    }
                });
            }
        });

        // ✅ Ajout de l'icône à droite du header
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(4, 125, 154));
        rightPanel.add(ajouterIcon);
        headerPanel.add(titreModLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // ✅ Ajout du header au panneau principal des modules
        modulesPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Code", "Nom", "Volume Horaire", "Coefficient", "Crédits", "Actions"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 230, 255));

        // ✅ Centrage des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(2, 119, 232));
        header.setForeground(Color.WHITE);

        TableDetailsRendererEditor editor = new TableDetailsRendererEditor("trainings", new ActionHandler() {
            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir module ID : " + id);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier module ID : " + id);
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");

                if (confirmation == JOptionPane.YES_OPTION) {
                    m1.uasz.sn.models.Module module = moduleService.trouverModule((Long) id);

                    if (module == null) {
                        JOptionPane.showMessageDialog(null, "Erreur : Module introuvable !");
                        return;
                    }

                    // Vérifier si le module est assigné à un enseignant avant suppression
                    Hibernate.initialize(formation.getModules());
                    boolean moduleDejaAssigne = formation.getModules().stream()
                            .anyMatch(m -> m.getCode().equals(module.getCode()));

                    if (!moduleDejaAssigne) {
                        JOptionPane.showMessageDialog(null, "Ce module n'est pas assigné à cette formation !");
                    } else {
                        moduleService.supprimerModuleFormation(module.getCode(), formation.getId());
                        JOptionPane.showMessageDialog(null, "Module désaffecté avec succès !");
                    }
                    chargerModules(formation);
                }
            }
        });
        table.getColumnModel().getColumn(6).setCellRenderer(editor);
        table.getColumnModel().getColumn(6).setCellEditor(editor);

        modulesPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tabbedPane.addTab("Modules Assignés", modulesPanel);


        add(tabbedPane, BorderLayout.CENTER);
        chargerModules(formation);



        //------------------------------------------------------------------------------------------------------//



        // Onglet Étudiants Assignés
        etudPanel = new JPanel(new BorderLayout());
        etudPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        etudPanel.setBackground(Color.WHITE);

        // ✅ Titre correct pour l'onglet étudiants
        JLabel titreEtudLabel = new JLabel("📋 Liste des Étudiants");
        titreEtudLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titreEtudLabel.setForeground(Color.WHITE);
        titreEtudLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreEtudLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panel d'en-tête avec fond coloré
        JPanel headerEtudPanel = new JPanel(new BorderLayout());
        headerEtudPanel.setBackground(new Color(4, 125, 154));

        // ✅ Icône "Ajouter Étudiant"
        JLabel addEtudIcon = new JLabel();
        try {
            InputStream input = getClass().getResourceAsStream("/img/icons/8664849_square_plus_icon.png");
            if (input != null) {
                BufferedImage addIcons = ImageIO.read(input);
                addEtudIcon.setIcon(new ImageIcon(addIcons.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addEtudIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ Gestion de l'événement pour ajouter un étudiant
        addEtudIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String champs = "Étudiants";
                List<String> comboBoxes = new ArrayList<>();
                List<m1.uasz.sn.models.Etudiant> etudiantList = etudiantService.listerEtudiants();

                if (etudiantList != null && !etudiantList.isEmpty()) {
                    for (m1.uasz.sn.models.Etudiant etudiant : etudiantList) {
                        comboBoxes.add(etudiant.getIne() + " " + etudiant.getPrenoms() + " " + etudiant.getNom());
                    }
                }

                FormulaireComboBox form = new FormulaireComboBox(
                        "Ajouter Étudiant",
                        "create",
                        "trainings",
                        "students",
                        formation.getNom(),
                        champs,
                        comboBoxes,
                        500,
                        300,
                        new ComponentValidation(),
                        new ComponentDetailsAction()
                );

                form.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        chargerEtud(formation);
                    }
                });
            }
        });

        // ✅ Ajout de l'icône à droite du header
        JPanel rightEtudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightEtudPanel.setBackground(new Color(4, 125, 154));
        rightEtudPanel.add(addEtudIcon);

        headerEtudPanel.add(titreEtudLabel, BorderLayout.CENTER);
        headerEtudPanel.add(rightEtudPanel, BorderLayout.EAST);

        // ✅ Ajout du header au panneau principal des étudiants
        etudPanel.add(headerEtudPanel, BorderLayout.NORTH);

        // ✅ Colonnes adaptées aux étudiants
        String[] columnsEtud = {"INE", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email"};
        modelEtud = new DefaultTableModel(columnsEtud, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable tableEtud = new JTable(modelEtud);
        tableEtud.setRowHeight(45);
        tableEtud.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableEtud.setSelectionBackground(new Color(200, 230, 255));

        // ✅ Centrage des cellules
        DefaultTableCellRenderer centerRendererEtud = new DefaultTableCellRenderer();
        centerRendererEtud.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableEtud.getColumnCount(); i++) {
            tableEtud.getColumnModel().getColumn(i).setCellRenderer(centerRendererEtud);
        }

        JTableHeader headerEtud = tableEtud.getTableHeader();
        headerEtud.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerEtud.setBackground(new Color(2, 119, 232));
        headerEtud.setForeground(Color.WHITE);

        // ✅ Gestion des actions des étudiants
        TableDetailsRendererEditor editorEtud = new TableDetailsRendererEditor("trainings", new ActionHandler() {
            @Override
            public void onView(Object id, String callingComponent) {
                System.out.println("Voir étudiant ID : " + id);
            }

            @Override
            public void onEdit(Object id, String callingComponent) {
                System.out.println("Modifier étudiant ID : " + id);
            }

            @Override
            public void onDelete(Object id, String callingComponent) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");

                if (confirmation == JOptionPane.YES_OPTION) {
                    m1.uasz.sn.models.Etudiant etudiant = etudiantService.trouverEtudiant((String) id);

                    if (etudiant == null) {
                        JOptionPane.showMessageDialog(null, "Erreur : Étudiant introuvable !");
                        return;
                    }

                    etudiantService.desinscrireEtudiantFormation(etudiant.getIne(), formation.getId());
                    JOptionPane.showMessageDialog(null, "Étudiant désaffecté avec succès !");
                    chargerEtud(formation);
                }
            }
        });
        tableEtud.getColumnModel().getColumn(6).setCellRenderer(editorEtud);
        tableEtud.getColumnModel().getColumn(6).setCellEditor(editorEtud);

        // ✅ Ajout du tableau des étudiants
        etudPanel.add(new JScrollPane(tableEtud), BorderLayout.CENTER);
        tabbedPane.addTab("Étudiants", etudPanel);

        chargerEtud(formation);
    }

    private void ajouterChamp(JPanel panel, String label, String value) {
        JLabel champLabel = new JLabel(label );
        champLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        champLabel.setForeground(new Color(33, 33, 33));

        JLabel champValue = new JLabel(value);
        champValue.setFont(new Font("SansSerif", Font.PLAIN, 16));

        panel.add(champLabel);
        panel.add(champValue);
    }

    private void chargerModules(Formation formation) {
        List<Module> modules = moduleDAO.findModulesByFormation(formation);
        model.setRowCount(0);
        for (Module module : modules) {
            model.addRow(new Object[]{module.getId(), module.getCode(), module.getNom(), module.getVolumeHoraire(), module.getCoefficient(), module.getCredits()});
        }
    }

    private void chargerEtud(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);
        modelEtud.setRowCount(0);
        for (Etudiant etu: etudiants) {
            modelEtud.addRow(new Object[]{etu.getIne(), etu.getNom(), etu.getPrenoms(), etu.getDateNaissance(), etu.getSexe(), etu.getAdresse(), etu.getEmail(), ""});
        }
    }
}
