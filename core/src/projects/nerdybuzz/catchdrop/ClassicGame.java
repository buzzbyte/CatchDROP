package projects.nerdybuzz.catchdrop;

public class ClassicGame extends GameScreen {

	public ClassicGame(CDGame game, int bucketX, boolean bucketTouched) {
		super(game, bucketX, bucketTouched);
		game.showMissedDrops = false;
		loseOnMissedDrop = true;
		spawnBurntToast = true;
		game.showZenScores = false;
		poisoned = false;
		CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQAQ"); // "New Game" Achievement
		AchievementSystem.giveAchievement("CgkI9czCiZEfEAIQAQ", 0);
	}

}
