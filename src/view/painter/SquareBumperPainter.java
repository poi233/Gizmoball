package view.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import model.gizmos.IGizmo;

public class SquareBumperPainter implements IGizmoPainter {
	
	@Override
	public void paint(Graphics2D g, IGizmo gizmo) {
		Rectangle2D.Double rect = new Rectangle2D.Double(gizmo.getX(), gizmo.getY(), gizmo.getWidth(), gizmo.getHeight());

		g.setColor(Color.lightGray);
		g.fill(rect);

		g.setColor(Color.lightGray.darker());
		g.draw(rect);
	}
}
