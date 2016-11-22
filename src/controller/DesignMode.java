package controller;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import model.base.Ball;
import model.base.Board;
import model.base.IBoardItem;
import model.gizmos.AbsorberGizmo;
import model.gizmos.AcceleratorGizmo;
import model.gizmos.CircleBumper;
import model.gizmos.GateGizmo;
import model.gizmos.IGizmo;
import model.gizmos.LeftFlipper;
import model.gizmos.MultiballGizmo;
import model.gizmos.PortalGizmo;
import model.gizmos.RightFlipper;
import model.gizmos.SpinnerGizmo;
import model.gizmos.SquareBumper;
import model.gizmos.TriangleBumper;
import controller.RunMode.UpdateReason;

public class DesignMode extends Observable {
	
	private Board board;
	private DesignCommand currentCommand;
	private Rectangle positionBox;
	private boolean positionValid, variableSize, selecting;
	private int startX, startY;
	private IGizmo selectedGizmo;
	private TriggerHandler triggerHandler;
	private String statusMessage;

	//显示当前需要执行的操作
	public enum DesignCommand {
		None,
		AddCircleBumper("点击添加圆球.", true),
		AddSquareBumper("点击添加方块.", true),
		AddTriangleBumper("点击添加三角.", true),
		AddLeftFlipper("点击放置做挡板.", 2, 2),
		AddRightFlipper("点击添加右挡板.", 2, 2),
		AddAbsorber("点击添加吸收器", true),
		AddBall("点击添加小球", true),
		DeleteGizmo("删除", false),
		RotateGizmo("旋转90度", false),
		MoveGizmo("移动", false),
		ConnectKeyUp("点击添加按下按键事件", false),
		ConnectKeyDown("点击添加放开按键事件", false),
		ConnectGizmo("点击添加事件", false),
		AddAcceleratorGizmo("点击添加加速器", true),
		AddPortalGizmo("点击添加传送门", true),
		AddMultiballGizmo("点击添加小球发射器", true),
		AddGateGizmo("点击添加可变门", true),
		AddSpinnerGizmo("点击添加旋转板", 2, 2);
		
		private DesignCommand() {
			
			this.statusMessage = "";
			this.addGizmoCommand = false;
			this.positionBox = null;
		}
		
		private DesignCommand(String statusMessage, boolean addGizmoCommand){
			
			this.statusMessage = statusMessage;
			this.addGizmoCommand = addGizmoCommand;
			this.positionBox = new Rectangle(0, 0, 1, 1);
		}
		
		private DesignCommand(String statusMessage, int positionBoxWidth, int positionBoxHeight) {

			this.statusMessage = statusMessage;
			this.addGizmoCommand = true;
			this.positionBox = new Rectangle(0, 0, positionBoxWidth, positionBoxHeight);
		}
		
		public String statusMessage;
		public boolean addGizmoCommand;
		public Rectangle positionBox; 
	}

	public DesignMode(Board board, TriggerHandler triggerHandler) {
		
		this.board = board;
		this.triggerHandler = triggerHandler;
		currentCommand = DesignCommand.None;
	}

	//获取当前命令
	public DesignCommand getCurrentCommand() {
		
		return currentCommand;
	}

	//设置当前命令并提供给observer
	public void setCurrentCommand(DesignCommand value) {
		
		currentCommand = value;
		positionBox = currentCommand.positionBox;		
		setStatusMessage(currentCommand.statusMessage);
		selectedGizmo = null;
		
		this.setChanged();
		this.notifyObservers(UpdateReason.SelectedToolChanged);
	}

	//初始化鼠标起始位置
	public void beginSelectAt(int x, int y) {
		
		if (positionValid == false)
			return;
		
		switch (currentCommand) {
		
			case AddAbsorber:
				positionBox.setLocation(x, y);
				variableSize = true;
				break;
				
			case MoveGizmo:
				selectedGizmo = board.getGizmoAt(x, y);
				
				if (selectedGizmo != null) {
					positionBox = new Rectangle(selectedGizmo.getX(), selectedGizmo.getY(), selectedGizmo.getWidth(), selectedGizmo.getHeight() );
				}
				break;
		}
		
		startX = x;
		startY = y;
		selecting = true;
	}

	//鼠标终止位置
	public void endSelectAt(int x, int y) {
		
		selecting = false;
		
		if (positionValid == false)
			return;
		
		switch (currentCommand) {
		
			case AddAbsorber:
				board.addGizmo(new AbsorberGizmo(positionBox.x, positionBox.y, 
						positionBox.x + positionBox.width, positionBox.y + positionBox.height));
				
				positionBox.setLocation(x, y);
				positionBox.setSize(1, 1);
				variableSize = false;
				positionValid = false;
				break;
				
			case AddCircleBumper:
				board.addGizmo(new CircleBumper(x, y));
				positionValid = false;
				break;
				
			case AddSquareBumper:
				board.addGizmo(new SquareBumper(x, y));
				positionValid = false;
				break;
				
			case AddTriangleBumper:
				board.addGizmo(new TriangleBumper(x, y, 0));
				positionValid = false;
				break;
				
			case AddLeftFlipper:
				board.addGizmo(new LeftFlipper(x, y));
				positionValid = false;
				break;
				
			case AddRightFlipper:
				board.addGizmo(new RightFlipper(x, y));
				positionValid = false;
				break;
				
			case AddAcceleratorGizmo:
				board.addGizmo(new AcceleratorGizmo(x, y));
				positionValid = false;
				break;
				
			case AddPortalGizmo:
				board.addGizmo(new PortalGizmo(x, y));
				positionValid = false;
				break;
				
			case AddMultiballGizmo:
				board.addGizmo(new MultiballGizmo(x, y, board));
				positionValid = false;
				break;
				
			case AddGateGizmo:
				board.addGizmo(new GateGizmo(x, y));
				positionValid = false;
				break;
				
			case AddSpinnerGizmo:
				board.addGizmo(new SpinnerGizmo(x, y));
				positionValid = false;
				break;
				
			case AddBall:
				board.addBall(new Ball(x + 0.5, y + 0.5, 0.25, 1));
				positionValid = false;
				break;
				
			case MoveGizmo:
				if (selectedGizmo != null) {
					selectedGizmo.move(x, y);
					selectedGizmo = null;
				}
				break;
				
			case RotateGizmo:
				selectedGizmo = board.getGizmoAt(x, y);
				
				if (selectedGizmo != null) {
					if (selectedGizmo.canRotate())
						selectedGizmo.rotate();
					
					selectedGizmo = null;
				}
				break;
				
			case DeleteGizmo:
				Ball ball = board.getBallAt(x, y);
				
				if (ball != null) {
					board.getBalls().remove(ball);
				}else {
					
					selectedGizmo = board.getGizmoAt(x, y);
					
					if (selectedGizmo != null) {
						board.getGizmos().remove(selectedGizmo);
						
						//remove trigger references
						removeGizmoFromCollectionOfLists(triggerHandler.getLinksDown().values(), selectedGizmo);
						removeGizmoFromCollectionOfLists(triggerHandler.getLinksUp().values(), selectedGizmo);
						
						for (IGizmo gizmo: board.getGizmos())
						{
							removeGizmoFromList(gizmo.getConnectedItems(), selectedGizmo);
						}
						
						selectedGizmo = null;
						positionValid = false;
					}
				}
				break;
				
			case ConnectKeyDown:
			case ConnectKeyUp:
				selectedGizmo = board.getGizmoAt(x, y);
				
				if (selectedGizmo != null) {
					setStatusMessage("请按下按键");
				}
				break;
				
			case ConnectGizmo:
				if (selectedGizmo != null) {
					IGizmo targetGizmo = board.getGizmoAt(x, y);
					
					if (targetGizmo != null) {
						selectedGizmo.connect(targetGizmo);
						setStatusMessage("已链接");
						selectedGizmo = null;
					}
				}else {
					selectedGizmo = board.getGizmoAt(x, y);
					
					if (selectedGizmo != null) {
						setStatusMessage("选择目标");
					}
				}
				break;
				
			default:
				return;
		}
		
		this.setChanged();
		this.notifyObservers(UpdateReason.BoardChanged);
	}
	
	
	private void removeGizmoFromList(List<IBoardItem> items, IGizmo gizmo)
	{
		//list.remove only removes first occurence
		//using iterator to avoid ConcurrentModificationException
		Iterator<IBoardItem> iterator = items.iterator();
		
		while (iterator.hasNext())
		{
			IBoardItem item = iterator.next();
			
			if (item == gizmo)
				iterator.remove();
		}
	}
	
	private void removeGizmoFromCollectionOfLists(Collection<List<IBoardItem>> collection, IGizmo gizmo)
	{
		for (List<IBoardItem> list: collection)
			removeGizmoFromList(list, gizmo);
	}


	//高亮鼠标所在格子四周
	public void moveTo(int x, int y) {

		if (positionBox != null) {
			if (currentCommand.addGizmoCommand) {
				if (variableSize == false) {
					//move the box to the new position
					positionBox.setLocation(x, y);
				}else {
					//resize the box from the start to the new position
					positionBox.setLocation(Math.min(x, startX), Math.min(y, startY));
					positionBox.setSize(Math.abs(x - startX) + 1, Math.abs(y - startY) + 1);
				}
				
				positionValid = validLocation();
			}else {
				if (selecting) {
					positionBox.setLocation(x, y);
					positionValid = validLocation();
				}else {
					IGizmo gizmo = board.getGizmoAt(x, y);
					
					if (gizmo != null) {
						positionBox.setLocation(gizmo.getX(), gizmo.getY());
						positionBox.setSize(gizmo.getWidth(), gizmo.getHeight());
						positionValid = true;
					}else {
						Ball ball = board.getBallAt(x, y);
						
						if (ball != null && currentCommand == DesignCommand.DeleteGizmo) {
							positionBox.setLocation(x, y);
							positionBox.setSize(1, 1);
							positionValid = true;
						}else {
							positionBox.setLocation(x, y);
							positionBox.setSize(1, 1);
							positionValid = false;
						}
					}
				}
			}
			
			this.setChanged();
			this.notifyObservers(UpdateReason.BoardChanged);
		}
	}
	
	
	public Rectangle getPositionBox() {
		
		return positionBox;
	}
	
	
	public boolean getPositionValid() {
		
		return positionValid;
	}
	
	
	public String getStatusMessage() {
		
		return statusMessage;
	}
	
	protected void setStatusMessage(String message) {
		
		this.statusMessage = message;
		this.setChanged();
		this.notifyObservers(UpdateReason.StatusChanged);
	}

	//注册按键事件
	public void keyPressed(int keycode) {
		
		switch (currentCommand) {
		
			case ConnectKeyDown:
				if (selectedGizmo != null){
					triggerHandler.addLinkDown(keycode, selectedGizmo);
					setStatusMessage("已链接");
					selectedGizmo = null;
				}
				
				break;
				
			case ConnectKeyUp:
				if (selectedGizmo != null) {
					triggerHandler.addLinkUp(keycode, selectedGizmo);
					setStatusMessage("已链接");
					selectedGizmo = null;
				}
				
				break;
		}
		
		setStatusMessage("按键已链接");
		selectedGizmo = null;
	}


	//判断鼠标所在位置再进行放置是否合法
	private boolean validLocation() {
		int x = positionBox.x;
		int y = positionBox.y;
		int w = positionBox.width;
		int h = positionBox.height;
		
		if (x + w > board.getWidth() || y + h > board.getHeight()) {
			return false;
		}
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int xx = x + i;
				int yy = y + j;
				IGizmo bMG = board.getGizmoAt(xx, yy);
				Ball bMB = board.getBallAt(xx, yy);
				if (bMG != null && !bMG.equals(selectedGizmo)) {
					return false;
				} else if (bMB != null) {
					return false;
				}
			}
		}
		
		return true;
	}
}
