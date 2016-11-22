package model.gizmos;

import model.base.Ball;
import model.base.Board;
import model.base.GizmoType;

public class MultiballGizmo extends CircleBumper {
	
	private Board board;

	/**
	 * Constructs a new MultiballGizmo
	 * 
	 * @param x - topleft x point.
	 * @param y - topleft y point.
	 * @param board - the painter it is being placed on.
	 */
	public MultiballGizmo(int x, int y, Board board) {
		super(x, y);
		this.board = board;
	}

	@Override
	public GizmoType getType() {
		return GizmoType.MultiballGizmo;
	}

	@Override
	public void doAction() {
		board.addBall(new Ball(x + 0.5, y + 1.5, 0.25, 1));
	}
}
