package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class LeftFlipperIcon implements Icon {

    Color color;
    int[] xc;
    int[] yc;

    public LeftFlipperIcon(Color c) {
        color = c;

    }

    @Override
    public int getIconHeight() {
        return 7;
    }

    @Override
    public int getIconWidth() {
        return 7;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.pink);
        int[] xc = {11, 24, 19, 6,};
        int[] yc = {6, 19, 24, 11};
        g.fillOval(5, 5, getIconWidth(), getIconHeight());
        g.fillPolygon(xc, yc, 4);
        g.fillOval(18, 18, getIconWidth(), getIconWidth());

    }

}
