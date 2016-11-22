package model.gizmoPhysics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import model.base.*;
import model.gizmos.AbsorberGizmo;
import model.gizmos.CircleBumper;
import model.gizmos.Flipper;
import model.gizmos.GateGizmo;
import model.gizmos.IGizmo;
import model.gizmos.ISpinningGizmo;
import model.gizmos.OuterWallsGizmo;
import model.gizmos.SpinnerGizmo;
import model.gizmos.SquareBumper;
import model.gizmos.TriangleBumper;

public class PhysicsEngine implements IPhysicsEngine, Observer {
	
	public static double DEFAULT_MU = 0.025, DEFAULT_MU2 = 0.025,
			DEFAULT_GRAVITY = 25;

	public Board map;
	public List<PhysicsBall> balls;
	private List<ISpinningGizmo> spinningGizmos;
	public Map<IGizmo, PhysicsGizmo> objects;
	private double mintime;
	private PhysicsBall collidingBall;
	private IPhysicsObject collidingObject;
	private IBoardItem collidingBoardItem;
	private double mu, mu2, gravity;
	
	/**
	 * Contrusts a new PhysicsEngine.
	 */
	public PhysicsEngine() {
		balls = new ArrayList<PhysicsBall>();
		objects = new HashMap<IGizmo, PhysicsGizmo>();
		spinningGizmos = new ArrayList<ISpinningGizmo>();

		mu = DEFAULT_MU;
		mu2 = DEFAULT_MU2;
		gravity = DEFAULT_GRAVITY;
	}

	@Override
	/**
	 * Initialises the basePhysics engine with the state of the game
	 * grid. i.e. lets it know where all the gizmos are. 
	 * 
	 * @param map - the map of gizmos. 
	 */
	public void initialise(Board map) {
		this.map = map;

		map.addObserver(this);

		objects.clear();
		balls.clear();
		spinningGizmos.clear();

		objects.put(new OuterWallsGizmo(map.getWidth(), map.getHeight()),
				new OuterWalls(map));

		for (IGizmo gizmo : map.getGizmos()) {
			objects.put(gizmo, modelGizmo(gizmo));

			if (gizmo instanceof ISpinningGizmo) {
				spinningGizmos.add((ISpinningGizmo)gizmo);
			}
		}

		for (Ball ball : map.getBalls()) {
			balls.add(new PhysicsBall(ball));
		}
	}

	@Override
	/**
	 * Calculates the current state of the game, based on the pased in
	 * timedelta. 
	 */
	public void calculateState(double timedelta) {
		// put an arbitrary cap on recursion
		for (int i = 0; i < 100; i++) {
			calculateTimeUntilNextCollision();

			if (mintime < timedelta) {
				// we have a collision in this time step
				// do the reflection and move the balls
				moveBalls(mintime);
				moveSpinningGizmos(mintime);

				collidingBall.reflect(collidingObject);
				collidingBall.getBall().trigger(collidingBoardItem);
				collidingBoardItem.trigger(collidingBall.getBall());

				// continue for the rest of the time step
				timedelta -= mintime;
			} else {
				moveBalls(timedelta);
				moveSpinningGizmos(timedelta);
				return;
			}
		}

		// reached the recursion limit:
		// pretend there's not a reflection, not ideal, but better than crashing
		moveBalls(timedelta);
		moveSpinningGizmos(timedelta);
	}

	private void calculateTimeUntilNextCollision() {
		mintime = Double.POSITIVE_INFINITY;
		collidingObject = null;

		for (int i = 0; i < balls.size(); i++) {
			PhysicsBall ball = balls.get(i);

			// collide against all gizmo objects in the scene
			for (Map.Entry<IGizmo, PhysicsGizmo> entry : objects.entrySet()) {
				for (IPhysicsObject object : entry.getValue()
						.getPhysicsObjects()) {
					collide(ball, object, entry.getKey());
				}
			}

			// collide against the other balls
			for (int u = i; u < balls.size(); u++) {
				collide(ball, balls.get(u), balls.get(u).getBall());
			}
		}
	}

	private void collide(PhysicsBall ball, IPhysicsObject object,
			IBoardItem item) {
		double time = ball.timeUntilCollision(object);

		if (time < mintime && !ball.getBall().getIsCaptured())
		{
			mintime = time;
			collidingObject = object;
			collidingBall = ball;
			collidingBoardItem = item;
		}
	}

	private void moveBalls(double timedelta) {
		for (PhysicsBall ball : balls) {
			ball.applyFrictionAndGravity(timedelta / 2, gravity, mu, mu2);
			ball.move(timedelta);
			ball.applyFrictionAndGravity(timedelta / 2, gravity, mu, mu2);
		}
	}

	private void moveSpinningGizmos(double timedelta) {
		for (ISpinningGizmo gizmo : spinningGizmos) {
			if (gizmo.getAngularMomentum() != 0)
				gizmo.setAngle(gizmo.getAngle()
						+ gizmo.getAngularMomentum() * timedelta);
		}
	}

	private PhysicsGizmo modelGizmo(IGizmo gizmo) {
		switch (gizmo.getType()) {
		case CircleBumper:
		case AcceleratorGizmo:
		case PortalGizmo:
		case MultiballGizmo:
			return new PhysicsCircleBumper((CircleBumper) gizmo);

		case SquareBumper:
			return new PhysicsSquareBumper((SquareBumper) gizmo);

		case TriangleBumper:
			return new PhysicsTriangleBumper((TriangleBumper) gizmo);

		case Flipper:
			return new PhysicsFlipper((Flipper) gizmo);

		case Absorber:
			return new PhysicsAbsorberGizmo((AbsorberGizmo) gizmo);

		case GateGizmo:
			return new PhysicsGateGizmo((GateGizmo) gizmo);
			
		case SpinnerGizmo:
			return new PhysicsSpinnerGizmo((SpinnerGizmo) gizmo);
			
		default:
			throw new UnsupportedOperationException(String.format(
					"Could not model gizmo: %s", gizmo.getType()));
		}
	}

	@Override
	public void update(Observable source, Object arg) {
		if (arg instanceof Ball) {
			balls.add(new PhysicsBall((Ball) arg));
		}
	}

	@Override
	public void setFriction(double mu, double mu2) {
		this.mu = mu;
		this.mu2 = mu2;
	}

	@Override
	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	@Override
	public double getGravity() {
		return gravity;
	}

	@Override
	public double getFriction1() {
		return mu;
	}

	@Override
	public double getFriction2() {
		return mu2;
	}
}
