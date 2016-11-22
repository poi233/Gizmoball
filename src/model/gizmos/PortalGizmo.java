package model.gizmos;

import model.base.Ball;
import model.base.GizmoType;
import model.base.IBoardItem;

public class PortalGizmo extends CircleBumper {
	
	/**
	 * Constructs a PortalGizmo
	 * 
	 * @param x - topleft x point.
	 * @param y - topleft y point.
	 */
	public PortalGizmo(int x, int y) {
		super(x, y);
	}

	@Override
	public GizmoType getType() {
		return GizmoType.PortalGizmo;
	}

	@Override
	public void trigger(IBoardItem item) {
		if (item instanceof Ball) {
			Ball ball = (Ball) item;

			for (IBoardItem connectedItem : connectedItems) {
				if (connectedItem instanceof PortalGizmo) {
					PortalGizmo exit = (PortalGizmo) connectedItem;
					double newx = ball.getX() - x + exit.x;
					double newy = ball.getY() - y + exit.y;
					ball.move(newx, newy);
					return;
				}
			}
		}

		super.trigger(item);
	}
}
