import java.awt.Color;					//imports:
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImageReader {

	public void getFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setCurrentDirectory(new File("GOL/res/TEMPLATES"));				
		fc.showOpenDialog(Main.game.frame.frameComponent);				//opens a dialogue window showing the files in the provided folder
		File fileSelected = null;
		while (fileSelected == null) {
			fileSelected = fc.getSelectedFile();						//waits for the user to select a file
		}			
		try {
			String filePath = fileSelected.getAbsolutePath();			//gets the file-path of the selected file
			Main.game.menu.template = ImageIO.read(new FileInputStream(filePath));
			if (Main.game.menu.template.getWidth() > Grid.width || Main.game.menu.template.getHeight() > Grid.height) {
				 JOptionPane.showMessageDialog(Main.game.frame.frameComponent, "Adjusting Grid Size");
				Main.game.grid.height = Main.game.menu.template.getHeight() + (int)(Main.game.menu.template.getHeight() / 1.2) ;
				Main.game.grid.width = Main.game.grid.height;																		//adjusts the grid height if the template used is too large
			} 
				Main.game.menu.ImageGrid = new boolean[Main.game.menu.template.getWidth()][Main.game.menu.template.getHeight()];
				for (int x = 0; x < Main.game.menu.template.getHeight(); x++) {
					for (int y = 0; y < Main.game.menu.template.getWidth(); y++) {
						Color RGBValue = new Color(Main.game.menu.template.getRGB(x, y));
						int red = RGBValue.getRed();										
						Main.game.menu.ImageGrid[x][y] = red < 50;								//gets the colour value of each pixel, and if the pixel contains a red element over a certain threshold the corresponding grid square is set to false
																												//otherwise true
					}
				}
				Main.game.grid.importImage(Main.game.menu.ImageGrid);				//maps the template grid to a full size grid 

			

		} catch (IOException e) {
			System.err.println("Warning: program couldnt be bothered mapping the filepath");		//error catch :)
		}
	}

}
