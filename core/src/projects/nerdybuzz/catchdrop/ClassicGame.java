package projects.nerdybuzz.catchdrop;

public class ClassicGame extends GameScreen {

	public ClassicGame(CDGame game, int bucketX, boolean bucketTouched) {
		super(game, bucketX, bucketTouched);
		game.showMissedDrops = false;
		loseOnMissedDrop = true;
		spawnBurntToast = true;
		game.showZenScores = false;
		poisoned = false;
	}

}
