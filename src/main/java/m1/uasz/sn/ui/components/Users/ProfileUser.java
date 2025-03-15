package m1.uasz.sn.ui.components.Users;

import com.formdev.flatlaf.FlatLightLaf;
import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.models.*;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.services.EnseignantService;
import m1.uasz.sn.services.EtudiantService;
import m1.uasz.sn.services.ModuleService;
import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.MainFrame;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;
import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentDetailsAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;
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

public class ProfileUser extends RoundedSideBar {
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
    private UtilisateurService utilisateurService = new UtilisateurService();

    public ProfileUser(MainFrame mainFrame, Utilisateur utilisateur) {
        super(80, 80, false, false, true, true);
        setLayout(new BorderLayout());
        setBackground(new Color(4, 125, 154));
        FlatLightLaf.setup();
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ✅ Titre général "DETAILS COMPOSANT"
        JLabel titreComposantLabel = new JLabel("🛠️ PROFILE UTILISATEUR");
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
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 20, 10)); // Espacement de 20px entre colonnes
        fieldsPanel.setBackground(Color.WHITE);

        // ✅ Bouton d'icône en haut à droite
        JButton iconButton = new JButton();
        iconButton.setBorderPainted(false);
        iconButton.setContentAreaFilled(false);
        iconButton.setFocusPainted(false);
        iconButton.setToolTipText("Modifier les informations"); // Info-bulle

// Charger une icône depuis les ressources (si disponible)
        try {
            InputStream is = getClass().getResourceAsStream("/img/icons/8666683_edit_2_icon.png"); // Remplace avec le chemin réel
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                iconButton.setIcon(new ImageIcon(img.getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
            } else {
                iconButton.setText("✏️"); // Emoji comme alternative
            }
        } catch (Exception e) {
            iconButton.setText("✏️"); // Emoji en cas d'erreur de chargement
        }

// Ajouter un écouteur d'événement au clic
        iconButton.addActionListener(e -> {
            // Mapper correctement les valeurs de l'utilisateur
            String[] champs = {"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
            JTextField[] textFields = new JTextField[champs.length];

            textFields[0] = new JTextField();
            textFields[0].setText(utilisateur.getRole());
            textFields[1] = new JTextField();
            textFields[1].setText(utilisateur.getNom());
            textFields[2] = new JTextField();
            textFields[2].setText(utilisateur.getPrenom());
            textFields[3] = new JTextField();
            textFields[3].setText(utilisateur.getEmail());
            textFields[4] = new JTextField();
            textFields[4].setText(utilisateur.getPassword());
            textFields[5] = new JTextField();
            textFields[5].setText(utilisateur.getPassword());

            new Formulaire11ChampsFrame("Modifier utilisateur", "update", "profile", champs, textFields, 800, 570,
                    new ComponentValidation(), new ComponentAction(mainFrame));
        });

// ✅ Panneau pour placer l'icône en haut à droite
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(Color.WHITE);
        topRightPanel.add(iconButton);

        infoPanel.add(topRightPanel, BorderLayout.NORTH);
        infoPanel.add(fieldsPanel, BorderLayout.CENTER);

        ajouterChamp(fieldsPanel, "Prenom : ", utilisateur.getPrenom());
        ajouterChamp(fieldsPanel, "Nom : ", utilisateur.getNom());
        ajouterChamp(fieldsPanel, "Email : ", utilisateur.getEmail());
        ajouterChamp(fieldsPanel, "Role : ", utilisateur.getRole());
        ajouterChamp(fieldsPanel, "Password : ", utilisateur.getPassword());
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");
        ajouterChamp(fieldsPanel, "", "");

        // Ajouter le panneau à l'onglet
        tabbedPane.addTab("Informations", infoPanel);

        add(tabbedPane, BorderLayout.CENTER);


//------------------------------------------------------------------------------------------------------//

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
}
