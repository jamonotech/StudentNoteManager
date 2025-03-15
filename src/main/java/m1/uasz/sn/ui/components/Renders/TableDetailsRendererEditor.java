package m1.uasz.sn.ui.components.Renders;

import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.components.Action_Validation.ActionHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TableDetailsRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JPanel panel;
    private JLabel viewIcon, editIcon, deleteIcon;
    private JTable table;
    private int row;
    private ActionHandler actionHandler;
    private String callingComponent;
    private UtilisateurService utilisateurService = new UtilisateurService();

    public TableDetailsRendererEditor(String callingComponent, ActionHandler actionHandler) {
        this.callingComponent = callingComponent;
        this.actionHandler = actionHandler;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panel.setOpaque(false);

        deleteIcon = createIconLabel("/img/icons/8664938_trash_can_delete_remove_icon.png", "Supprimer");

        panel.add(deleteIcon);

        addListeners();
    }

    private void addListeners() {
        deleteIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (table != null) {
                    stopCellEditing();

                    // Vérification du rôle
                    if (!isAuthorized()) {
                        JOptionPane.showMessageDialog(null, "Vous n'êtes pas autorisé à supprimer cet élément.", "Accès refusé", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Object id = table.getValueAt(row, 0);
                    actionHandler.onDelete(id, callingComponent);
                }
            }
        });
    }

    // Méthode pour vérifier l'autorisation
    private boolean isAuthorized() {
        String userRole = utilisateurService.getUtilisateurConnecte().getRole();
        return "RESPONSABLE".equals(userRole) || "marks".equals(callingComponent);
    }

    private JLabel createIconLabel(String resourcePath, String tooltip) {
        JLabel label = new JLabel(getResizedIcon(resourcePath, 20, 20));
        label.setToolTipText(tooltip);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Effet hover
        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }

            public void mouseExited(MouseEvent e) {
                label.setBorder(null);
            }
        });

        return label;
    }

    private String[] champsForm() {
        return new String[]{"Type", "Nom", "Prénom", "Email", "Mot de Passe", "Confirmer Mot de Passe"};
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

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        stopCellEditing();
        this.row = row; // 🔥 Stocke la ligne actuelle
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        stopCellEditing();
        this.table = table;
        this.row = row; // 🔥 Stocke la ligne actuelle
        return panel;
    }

    @Override
    public boolean isCellEditable(java.util.EventObject e) {
        return true; // ✅ Autorise l'édition pour déclencher les événements des icônes
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
