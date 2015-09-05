package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class ZenGame extends GameScreen {
	public long lastTimeInSeconds;
	public long nowTimeInSeconds;
	
	public FallingRect timePowerUp;
	private PoisonDrop poisonDrop;
	private boolean spawnedTimePowerUp = false;
	private int randomNumber1;
	private int randomNumber2;
	public static boolean spawnedPoisonDrop = false;
	public static boolean activePoisonDrop = false;
	public boolean poisonSpawnTimed;

	public ZenGame(CDGame game, int bucketX, boolean bucketTouched) {
		super(game, bucketX, bucketTouched);
		game.showMissedDrops = true;
		game.showZenScores = true;
		loseOnMissedDrop = false;
		spawnBurntToast = false;
		poisoned = false;
		game.timerTime = 60;
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		lastTimeInSeconds = nowTimeInSeconds;
				
		timePowerUp = new TimePowerUp(game);
		poisonDrop = new PoisonDrop(game, MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, timerFont);
		
		randomNumber1 = MathUtils.random(-5, 5);
		randomNumber2 = MathUtils.random(45, 55);
		poisonSpawnTimed = true;
		System.out.println("power-up spawning: "+randomNumber1);
		System.out.println("poison spawning: "+randomNumber2);
		CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQAQ"); // "New Game" Achievement
	}
	
	public void render(float delta) {
		super.render(delta);
		poisonDrop.render(delta);
	}
	
	public void update(float delta) {
		super.update(delta);
		poisonDrop.update(delta);
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		if(!game.paused) {
			if(lastTimeInSeconds != nowTimeInSeconds) {
				game.timerTime--;
				lastTimeInSeconds = nowTimeInSeconds;
			}
			
			if(game.timerTime == 1) {
				game.spawnDrops = false;
			}
			
			if(game.timerTime < 0) {
				game.setScreen(new EndScreen(game));
				dispose();
			}
		}
		
		if(randomNumber1 == game.timerTime && game.spawnDrops && !spawnedTimePowerUp) {
			fallingObjects.add(timePowerUp);
			spawnedTimePowerUp = true;
		}
		
		if(!spawnedPoisonDrop && !activePoisonDrop && !poisonSpawnTimed) {
			randomNumber2 = MathUtils.random(game.timerTime-10, game.timerTime);
			poisonSpawnTimed = true;
			System.out.println("poison spawning: "+randomNumber2);
		}
		
		if(randomNumber2 == game.timerTime && game.spawnDrops && !activePoisonDrop && !spawnedPoisonDrop && poisonSpawnTimed) {
			poisonDrop = new PoisonDrop(game, MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, timerFont);
			fallingObjects.add(poisonDrop);
			spawnedPoisonDrop = true;
			poisonSpawnTimed = false;
		}
	}
	
	public void dispose() {
		timePowerUp.dispose();
		poisonDrop.dispose();
	}

}
