package model.gizmoPhysics;

import model.gizmos.CircleBumper;
import model.basePhysics.Circle;

public class PhysicsCircleBumper extends PhysicsGizmo {
	
	/**
	 * Creates a basePhysics representation of a CircleBumper.
	 * 
	 * @param bumper - the CircleBumper to represent.
	 */
	public PhysicsCircleBumper(CircleBumper bumper) {
		objects.add(new Circle(0.5 + bumper.getX(), 0.5 + bumper.getY(), 0.5));
	}
}
