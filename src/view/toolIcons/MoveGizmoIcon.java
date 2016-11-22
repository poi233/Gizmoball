package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class MoveGizmoIcon implements Icon {
	Color color;

	public MoveGizmoIcon(Color c) {
		color = c;
	}

	public int getIconWidth() {
		return 60;
	}

	public int getIconHeight() {
		return 20;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);

		g.drawString("Move", 16, 17);
	}

}
