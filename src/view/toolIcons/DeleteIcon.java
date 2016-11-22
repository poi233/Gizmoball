package view.toolIcons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class DeleteIcon implements Icon {

	Color color;

	public DeleteIcon(Color c) {
		color = c;

	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 18;
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 17;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int pad = 5;
		g.setColor(Color.red);
		g.drawString(" Delete", 8, 17);
	}

}
