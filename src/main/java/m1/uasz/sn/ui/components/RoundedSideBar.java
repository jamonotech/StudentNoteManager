package m1.uasz.sn.ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundedSideBar extends JPanel {
    private int arcWidth;
    private int arcHeight;
    private boolean topLeft, topRight, bottomLeft, bottomRight;

    public RoundedSideBar(int arcWidth, int arcHeight, boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        setOpaque(false); // Permet de voir les bords arrondis
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Activer l'anti-aliasing pour des bords lisses
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Définir la couleur d'arrière-plan
        g2.setColor(getBackground());

        int width = getWidth();
        int height = getHeight();

        // Dessiner un rectangle avec des coins arrondis
        g2.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);

        // Effacer les coins non arrondis en dessinant des rectangles simples
        if (!topRight) {
            g2.fillRect(width - arcWidth, 0, arcWidth, arcHeight);
        }
        if (!bottomLeft) {
            g2.fillRect(0, height - arcHeight, arcWidth, arcHeight);
        }
        if (!bottomRight) {
            g2.fillRect(width - arcWidth, height - arcHeight, arcWidth, arcHeight);
        }
        if (!topLeft) { // Supprimer le coin supérieur gauche si nécessaire
            g2.fillRect(0, 0, arcWidth, arcHeight);
        }

        g2.dispose();
    }

}
