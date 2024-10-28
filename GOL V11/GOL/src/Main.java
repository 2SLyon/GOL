public class Main {

	static Game game;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		game = new Game();						
		game.start();				//runs start in 'game' (running is set to true)
	}

}
