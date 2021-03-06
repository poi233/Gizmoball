package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class RotateGizmoIcon implements Icon {

	Color color;

	public RotateGizmoIcon(Color c) {
		color = c;

	}

	@Override
	public int getIconHeight() {
		return 18;
	}

	@Override
	public int getIconWidth() {
		return 17;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.black);
		g.drawString(" Rotate", 8, 17);
	}

}
