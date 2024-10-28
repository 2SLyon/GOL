import java.awt.Color;			//imports:
import java.awt.Graphics2D;		

public class Rules {

	public void render(Graphics2D g2d){
			
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT);
		g2d.setColor(Color.RED);
		Main.game.menu.drawString("RULES", 400, 300, g2d);
																//default rules method for error checking etc.
		
		
	}
	
	
}
