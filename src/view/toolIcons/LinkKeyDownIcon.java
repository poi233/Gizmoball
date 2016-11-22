package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class LinkKeyDownIcon implements Icon {

	private Color color;

	public LinkKeyDownIcon(Color c) {
		color = c;
	}

	@Override
	public int getIconHeight() {
		return 13;
	}

	@Override
	public int getIconWidth() {
		return 7;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);

		g.setColor(color);
		g.drawString(" Key Down", 8, 17);

	}

}
