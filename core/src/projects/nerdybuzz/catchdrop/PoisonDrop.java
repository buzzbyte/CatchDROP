package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class PoisonDrop extends FallingRect implements FallingAct {
	private static final long serialVersionUID = 1L;
	private ParticleEffect fireEffect;
	private ParticleEffectPool fireEffectPool;
	private PooledEffect firePEffect;
	private float bucketX;
	private float bucketY;
	private int poisonTimer;
	private long nowTimeInSeconds;
	private long lastTimeInSeconds;
	private BitmapFont timerFont;
	
	public PoisonDrop(final CDGame game) {
		super(game);
		x = MathUtils.random(0, game.GAME_WIDTH-64);
		y = game.GAME_HEIGHT;
		width = 64;
		height = 64;
		loseOnMiss = false;
		gainValue = 0;
		loseValue = 2;
		rectImg = new Texture("poison.png");
		
		poisonTimer = 10;
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		lastTimeInSeconds = nowTimeInSeconds;
		
		fireEffect = new ParticleEffect();
		fireEffect.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
		fireEffectPool = new ParticleEffectPool(fireEffect, 1, 2);
		firePEffect = fireEffectPool.obtain();
	}
	
	public PoisonDrop(final CDGame game, float x, float y, BitmapFont timerFont) {
		super(game);
		this.x = x;
		this.y = y;
		width = 64;
		height = 64;
		loseOnMiss = false;
		gainValue = 0;
		loseValue = 2;
		rectImg = new Texture("poison.png");
		
		poisonTimer = 10;
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		lastTimeInSeconds = nowTimeInSeconds;
		
		fireEffect = new ParticleEffect();
		fireEffect.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
		fireEffectPool = new ParticleEffectPool(fireEffect, 1, 2);
		firePEffect = fireEffectPool.obtain();
		
		this.timerFont = timerFont;
	}
	
	public void render(float delta) {
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		timerFont.setColor(Color.GREEN);
		if(ZenGame.activePoisonDrop)
			timerFont.draw(game.batch, game.secondsToTime(poisonTimer, false), game.GAME_WIDTH-130, game.GAME_HEIGHT-60);
		game.batch.end();
	}

	public void update(float delta) {
		nowTimeInSeconds = TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000;
		if(!game.paused && ZenGame.activePoisonDrop) {
			if(lastTimeInSeconds != nowTimeInSeconds) {
				poisonTimer--;
				lastTimeInSeconds = nowTimeInSeconds;
			}
			
			if(poisonTimer < 0) {
				finish();
			}
		}
		
		firePEffect.setPosition(GameScreen.bucket.x+64/2, GameScreen.bucket.y+64);
		firePEffect.update(delta);
		if(game.zenScore == 0) {
			finish();
		}
	}
	
	public void onMiss() {
		ZenGame.activePoisonDrop = false;
		ZenGame.spawnedPoisonDrop = false;
	}
	
	public void onGet() {
		GameScreen.effects.add(firePEffect);
		GameScreen.poisoned = true;
		ZenGame.activePoisonDrop = true;
		CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQCw");
	}
	
	public void onSpawn() {}
	
	public void onDispose() {
		rectImg.dispose();
		firePEffect.dispose();
		fireEffect.dispose();
	}
	
	public void dispose() {
		onDispose();
	}
	
	public void finish() {
		GameScreen.poisoned = false;
		firePEffect.reset();
		GameScreen.effects.removeValue(firePEffect, false);
		ZenGame.activePoisonDrop = false;
		ZenGame.spawnedPoisonDrop = false;
	}

}
