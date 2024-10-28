import java.awt.Color;		//imports:
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class Options {
	static Font optionsFont = new Font("Calibri", Font.PLAIN, 40);
	static int optionsspacer = 35;
	static FontMetrics fm;
	public static List<Color> colorList = Arrays.asList(Color.MAGENTA, Color.CYAN, Color.WHITE, Color.BLUE, Color.RED, Color.ORANGE, Color.PINK, Color.YELLOW, Color.LIGHT_GRAY);		
	public static List<String> colornames = Arrays.asList("MAGENTA", "CYAN", "WHITE", "BLUE", "RED", "ORANGE", "PINK", "YELLOW", "LIGHT GREY");		//two lists for each colour and its displayed name
	public static int c = 0;
	public static Rectangle GenB;	
	public static Rectangle ColB;
	
	
	public static Color getColour() {
		Color color = colorList.get(c % colorList.size());	//ensures that the reference for the index never exits the bounds of the list
		return color;										//method for returning current selected colour
	}
	
	public static void render(Graphics2D g2d) {
		String colorname = colornames.get(c % colornames.size());
		fm = g2d.getFontMetrics(optionsFont);
		g2d.setColor(getColour());
		g2d.setFont(optionsFont);
		String C = "COLOUR: " + colorname;
		g2d.drawString(C, Frame.WIDTH / 2 - fm.stringWidth(C) / 2, Frame.HEIGHT / 2);
		ColB = Main.game.menu.r(C, Frame.WIDTH / 2 - fm.stringWidth(C) / 2, Frame.HEIGHT / 2 - (fm.getHeight() / 2 + 15), g2d);
		g2d.draw(ColB);
		String returnmenu = "M to return";
		g2d.drawString(returnmenu, Frame.WIDTH / 2 - fm.stringWidth(returnmenu) / 2, Frame.HEIGHT / 2 + 7 * optionsspacer);
		String generationsselect = "Modify rules";
		g2d.drawString(generationsselect, Frame.WIDTH / 2 - fm.stringWidth(generationsselect) / 2, Frame.HEIGHT / 2 - 4 * optionsspacer);
		GenB = Main.game.menu.r(generationsselect, Frame.WIDTH / 2 - fm.stringWidth(generationsselect) / 2, Frame.HEIGHT / 2 - 4 * optionsspacer - (fm.getHeight() / 2 + 15), g2d);		//displays the text for cycling colours and rule modifying
		g2d.draw(GenB);
	}																																

	public static void changerules(Frame frame) {
		Object[] pop = {"STRONG  min:1  max:2", "DEFAULT  min:2  max:3", "WEAK  min:3  Max:4"};							
		String max = (String) JOptionPane.showInputDialog(frame, "Select neighbour survival threshold:\n", "Enter:",
				JOptionPane.PLAIN_MESSAGE, null, pop, "DEFAULT  min:2  max:3");
		switch(max) {																							//I use the same system here as in choosing the grid size
		case "STRONG  min:1  max:2":
			Main.game.grid.maxNeighbours = 2;
			Main.game.grid.minNeighbours = 1;
			break;
		case "DEFAULT  min:2  max:3":
			Main.game.grid.maxNeighbours = 3;
			Main.game.grid.minNeighbours = 2;
			break;
		case "WEAK  min:3  Max:4":
			Main.game.grid.maxNeighbours = 4;
			Main.game.grid.minNeighbours = 3;
			System.out.println("Min: " + Main.game.grid.minNeighbours + " Max: " + Main.game.grid.maxNeighbours);
			break;
		default:
			break;
		}
		
		
	}
	
	
}
