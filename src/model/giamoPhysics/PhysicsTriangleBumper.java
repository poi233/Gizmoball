package model.giamoPhysics;

import model.base.RotatablePoint;
import model.gizmos.TriangleBumper;
import model.basePhysics.Circle;
import model.basePhysics.LineSegment;

public class PhysicsTriangleBumper extends PhysicsGizmo {
	
	/**
	 * Creates a basePhysics representation of a triangle bumper.
	 * 
	 * @param bumper - the TriangleBumper to represent.
	 */
	public PhysicsTriangleBumper(TriangleBumper bumper) {
		double x = bumper.getX(), y = bumper.getY();
		int orientation = bumper.getOrientation();

		RotatablePoint centre = new RotatablePoint(x + 0.5, y + 0.5);
		RotatablePoint p1 = new RotatablePoint(x, y)
				.rotate(centre, orientation);
		RotatablePoint p2 = new RotatablePoint(x + 1, y).rotate(centre,
				orientation);
		RotatablePoint p3 = new RotatablePoint(x, y + 1).rotate(centre,
				orientation);

		objects.add(new LineSegment(p1.x, p1.y, p2.x, p2.y));
		objects.add(new LineSegment(p3.x, p3.y, p2.x, p2.y));
		objects.add(new LineSegment(p1.x, p1.y, p3.x, p3.y));

		objects.add(new Circle(p1.x, p1.y, 0));
		objects.add(new Circle(p2.x, p2.y, 0));
		objects.add(new Circle(p3.x, p3.y, 0));
	}
}