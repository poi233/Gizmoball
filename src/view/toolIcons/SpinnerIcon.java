package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class SpinnerIcon implements Icon {

	Color color;
	int[] xc;
	int[] yc;

	public SpinnerIcon(Color c) {
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
		int[] xc = { 9, 22, 22, 9, };
		int[] yc = { 12, 12, 19, 19 };
		g.fillOval(5, 12, getIconWidth(), getIconHeight());
		g.fillPolygon(xc, yc, 4);
		g.fillOval(18, 12, getIconWidth(), getIconWidth());

	}

}
