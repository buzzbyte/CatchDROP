package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class BurntToastObj extends FallingRect implements FallingAct {
	private static final long serialVersionUID = 1L;
	
	
	public BurntToastObj(final CDGame game) {
		super(game);
		x = MathUtils.random(0, game.GAME_WIDTH-64);
		y = game.GAME_HEIGHT;
		width = 64;
		height = 64;
		loseOnMiss = false;
		gainValue = 10;
		loseValue = 0;
		rectImg = new Texture("burntToast.jpg");
	}

	@Override
	public void onMiss() {}

	@Override
	public void onGet() {
		System.out.println("Got my toast!!");
		game.score += 10;
		// "Got my toast!!" Achievement
		CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQDg");
	}

	@Override
	public void onSpawn() {}

	@Override
	public void onDispose() {}

}
