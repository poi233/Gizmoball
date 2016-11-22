package view.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import model.gizmos.IGizmo;

public class CircleBumperPainter implements IGizmoPainter {
	
	@Override
	public void paint(Graphics2D g, IGizmo gizmo) {
		
		Ellipse2D.Double circle = new Ellipse2D.Double(gizmo.getX(), gizmo.getY(), gizmo.getWidth(), gizmo.getHeight());

		Color colour;

		switch (gizmo.getType()) {
		case CircleBumper:
			colour = Color.lightGray;
			break;

		case AcceleratorGizmo:
			colour = Color.blue;
			break;

		case PortalGizmo:
			colour = new Color(139, 0, 244);
			break;

		// case MultiballGizmo:
		default:
			colour = Color.white;
			break;
		}

		g.setColor(colour);
		g.fill(circle);

		g.setColor(colour.darker());
		g.draw(circle);
	}
}
