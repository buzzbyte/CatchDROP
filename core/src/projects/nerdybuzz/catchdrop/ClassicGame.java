package projects.nerdybuzz.catchdrop;

public class ClassicGame extends GameScreen {

	public ClassicGame(CDGame game) {
		super(game);
		game.showMissedDrops = false;
		loseOnMissedDrop = true;
		spawnBurntToast = true;
	}

}
