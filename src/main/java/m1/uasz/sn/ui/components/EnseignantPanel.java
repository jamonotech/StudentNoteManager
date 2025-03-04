package m1.uasz.sn.ui.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import m1.uasz.sn.services.EnseignantService;

public class EnseignantPanel extends JPanel {
    private EnseignantService enseignantService = new EnseignantService();
    private DefaultTableModel model;

    public EnseignantPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titreLabel = new JLabel("Liste des Enseignants");
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Colonnes du tableau (ajout des nouvelles colonnes)
        String[] columns = {
                "Matricule", "Nom", "Prénom", "Date de Naissance", "Sexe", "Adresse", "Email",
                "Grade", "Spécialité", "Bureau", "Institution", "Actions"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 11; // Seules les icônes sont cliquables
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);

        // Personnalisation de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));

        // Appliquer TableRenderer pour les icônes
        TableColumn column = table.getColumnModel().getColumn(11);
        column.setCellRenderer(new TableRenderer());

        // Panel pour le tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter les composants
        add(titreLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Charger les enseignants depuis la BD
        chargerEnseignants();
    }

    private void chargerEnseignants() {
        List<m1.uasz.sn.models.Enseignant> enseignants = enseignantService.listerEnseignants();
        model.setRowCount(0); // Nettoyer le tableau avant de le remplir
        for (m1.uasz.sn.models.Enseignant ens : enseignants) {
            model.addRow(new Object[]{
                    ens.getMatricule(),
                    ens.getNom(),
                    ens.getPrenoms(),
                    ens.getDateNaissance(), // Format de date si nécessaire
                    ens.getSexe(),
                    ens.getAdresse(),
                    ens.getEmail(),
                    ens.getGrade(),
                    ens.getSpecialite(),
                    ens.getBureau(),
                    ens.getInstitution(),
                    "" // Pour les icônes d'actions
            });
        }
    }
}

// ✅ Définition de TableRenderer pour gérer l'affichage des icônes
class TableRenderer extends JPanel implements TableCellRenderer {
    public TableRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 2)); // Ajustement de l'espacement
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll(); // Nettoyer l'ancien contenu

        // Charger les icônes
        JLabel detailIcon = new JLabel(getResizedIcon("/img/icons/8664880_eye_view_icon.png", 19, 19));
        JLabel editIcon = new JLabel(getResizedIcon("/img/icons/8666683_edit_2_icon.png", 19, 19));
        JLabel deleteIcon = new JLabel(getResizedIcon("/img/icons/8664938_trash_can_delete_remove_icon.png", 19, 19));

        // Ajout des icônes avec un espacement uniforme
        add(detailIcon);
        add(editIcon);
        add(deleteIcon);

        return this;
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
