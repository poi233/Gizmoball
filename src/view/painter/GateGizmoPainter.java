package view.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import model.gizmos.IGizmo;

public class GateGizmoPainter implements IGizmoPainter {
	
	@Override
	public void paint(Graphics2D g, IGizmo gizmo) {
		Ellipse2D.Double circle = new Ellipse2D.Double(gizmo.getX(), gizmo.getY(), gizmo.getWidth(), gizmo.getHeight());

		if (gizmo.getTriggeredState()) {
			g.setColor(Color.lightGray);
			g.fill(circle);
		}

		g.setColor(Color.lightGray.darker());
		g.draw(circle);
	}

}
