package model.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.gizmos.IGizmo;

public class Board extends Observable {
	
	private List<IGizmo> gizmos;
	private List<Ball> balls;
	private int width, height;
	private boolean runMode;

	/**
	 * Constructs a new painter.
	 * 
	 * @param width - the width of the painter.
	 * @param height - the height of the painter.
	 */
	public Board(int width, int height) {
		gizmos = new ArrayList<IGizmo>();
		balls = new ArrayList<Ball>();
		runMode = false;

		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<IGizmo> getGizmos() {
		return gizmos;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void addGizmo(IGizmo gizmo) {
		gizmos.add(gizmo);
		this.setChanged();
		this.notifyObservers(gizmo);
	}

	public void removeGizmo(IGizmo gizmo) {
		gizmos.remove(gizmo);
		this.setChanged();
		this.notifyObservers(gizmo);
	}

	public void addBall(Ball ball) {
		balls.add(ball);
		this.setChanged();
		this.notifyObservers(ball);
	}

	public void removeBall(Ball ball) {
		balls.remove(ball);
		this.setChanged();
		this.notifyObservers();
	}

	public IGizmo getGizmoAt(int x, int y) {
		for (IGizmo gizmo : gizmos) {
			if (x >= gizmo.getX() && x < gizmo.getX() + gizmo.getWidth()
					&& y >= gizmo.getY()
					&& y < gizmo.getY() + gizmo.getHeight()) {
				return gizmo;
			}
		}

		return null;
	}

	public Ball getBallAt(int x, int y) {

		for (Ball ball : balls) {
			if ((ball.getX() >= x && ball.getX() < x + 1)
					&& (ball.getY() >= y && ball.getY() < y + 1)) {
				return ball;
			}
		}

		return null;
	}

	public void runMode() {
		runMode = !runMode;
		this.setChanged();
		this.notifyObservers(runMode);
	}
}
