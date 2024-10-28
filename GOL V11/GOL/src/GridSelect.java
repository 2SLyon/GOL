import javax.swing.JOptionPane;		//imports:

public class GridSelect {
	public static boolean selected = false;

	public static void SelectSize(Frame frame) {
		Object[] possibilities = { "small", "medium", "large", "insane" };							//creates a new list of potential grid size options
		String s = (String) JOptionPane.showInputDialog(frame, "Select Grid Size:\n", "Grid Size",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "medium");							//displays a dialogue box allowing the user to select the grid size they want to use 	
										//default grid size is set to medium
		
		switch (s) {
		case "small":
			Main.game.grid.squareSize = 20;
			Main.game.grid.crid();	
			break;
		case "medium":
			Main.game.grid.squareSize = 10;
			Main.game.grid.crid();
			break;
		case "large":
			Main.game.grid.squareSize = 5;
			Main.game.grid.crid();
			break;
		case "insane":
			Main.game.grid.squareSize = 2;
			Main.game.grid.crid();
			break;
		default:
			break;											//determines the square size for each size
		}
		selected = true;									//tells the program that the user has selected a grid size
	}

}
