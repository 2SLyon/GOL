import java.awt.Canvas;		//imports:
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;						
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;							

public class Frame extends Canvas {
	Font rulesFont = new Font("Calibri", Font.PLAIN, 30);
	FontMetrics fm;
	boolean inrules;
	JFrame frameComponent;
	Game game;
	public String rules;
	public static int stringX;
	public static int stringY;
	public static int spacer = 45;
	public static int buttonspacer = 20;
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();	//used for getting pixel dimensions of the screen used
	public static final Dimension WINDOW_SIZE = new Dimension(WIDTH, HEIGHT);
	public BufferedReader br;
	public static String[] fullRules;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public Rectangle startB;
	public Rectangle stepB;
	public Rectangle clearB;
	public Rectangle speedinc;
	public Rectangle speeddec;
	public Rectangle colour;
	public Rectangle importtemp;		//the rectangle bounds used for my buttons

	public Frame(Game game) {
		this.game = game;
		frameComponent = new JFrame();
		frameComponent.setUndecorated(true);
		frameComponent.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frameComponent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameComponent.add(this);
		frameComponent.setVisible(true);								//formatting the frame

	}

	public void render() throws FileNotFoundException {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}																//gets a BufferStartegy for rendering

		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();				
		g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);		//draws 'image' described earlier as an image field that fills the screen

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);								//then fills the field with a black rectangle for the background

		if (Game.STATE == 0) {
			Main.game.menu.render(g2d);									//renders the menu
		}
		if (Game.STATE == 1 || Game.STATE == 4) {
			if (Main.game.select.selected == false) {					//checks to see whether the gird size has already been chosen by the user
				GridSelect.SelectSize(this);
			} else {
				g2d.setColor(Main.game.options.getColour());
				g2d.setFont(new Font("Calibri", Font.PLAIN, 40));
				fm = g2d.getFontMetrics();
				String G = "GENERATION: " + (Integer.toString(Main.game.grid.Generations));
				g2d.drawString(G, Main.game.grid.x / 2 - fm.stringWidth(G) / 2, fm.getHeight() + 35);

				String P = "POPULATION: " + (Integer.toString(Main.game.grid.Population));
				g2d.drawString(P, Main.game.grid.x / 2 - fm.stringWidth(G) / 2, (fm.getHeight() + 35) + spacer);

				String S = "SPEED: " + (Integer.toString(1024 / Main.game.mayo.timeDelay));
				g2d.drawString(S, Main.game.grid.x / 2 - fm.stringWidth(G) / 2, (fm.getHeight() + 35) + 2 * spacer);		//draws each information string using a formatted method with a predetermined buffer for spacing

				String start = " START ";
				if (Main.game.mayo.running == true) {

					start = " STOP ";
				} else {
					start = " START ";			//if the game is running, the start button reads 'stop'
				}
				String step = " STEP ";
				String clear = " RESET ";
				String speedup = " +SPEED ";
				String speeddown = " -SPEED ";
				String colourtoggle = " COLOUR ";
				String importtemplate = " IMPORT TEMPLATE";		//initialises the strings for their corresponding buttons

				g2d.drawString(start, (Main.game.grid.x / 2) - fm.stringWidth(start) / 2,
						(fm.getHeight() + 35) + 5 * spacer + buttonspacer);
				startB = Main.game.menu.r(start, Main.game.grid.x / 2 - fm.stringWidth(start) / 2,
						(((fm.getHeight() + 35) + (5 * spacer)) - (fm.getHeight() + 35) / 2) + buttonspacer, g2d);

				g2d.drawString(step, (Main.game.grid.x / 2) - fm.stringWidth(step) / 2,
						(fm.getHeight() + 35) + (6 * spacer) + (2 * buttonspacer));
				stepB = Main.game.menu.r(step, Main.game.grid.x / 2 - fm.stringWidth(step) / 2,
						(((fm.getHeight() + 35) + (6 * spacer)) - (fm.getHeight() + 35) / 2) + (2 * buttonspacer), g2d);

				g2d.drawString(clear, (Main.game.grid.x / 2) - fm.stringWidth(clear) / 2,
						(fm.getHeight() + 35) + (7 * spacer) + (3 * buttonspacer));
				clearB = Main.game.menu.r(clear, Main.game.grid.x / 2 - fm.stringWidth(clear) / 2,
						(((fm.getHeight() + 35) + (7 * spacer)) - (fm.getHeight() + 35) / 2) + (3 * buttonspacer), g2d);

				g2d.drawString(speedup, (Main.game.grid.x / 2) - fm.stringWidth(speedup) / 2,
						(fm.getHeight() + 35) + (8 * spacer) + (4 * buttonspacer));
				speedinc = Main.game.menu.r(speedup, Main.game.grid.x / 2 - fm.stringWidth(speedup) / 2,
						(((fm.getHeight() + 35) + (8 * spacer)) - (fm.getHeight() + 35) / 2) + (4 * buttonspacer), g2d);

				g2d.drawString(speeddown, (Main.game.grid.x / 2) - fm.stringWidth(speeddown) / 2,
						(fm.getHeight() + 35) + (9 * spacer) + (5 * buttonspacer));
				speeddec = Main.game.menu.r(speeddown, Main.game.grid.x / 2 - fm.stringWidth(speeddown) / 2,
						(((fm.getHeight() + 35) + (9 * spacer)) - (fm.getHeight() + 35) / 2) + (5 * buttonspacer), g2d);

				g2d.drawString(colourtoggle, (Main.game.grid.x / 2) - fm.stringWidth(colourtoggle) / 2,
						(fm.getHeight() + 35) + (10 * spacer) + (6 * buttonspacer));
				colour = Main.game.menu.r(colourtoggle, Main.game.grid.x / 2 - fm.stringWidth(colourtoggle) / 2,
						(((fm.getHeight() + 35) + (10 * spacer)) - (fm.getHeight() + 35) / 2) + (6 * buttonspacer),
						g2d);
				
				g2d.drawString(importtemplate, (Frame.WIDTH -  Main.game.grid.x) + fm.stringWidth(importtemplate) / 4,
						(fm.getHeight() + 35) + (2 * spacer) + (2 * buttonspacer));
				importtemp = Main.game.menu.r(importtemplate, (Frame.WIDTH -  Main.game.grid.x) + fm.stringWidth(importtemplate) / 4,
						(((fm.getHeight() + 35) + (2 * spacer)) - (fm.getHeight() + 35) / 2) + (2 * buttonspacer),							//initialises the position of each string (which is drawn) and the position of each button
						g2d);

				g2d.draw(startB);
				g2d.draw(stepB);
				g2d.draw(clearB);
				g2d.draw(speedinc);
				g2d.draw(speeddec);
				g2d.draw(colour);
				g2d.draw(importtemp);		//draws the buttons

				if (Main.game.grid.Stable == true) {
					g2d.drawString("STABLE", Main.game.grid.x / 2 - fm.stringWidth(G) / 2,
							(fm.getHeight() + 35) + 4 * spacer);
				}																				//draws the stable indicator

				if (Main.game.mayo.running) {
					g2d.setColor(Main.game.options.getColour());
					g2d.setFont(new Font("Calibri", Font.PLAIN, 40));
					int stringWidth = fm.stringWidth("RUNNING");
					int stringHeight = fm.getHeight();
					g2d.drawString("RUNNING", Main.game.grid.x / 2 - fm.stringWidth(G) / 2,
							(fm.getHeight() + 35) + 3 * spacer);								//draws the running indicator

				}

				Main.game.grid.draw(g2d);
			}
		}
		if (Game.STATE == 2) {
			drawRules(g2d);																		//draws the rules
		}

		if (Game.STATE == 5) {
			Main.game.options.render(g2d);														//renders the options screen
		}

		g2d.dispose();
		bs.show();																				//compiles and clears the graphics handler, and then displays any rendered objects
	}

	public void drawRules(Graphics2D g2d) throws FileNotFoundException {

		br = new BufferedReader(new FileReader("GOL/res/rules.txt"));
		List<String> list = new ArrayList<String>();
		String s = "";
		String[] rulesList = new String[list.size()];
		try {
			while ((s = br.readLine()) != null) {
				list.add(s);
				rulesList = list.toArray(new String[0]);						//copies the rules text file to a string array line by line
			}
			g2d.setColor(Main.game.options.getColour());
			g2d.setFont(rulesFont);
			fm = g2d.getFontMetrics();
			int ruleswidth = fm.stringWidth(rulesList[2]);
			int rulesheight = fm.getAscent();
			for (int a = 0; a < rulesList.length; a++) {
				g2d.drawString(rulesList[a], 30, (a * rulesheight + 7) + rulesheight + 20);		//draws the rules with a predetermined font and formatting

			}

		} catch (IOException e) {
			String fullRules = "file not found";
			g2d.drawString(fullRules, (WIDTH - 70) / 2, (HEIGHT / 2));			//error catch for when the rules text file is not found
		}

	}

}
