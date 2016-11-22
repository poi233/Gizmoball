package model.gizmos;

import model.base.GizmoType;
import model.base.IBoardItem;

public interface IGizmo extends IBoardItem {
	
	GizmoType getType();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	int getOrientation();
	
	boolean canRotate();
}
