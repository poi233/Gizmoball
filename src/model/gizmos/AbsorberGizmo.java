package model.gizmos;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import model.base.Ball;
import model.base.GizmoType;
import model.base.IBoardItem;

public class AbsorberGizmo extends Gizmo implements Observer {
	private Queue<Ball> balls;
	private Ball ejectingBall;

	/**
	 * Constructor for an absorber gizmo.
	 * 
	 * @param x1 - the topleft x point.
	 * @param y1 - the topleft y point.
	 * @param x2 - the width. 
	 * @param y2 - the height. 
	 */
	public AbsorberGizmo(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2 - x1, y2 - y1);

		balls = new LinkedList<Ball>();
		ejectingBall = null;
	}

	@Override
	public GizmoType getType() {
		return GizmoType.Absorber;
	}

	@Override
	/**
	 * Triggers the absorber
	 */
	public void trigger(IBoardItem item) {
		if (item instanceof Ball) {
			Ball ball = (Ball) item;

			if (ball != ejectingBall) {
				ball.move(this.x + this.width - 0.25, this.y + this.height
						- 0.25);
				ball.capture();

				if (!balls.contains(ball)) {
					balls.add(ball);
					ball.addObserver(this);
				}
			}
		}

		super.trigger(item);
	}

	@Override
	public void doAction() {
		if (ejectingBall == null && balls.isEmpty() == false) {
			ejectingBall = balls.remove();
			ejectingBall.setVelocity(0, -50);
			ejectingBall.move(this.x + this.width - 0.35, this.y - 0.3);
		}
	}

	@Override
	public void update(Observable source, Object arg) {
		if (source == ejectingBall) {
			if (ejectingBall.getY() < this.getY() - 0.35) {
				ejectingBall = null;
			}
		}
	}
	
	
	public Queue<Ball> getCapturedBalls()
	{
		return balls;
	}
}
