package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.utils.TimeUtils;

public class ZenGame extends GameScreen {
	public long lastTimeInSeconds;

	public ZenGame(CDGame game) {
		super(game);
		game.showMissedDrops = true;
		loseOnMissedDrop = false;
		spawnBurntToast = false;
		timerTime = 60;
		lastTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
	}
	
	public void update(float delta) {
		super.update(delta);
		if(!game.paused) {
			if(lastTimeInSeconds != TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000) {
				timerTime--;
				lastTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
			}
			if(timerTime < 0) {
				game.setScreen(new EndScreen(game));
				dispose();
			}
		}
	}

}
