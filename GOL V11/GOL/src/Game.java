import java.io.FileNotFoundException;		//imports:

public class Game implements Runnable {
	boolean running = false;		//sets the default to not run when the grid is initially drawn
	int tickCount = 0;
	
	public static int STATE = 0;

	public Frame frame;
	public Grid grid;
	public Mayonnaise mayo;
	public Menu menu;
	public Rules rules;
	public ImageReader reader;
	public Options options;
	public GridSelect select;			//establishes each class

	public synchronized void start() {
		running = true;								//program based running is determined if program has started, it is running (template)
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void init() {
		frame = new Frame(this);
		grid = new Grid();
		mayo = new Mayonnaise(frame);
		menu = new Menu();
		rules = new Rules();
		reader = new ImageReader();
		options = new Options();
		select = new GridSelect();		//initialises the classes when the program is run
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 64;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();
		// running = false;	
																
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				tick();
				try {
					frame.render();
				} catch (FileNotFoundException e) {
					System.err.println("Unable to render for some reason");
				}
				delta--;
			}
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
			}																	//basic template tick system that checks if a render has occurred each tick
		}
	}

	public void tick() {
		tickCount++;				//incriments the ticks 

		mayo.tick();				//in game tick system (used for grid)
	}
}