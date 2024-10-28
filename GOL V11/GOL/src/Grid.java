import java.awt.AWTException;		//imports:
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;			

public class Grid {
	public int Generations = 0;
	public int Population = 0;		//default displayed values are set to 0
	public int minNeighbours = 2;
	public int maxNeighbours = 3;		//variables for neighbour checks
	public int deltaPop = 0;
	public boolean Stable = false;		//default stable indicator is false for initial render
	public int nextPop = 0;
	public static int height;
	public static int width;
	public static boolean grid[][];
	public static boolean nextGrid[][];
	public static boolean imgrid[][];			//establishes the grids for running a plain and imported grid
	public int squareSize = 10;
	int x;
	int y;
	int a;
	public Rectangle bounds;		//bounds used for menu text

	public Grid() {
		crid();		//runs the default method by drawing an empty grid
	}

	public void crid() {
		height = (Frame.HEIGHT / squareSize) - (squareSize + 1);
		width = height;
		grid = new boolean[width][height];
		nextGrid = new boolean[width][height];
		x = (Frame.WIDTH / 2) - (width * squareSize / 2);
		y = (Frame.HEIGHT / 2) - (height * squareSize / 2);
		;
		bounds = new Rectangle(x, y, width * squareSize, height * squareSize);			//establishes a 2D array using a select square size, and defines the bounds for the grid
	}

	public void cridFromImage() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[x][y] = Main.game.grid.grid[x][y]; 		//takes the 2D boolean array mapped from an imported image and applies it to a clean grid of determined size (previous method)
			}
		}
		imgrid = new boolean[width][height];
		nextGrid = new boolean[width][height];
		squareSize = Frame.HEIGHT / (height);
		x = (Frame.WIDTH - squareSize * width) / 2;
		y = (Frame.HEIGHT - squareSize * height) / 2;
		bounds = new Rectangle(x, y, width * squareSize, height * squareSize);
		Main.game.STATE = 4;								//maps the 2D array using the same method
	}

	public void update() {
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int neighbours = getNeighbours(x, y);
				nextGrid[x][y] = (minNeighbours == neighbours && grid[x][y] || neighbours == maxNeighbours); 
							//plots a second 2D array where for each element, a neighbour check is carried out and if a cell has the correct amount of neighbours, it remains
			}
	
		}
		System.out.println("Min: " + minNeighbours + " Max: " + maxNeighbours);
		grid = nextGrid;
		nextGrid = new boolean[width][height];
		Generations++;
		Population = getPop();
		deltaPop = java.lang.Math.abs(Population - nextPop);
		nextPop = Population;			//calculates the change in population
		if (Generations > 1) {
			System.out.println("Population = " + Population);
			System.out.println("deltaPop = " + deltaPop);
			System.out.println("Percentage change = " + ((double) (deltaPop) / Population) * 100);     //...if i want to display population data to the console
			if (((double) (deltaPop) / Population) * 100 <= 10) {
				Stable = true;
			}
			else {
				Stable = false;
			}								//checks whether the change in population is greater than 10% of the global population, and if so the formation is unstable

		}
	}

	public boolean[][] importImage(boolean[][] a) {
		Main.game.grid.grid = new boolean[width][height];
		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < a.length; y++) {
				int newY = y + ((height / 2) - (Main.game.menu.template.getWidth() / 2));
				int newX = x + ((height / 2) - (Main.game.menu.template.getHeight() / 2));
				Main.game.grid.grid[newX][newY] = a[x][y];									//plots the array mapped from an imported image to a plain grid
				cridFromImage();
			}
		}
		return a;			//returns the array

	}

	public int getNeighbours(int x, int y) {
		int output = 0;
		for (int a = x - 1; a <= x + 1; a++) {
			for (int b = y - 1; b <= y + 1; b++) {
				int c = a;
				int d = b;
				if (c >= width) {
					c -= width;
				} else if (c < 0) {
					c += width;
				}
				if (d >= height) {
					d -= height;
				} else if (d < 0) {
					d += height;
				}

				if (c != x || d != y) {
					if (grid[c][d]) {
						output++;
					}						//gets the number of 'neighbours' for each cell with given coordinates
				}
			}
		}
		return output;
	}

	public Point getGridSquareClicked(Point mousePos) {
		int mouseX = mousePos.x - x;
		int mouseY = mousePos.y - y;
		Point squareClicked = new Point(-1, -1);
		squareClicked.x = mouseX / squareSize;
		squareClicked.y = mouseY / squareSize;
		return squareClicked;							//maps the pixel coordinate of the mouse pointer to a grid square on screen
	}

	public void moveMouseOnGrid(int dx, int dy, Point mousePos) {
		try {
			Robot r = new Robot();
			Point p = Main.game.grid.getGridSquareClicked(mousePos);
			p.x += dx;
			p.y += dy;

			Point newCoord = Main.game.grid.getRealXY(p);
			r.mouseMove(newCoord.x, newCoord.y);				//uses a robot to move the mouse the equivalent number of pixels in a grid square in a chosen direction

		} catch (AWTException e) {
			System.err.println("warning: robot fail");				//error catch for unsuccessful translation
		}
	}

	public Point getRealXY(Point gridSquare) {
		int realX = (int) (x + (gridSquare.x + 0.5) * squareSize);
		int realY = (int) (y + (gridSquare.y + 0.5) * squareSize);
		Point screenPoint = new Point(realX, realY);
		return screenPoint;										//maps a given square on the grid to a real pixel coordinate on the screen
	}

	public void click(Point mousePos) {
		Point squareClicked = getGridSquareClicked(mousePos);
		grid[squareClicked.x][squareClicked.y] = !grid[squareClicked.x][squareClicked.y];			//toggles the state of a grid square when clicked
	}

	public int getPop() {
		int pop = 0;
		for (int x = 0; x < Grid.height; x++) {
			for (int y = 0; y < Grid.width; y++) {
				if (Main.game.grid.grid[x][y] == true) {
					pop++;
				}										//gets the population of the grid 
			}
		}
		return pop;
	}

	public void draw(Graphics2D g2d) {

		g2d.setColor(Color.DARK_GRAY);
		for (int a = 0; a <= width; a++) {
			g2d.drawLine(x + a * squareSize, y, x + a * squareSize, y + height * squareSize);				
		}
		for (int b = 0; b <= height; b++) {
			g2d.drawLine(x, y + b * squareSize, x + width * squareSize, y + b * squareSize);
		}																						//draws the grid lines
		g2d.setColor(Main.game.options.getColour());			//user determined colour
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (Main.game.grid.grid[x][y]) {
					g2d.fillRect(this.x + x * squareSize, this.y + y * squareSize, squareSize, squareSize);			//fills any true grid cells with a coloured square
				}
			}
		}
		g2d.setColor(Color.darkGray);
		g2d.drawRect(0, 0, x, Frame.HEIGHT - 1);
		g2d.drawRect(x + width * squareSize, 0, (Frame.WIDTH - (x + width * squareSize)) - 1, Frame.HEIGHT - 1);		//draws the two bordering rectangles for aesthetics using formatting given the grid size

	}

}
