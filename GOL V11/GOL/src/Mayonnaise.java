import java.awt.AWTException;		//imports:
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Set;

public class Mayonnaise implements KeyListener, MouseListener {

	Set<Integer> iHeld = new HashSet<>();
	Set<Integer> iReleased = new HashSet<>();
	Set<Integer> iNotToggled = new HashSet<>();			//hash-set for each button state
	public int newheight = Main.game.grid.height;
	public int newwidth = Main.game.grid.width;		
	public int previousstate = 1;
	boolean firstClick = true;
	boolean togglingFrom;
	public Point mousePos;

	boolean running = false;
	long lastUpdate = 0;
	int timeDelay = 8;

	public Mayonnaise(Frame frame) {
		frame.requestFocus();
		frame.addKeyListener(this);
		frame.addMouseListener(this);			//adds the key and mouse listener to the frame
	}

	public void tick() {
		Point mousePos = MouseInfo.getPointerInfo().getLocation();		//gets the pixel coordinate for the mouse cursor 
		switch (Game.STATE) {
		case 0:							//if in the menu
			if (iHeld.contains(KeyEvent.VK_DOWN) && !iNotToggled.contains(KeyEvent.VK_DOWN)) {
				Main.game.menu.option++;
			}
			if (iHeld.contains(KeyEvent.VK_UP) && !iNotToggled.contains(KeyEvent.VK_UP)) {		//cycle menu options
				Main.game.menu.option--;
			}
			if (Main.game.menu.option < 0) {
				Main.game.menu.option = Main.game.menu.options.size() - 1;
			}
			if (Main.game.menu.option == Main.game.menu.options.size()) {
				Main.game.menu.option = 0;											//loop menu to avoid errors & confusion
			}

			if (iHeld.contains(KeyEvent.VK_ENTER) && !iNotToggled.contains(KeyEvent.VK_ENTER)) {
				Main.game.menu.chooseOption();										//if the user selects an option
			}
			break;
		case 1:							
		case 4:							//if 'play' or 'import template' is selected
			
			

			if (iReleased.contains(KeyEvent.VK_R)
					|| iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.startB.contains(mousePos)) {
				running = !running;
			}																				//input handler for running state (grid running not game running)

			if (iReleased.contains(KeyEvent.VK_C)
					|| iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.clearB.contains(mousePos)) {
				Grid.grid = new boolean[Grid.width][Grid.height];
				running = false;
				Main.game.grid.Generations = 0;												//input handler for clearing the grid
			}

			if (iHeld.contains(KeyEvent.VK_P) && !iNotToggled.contains(KeyEvent.VK_P)
					|| iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.colour.contains(mousePos)) {
				Main.game.options.c++;														//input handler for cycling colours
			}

			if (iHeld.contains(KeyEvent.VK_DOWN) && !iNotToggled.contains(KeyEvent.VK_DOWN)) {

				Main.game.grid.moveMouseOnGrid(0, 1, mousePos);								
			}

			if (iHeld.contains(KeyEvent.VK_UP) && !iNotToggled.contains(KeyEvent.VK_UP)) {

				Main.game.grid.moveMouseOnGrid(0, -1, mousePos);
			}

			if (iHeld.contains(KeyEvent.VK_LEFT) && !iNotToggled.contains(KeyEvent.VK_LEFT)) {

				Main.game.grid.moveMouseOnGrid(-1, 0, mousePos);
			}

			if (iHeld.contains(KeyEvent.VK_RIGHT) && !iNotToggled.contains(KeyEvent.VK_RIGHT)) {

				Main.game.grid.moveMouseOnGrid(1, 0, mousePos);									//input handlers for moving individual squares on the grid (robot)
			}

			if (!running) {
				if (iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.importtemp.contains(mousePos)) {
				Main.game.grid.Generations = 0;
				Main.game.select.selected = true;
				Main.game.reader.getFile();
			}																							//input handler for the get import template button when on the grid
				if (iHeld.contains(MouseEvent.BUTTON1) && Main.game.grid.bounds.contains(mousePos)) {
					Point p = Main.game.grid.getGridSquareClicked(mousePos);
					if (firstClick) {
						firstClick = false;														//input handler for clicking a dragging on the grid
						togglingFrom = Grid.grid[p.x][p.y];
					}
					if (Grid.grid[p.x][p.y] == togglingFrom) {
						Main.game.grid.click(mousePos);											//registers which squares are clicked when dragged
					}
				}
				if (iReleased.contains(KeyEvent.VK_F)) {
					for (int x = 0; x < Main.game.grid.width; x++) {
						for (int y = 0; y < Main.game.grid.height; y++) {
							Grid.grid[x][y] = true;												//input hander for filling the grid
						}
					}
				}

				if (iHeld.contains(KeyEvent.VK_M) && !iNotToggled.contains(KeyEvent.VK_M)) {
					Main.game.select.selected = false;
					Game.STATE = 0;																//input handler for returning to the menu
				}

				if (iHeld.contains(KeyEvent.VK_SPACE) && Main.game.grid.bounds.contains(mousePos)) {
					Point p = Main.game.grid.getGridSquareClicked(mousePos);
					if (firstClick) {
						firstClick = false;
						togglingFrom = Grid.grid[p.x][p.y];
					}
					if (Grid.grid[p.x][p.y] == togglingFrom) {
						Main.game.grid.click(mousePos);											//input handler for dragging mouse and holding space (clicking 'dragged' squares)
					}

				}

			}
			if (iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.speedinc.contains(mousePos)) {
				timeDelay = timeDelay / 2;
				if (timeDelay < 2) {
					timeDelay = 1;																//in put handler for increasing the speed of the stepping 
				}
			}
			if (iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.speeddec.contains(mousePos)) {
				timeDelay = timeDelay * 2;
				if (timeDelay > 1000) {
					timeDelay = 1000;															//decreasing the speed of the stepping
				}
			}
			if (iHeld.contains(KeyEvent.VK_W) && !iNotToggled.contains(KeyEvent.VK_W)) {

				timeDelay = timeDelay / 2;
				if (timeDelay < 5) {															 
					timeDelay = 1;																
				}

			}

			if (iHeld.contains(KeyEvent.VK_S) && !iNotToggled.contains(KeyEvent.VK_S)) {

				timeDelay = timeDelay * 2;
				if (timeDelay > 1000) {
					timeDelay = 1000;														//keyboard controls for above
				}
			}

			if (running) {
				if (System.currentTimeMillis() - lastUpdate >= timeDelay) {
					Main.game.grid.update();
					lastUpdate = System.currentTimeMillis();								//returns the change in time between clicks for the run method in Game.class
				}
			} else if (iReleased.contains(KeyEvent.VK_ENTER)
					|| iReleased.contains(MouseEvent.BUTTON1) && Main.game.frame.stepB.contains(mousePos)) {
				Main.game.grid.update();													//input handler for a grid update (step)
			}
			if (iReleased.contains(MouseEvent.BUTTON1)) {
				firstClick = true;
			}
			if (iReleased.contains(KeyEvent.VK_SPACE)) {
				firstClick = true;															//establishes clicking and dragging start point
			}

			break;
		case 2:																				//displaying the rules

			if (iHeld.contains(KeyEvent.VK_M) && !iNotToggled.contains(KeyEvent.VK_M)) {
				Game.STATE = 0;
				Main.game.menu.option = 0;
			}																				//input handler for returning to menu

			break;
		case 5:																				//options screen

			if (iHeld.contains(KeyEvent.VK_M) && !iNotToggled.contains(KeyEvent.VK_M)) {
				Game.STATE = 0;																//input handler for returning to menu
				Main.game.menu.option = 0;
			}
			if (iHeld.contains(KeyEvent.VK_C) && !iNotToggled.contains(KeyEvent.VK_C)) {	//input handler for cycling colours
				Main.game.options.c++;

			}
			if (iReleased.contains(MouseEvent.BUTTON1) && Main.game.options.GenB.contains(mousePos)) {
				Options.changerules(null);
			}
			if (iReleased.contains(MouseEvent.BUTTON1)&& Main.game.options.ColB.contains(mousePos)){
				Main.game.options.c++;
			}
			

			break;
		default:
			System.err.println("INVALID GAME STATE");										//cannot find current game state
			System.exit(0);
			break;
		}
		iNotToggled.addAll(iHeld);
		iNotToggled.removeAll(iReleased);													//clears held buttons & keys
		iReleased.clear();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
															//default methods for getting key presses below:
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		iHeld.add(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		iHeld.remove(e.getButton());
		iReleased.add(e.getButton());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {					//input handler for quitting program
			System.exit(0);
		}
		iHeld.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		iHeld.remove(e.getKeyCode());
		iReleased.add(e.getKeyCode());										
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
