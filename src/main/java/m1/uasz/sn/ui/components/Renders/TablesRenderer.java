package m1.uasz.sn.ui.components.Renders;

import m1.uasz.sn.ui.components.Action_Validation.ComponentAction;
import m1.uasz.sn.ui.components.Action_Validation.ComponentValidation;
import m1.uasz.sn.ui.components.Forms.Formulaire11ChampsFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TablesRenderer extends JPanel implements TableCellRenderer {
    public TablesRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 5));
        setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll(); // Clean previous content

        JLabel viewIcon = createIconLabel("/img/icons/8664880_eye_view_icon.png", "Voir");
        JLabel editIcon = createIconLabel("/img/icons/8666683_edit_2_icon.png", "Modifier");
        JLabel deleteIcon = createIconLabel("/img/icons/8664938_trash_can_delete_remove_icon.png", "Supprimer");

        add(viewIcon);
        add(editIcon);
        add(deleteIcon);

        editIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                new Formulaire11ChampsFrame("Modifier utilisateur", "update", "users", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
            }
        });
        deleteIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                new Formulaire11ChampsFrame("Supprimer utilisateur", "delete", "users", champs, textFields, 500, 500, new ComponentValidation(), new ComponentAction());
            }
        });

        setOpaque(false);

        return this;
    }

    private final String[] champs = champsForm();
    private JTextField[] textFields = new JTextField[champs.length];

    private String[] champsForm() {
        String[] labels = {"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
        return labels;
    }

    private JLabel createIconLabel(String resourcePath, String tooltip) {
        JLabel label = new JLabel(getResizedIcon(resourcePath, 20, 20));
        label.setToolTipText(tooltip);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor for clickable icons

        // Hover effect with border
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setBorder(null);
            }
        });
        return label;
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
