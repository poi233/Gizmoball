package model.fileSystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.base.*;
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
import model.gizmoPhysics.IPhysicsEngine;

/* SAVE FILE GRAMMAR

 <file> ::= <commandline>*

 <commandline> ::= <command>"\n" | "\n"

 <command> ::= <gizmoOp> <name> <int-pair> |
 Absorber <name> <int-pair> <int-pair> |
 Ball <name> <float-pair> <float-pair> |
 Rotate <name> |
 Delete <name> |
 Move <name> <number-pair> |
 Connect <name> <name> |
 KeyConnect <keyid> <name> |
 Gravity FLOAT |
 Friction FLOAT FLOAT

 <name> ::= IDENTIFIER

 <gizmoOp> ::= Square | Circle | Triangle | RightFlipper | LeftFlipper

 <number-pair> ::= <int-pair> | <float-pair>

 <int-pair> ::= INTEGER INTEGER

 <float-pair> ::= FLOAT FLOAT

 <keyid> ::= "key" KEYNUM "down" |
 "key" KEYNUM "up"

 IDENTIFIER    represents any string composed only from the characters {'0'..'9','A'..'Z','a..z','_'}. The identifier "OuterWalls" is a special reserved word which refers to the outer walls; no other item may use this identifier.    

 INTEGER        represents any integer number    
 FLOAT          represents any floating point number    
 KEYNUM         represents any numeric key identifier (which are integers)

 */
public class Loader 
{
	public static final double DEFAULT_BALL_RADIUS = 0.25, DEFAULT_BALL_MASS = 1.0;
	
	private Pattern gizCommand;
	private Pattern absCommand;
	private Pattern ballCommand;
	private Pattern rotCommand;
	private Pattern delCommand;
	private Pattern movIntCommand;
	private Pattern movFloatCommand;
	private Pattern conCommand;
	private Pattern keyconCommand;
	private Pattern gravCommand;
	private Pattern fricCommand;
	private BufferedReader fileInput;
	private Map<String, IBoardItem> boardItemMap;
	private Map<Integer, List<IBoardItem>> keyupTriggers;
	private Map<Integer, List<IBoardItem>> keydownTriggers;
	private Board board;
	
	
	public Loader(String fileName, Board board) throws FileNotFoundException {
		this.board = board;
		
		String nameRegex = "([0-9A-Za-z_]+)";
		String intpairRegex = "(\\d+) (\\d+)";
		String floatRegex = "(\\d*\\.\\d+)";
		String floatpairRegex = floatRegex + " " + floatRegex;
		gizCommand = Pattern.compile("(Square|Circle|Triangle|RightFlipper|LeftFlipper|Accelerator|Portal|Multiball|Gate|Spinner) " + nameRegex + " " + intpairRegex);
		absCommand = Pattern.compile("Absorber " + nameRegex + " " + intpairRegex + " " + intpairRegex);
		ballCommand = Pattern.compile("Ball " + nameRegex + " " + floatpairRegex + " " + floatpairRegex);
		rotCommand = Pattern.compile("Rotate " + nameRegex);
		delCommand = Pattern.compile("Delete " + nameRegex);
		movIntCommand = Pattern.compile("Move " + nameRegex + " " + intpairRegex);
		movFloatCommand = Pattern.compile("Move " + nameRegex + " " + floatpairRegex);
		conCommand = Pattern.compile("Connect " + nameRegex + " " + nameRegex);
		keyconCommand = Pattern.compile("KeyConnect key (\\d+) (up|down) " + nameRegex);
		gravCommand = Pattern.compile("Gravity " + floatRegex);
		fricCommand = Pattern.compile("Friction " + floatpairRegex);
		fileInput = new BufferedReader(new FileReader(fileName));
		boardItemMap = new HashMap<String, IBoardItem>();
		
		keyupTriggers = new HashMap<Integer, List<IBoardItem>>();
		keydownTriggers = new HashMap<Integer, List<IBoardItem>>();
	}

	
	public void load(IPhysicsEngine engine) throws BadFileException, IOException {
		
		String line, gizop, name, name2, dir;
		int x, y, x1, x2, y1, y2, key;
		double xd, yd, vx, vy, g, f1, f2;
		Matcher matcher;
		
		while ((line = fileInput.readLine()) != null) {

			if (line.isEmpty()) {
				continue;
			}
			
			matcher = gizCommand.matcher(line);
			if (matcher.matches()) {
				gizop = matcher.group(1);
				name = matcher.group(2);
				ensureUniqueName(name);
				x = Integer.parseInt(matcher.group(3));
				y = Integer.parseInt(matcher.group(4));
				if (gizop.equals("Square")) {
					boardItemMap.put(name, new SquareBumper(x, y));
				} else if (gizop.equals("Circle")) {
					boardItemMap.put(name, new CircleBumper(x, y));
				} else if (gizop.equals("Triangle")) {
					boardItemMap.put(name, new TriangleBumper(x, y, 0));
				} else if (gizop.equals("RightFlipper")) {
					boardItemMap.put(name, new RightFlipper(x, y));
				} else if (gizop.equals("LeftFlipper")) {
					boardItemMap.put(name, new LeftFlipper(x, y));
				} else if (gizop.equals("Accelerator")) {
					boardItemMap.put(name, new AcceleratorGizmo(x, y));
				} else if (gizop.equals("Portal")) {
					boardItemMap.put(name, new PortalGizmo(x, y));
				} else if (gizop.equals("Multiball")) {
					boardItemMap.put(name, new MultiballGizmo(x, y, board));
				} else if (gizop.equals("Gate")) {
					boardItemMap.put(name, new GateGizmo(x, y));
				} else if (gizop.equals("Spinner")) {
					boardItemMap.put(name, new SpinnerGizmo(x, y));
				}
				continue;
			}
			
			matcher = absCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureUniqueName(name);
				x1 = Integer.valueOf(matcher.group(2));
				y1 = Integer.valueOf(matcher.group(3));
				x2 = Integer.valueOf(matcher.group(4));
				y2 = Integer.valueOf(matcher.group(5));
				boardItemMap.put(name, new AbsorberGizmo(x1, y1, x2, y2));
				continue;
			}
			
			matcher = ballCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(0);
				ensureUniqueName(name);
				xd = Double.valueOf(matcher.group(2));
				yd = Double.valueOf(matcher.group(3));
				vx = Double.valueOf(matcher.group(4));
				vy = Double.valueOf(matcher.group(5));
				boardItemMap.put(name, new Ball(xd, yd, DEFAULT_BALL_RADIUS, DEFAULT_BALL_MASS, vx, vy));
				continue;
			}
			
			matcher = rotCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureNameExists(name);
				boardItemMap.get(name).rotate();
				continue;
			}
			
			matcher = delCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureNameExists(name);
				boardItemMap.remove(name);
				continue;
			}
			
			matcher = movIntCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureNameExists(name);
				x = Integer.parseInt(matcher.group(2));
				y = Integer.parseInt(matcher.group(3));
				boardItemMap.get(name).move(x, y);
				continue;
			}
			
			matcher = movFloatCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureNameExists(name);
				xd = Double.parseDouble(matcher.group(2));
				yd = Double.parseDouble(matcher.group(3));
				boardItemMap.get(name).move(xd, yd);
				continue;
			}
			
			matcher = conCommand.matcher(line);
			if (matcher.matches()) {
				name = matcher.group(1);
				ensureNameExists(name);
				name2 = matcher.group(2);
				ensureNameExists(name2);
				boardItemMap.get(name).connect(boardItemMap.get(name2));
				continue;
			}
			
			matcher = keyconCommand.matcher(line);
			if (matcher.matches()) {
				key = Integer.valueOf(matcher.group(1));
				dir = matcher.group(2);
				name = matcher.group(3);
				ensureNameExists(name);
				
				if (dir.equals("up"))
				{
					addTrigger(keyupTriggers, key, name);
				}
				else
				{
					addTrigger(keydownTriggers, key, name);
				}
				
				continue;
			}
			
			matcher = gravCommand.matcher(line);
			if (matcher.matches()) {
				g = Double.valueOf(matcher.group(1));
				engine.setGravity(g);
				continue;
			}
			
			matcher = fricCommand.matcher(line);
			if (matcher.matches()) {
				f1 = Double.parseDouble(matcher.group(1));
				f2 = Double.parseDouble(matcher.group(2));
				engine.setFriction(f1, f2);
				continue;
			}
			
			throw new BadFileException("invalid command " + line);
		}
		
		//load the items into the painter
		for (IBoardItem item : boardItemMap.values())
		{
			if (item instanceof Ball)
			{
				board.addBall((Ball)item);
			}
			else
			{
				board.addGizmo((IGizmo)item);
			}
		}
	}
	
	
	public Map<Integer, List<IBoardItem>> getKeyUpTriggers() {
		return keyupTriggers;
	}
	
	
	public Map<Integer, List<IBoardItem>> getKeyDownTriggers() {
		return keydownTriggers;
	}
	

	private void ensureUniqueName(String name) throws BadFileException {
		if (boardItemMap.containsKey(name)) {
			throw new BadFileException(name + " name is not unquie");
		}
	}

	private void ensureNameExists(String name) throws BadFileException {
		// TODO put reference to OuterWalls in item map
		if (name.equals("OuterWalls") || boardItemMap.containsKey(name)) {
			return;
		}
		throw new BadFileException(name + " name does not exist yet");
	}
	
	private void addTrigger(Map<Integer, List<IBoardItem>> triggers, int key, String name){
		if (triggers.containsKey(key)){
			triggers.get(key).add(boardItemMap.get(name));
		}else {
			List<IBoardItem> items = new ArrayList<IBoardItem>();
			items.add(boardItemMap.get(name));
			triggers.put(key, items);
		}
	}
}