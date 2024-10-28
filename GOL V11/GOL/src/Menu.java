import java.awt.Color;			//imports:
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.w3c.dom.css.RGBColor;						


public class Menu {

	BufferedImage template = null;						//resets the template if menu is (re)opened

	Font titleFont = new Font("Arial", Font.BOLD, 50);
	Font optionsFont = new Font("Arial", Font.PLAIN, 40);
	String title = "Conway's Game of Life";
	List<String> options = new ArrayList<>(Arrays.asList("Play", "Use Image Template", "Rules and Controls", "Options", "Quit"));
	public boolean[][] ImageGrid;
	public Rectangle2D bounds;
	public int option = 0;				//establishes fonts and a list of menu options


	int x = Frame.WIDTH / 2;			//formatting for centring the text on screen

	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT);
		g2d.setColor(Main.game.options.getColour());
		g2d.setFont(titleFont);
		drawString(title, x, 150, g2d);
		g2d.setFont(optionsFont);
		for (String s : options) {
			g2d.setColor(Color.GRAY);
			int index = options.indexOf(s);
			String b = s;
			if (index == option) {
				g2d.setColor(Main.game.options.getColour());				//if the option is cycled, it is highlighted 
			}
			drawString(b, x, 400 + 100 * index, g2d);						//draws each item on the list of strings with a buffer proportional to their position in the list

		}
	}

	public void chooseOption() {
		String optionChosen = options.get(option);
		switch (optionChosen) {
		case "Play":
			Main.game.grid.Generations = 0;
			Game.STATE = 1;
			break;
		case "Use Image Template":
			Main.game.grid.Generations = 0;
			Main.game.select.selected = true;
			Main.game.reader.getFile();
			break;
		case "Rules and Controls":
			Game.STATE = 2;
			break;
		case "Options":
			Game.STATE = 5;
			break;
		case "Quit":
			System.exit(0);
			break;
		default:
			break;
		}												//carries out functions for each menu option
	}
	
	public void drawString(String s, int x, int y, Graphics2D g2d) {
		bounds = g2d.getFontMetrics().getStringBounds(s, g2d);
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();

		g2d.drawString(s, x - width / 2, y + height / 2);
	}													//the method for drawing a string (used for menu)
	
	public Rectangle r(String s, int x, int y, Graphics2D g2d) {
		Rectangle2D rectBounds = g2d.getFontMetrics().getStringBounds(s, g2d);
		Rectangle r = new Rectangle(x, y, (int)rectBounds.getWidth() + 3, (int)rectBounds.getHeight() + 3);
		return r;
	}													//function that gets the size and coordinates of the border of a given string


}
