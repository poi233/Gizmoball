package view.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import model.base.Ball;

public class BallPainter {
	
	public void paint(Graphics2D g, Ball ball) {
		
		double r = ball.getRadius();
		Ellipse2D.Double circle = new Ellipse2D.Double(ball.getX() - r,
				ball.getY() - r, r * 2, r * 2);

		g.setColor(Color.orange);
		g.fill(circle);

		g.setColor(Color.orange.darker());
		g.draw(circle);
	}
}
