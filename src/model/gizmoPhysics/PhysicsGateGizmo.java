package model.gizmoPhysics;

import java.util.Observable;
import java.util.Observer;

import model.gizmos.GateGizmo;
import model.basePhysics.Circle;

public class PhysicsGateGizmo extends PhysicsGizmo implements Observer {
	
	private GateGizmo gate;

	/**
	 * Creates a basePhysics representation of a GateGizmo.
	 * 
	 * @param gate - the gate to represent.
	 */
	public PhysicsGateGizmo(GateGizmo gate) {
		this.gate = gate;
		gate.addObserver(this);
		update(null, null);
	}

	@Override
	public void update(Observable source, Object arg) {
		objects.clear();
		
		if (gate.getTriggeredState())
			objects.add(new Circle(0.5 + gate.getX(), 0.5 + gate.getY(), 0.5));
	}
}