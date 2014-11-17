package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class ZenGame extends GameScreen {
	public long lastTimeInSeconds;
	public long nowTimeInSeconds;
	
	public FallingRect timePowerUp;
	private boolean spawnedTimePowerUp = false;
	private int randomNumber1;

	public ZenGame(CDGame game) {
		super(game);
		game.showMissedDrops = true;
		game.showZenScores = true;
		loseOnMissedDrop = false;
		spawnBurntToast = false;
		game.timerTime = 60;
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		lastTimeInSeconds = nowTimeInSeconds;
				
		timePowerUp = new TimePowerUp(game);
		
		randomNumber1 = MathUtils.random(-5, 5);
		System.out.println("power-up spawning: "+randomNumber1);
	}
	
	public void render(float delta) {
		super.render(delta);
		
	}
	
	public void update(float delta) {
		super.update(delta);
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
	}

}
