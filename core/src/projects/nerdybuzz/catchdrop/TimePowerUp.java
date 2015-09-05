package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class TimePowerUp extends FallingRect implements FallingAct {
	private static final long serialVersionUID = 1L;
	
	public TimePowerUp(final CDGame game) {
		super(game);
		x = MathUtils.random(0, game.GAME_WIDTH-64);
		y = game.GAME_HEIGHT;
		width = 64;
		height = 64;
		loseOnMiss = false;
		gainValue = 1;
		loseValue = 0;
		rectImg = new Texture("dropTime.png");
	}
	
	public void onGet() {
		game.timerTime += 10;
		if(!game.spawnDrops) game.spawnDrops = true;
	}
}