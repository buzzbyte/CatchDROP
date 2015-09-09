package projects.nerdybuzz.catchdrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final CDGame game;
	
	private OrthographicCamera camera;
	
	private Texture bucketImg;
	private Texture dropImg;
	//private Texture burntToastImg;
	
	protected static Rectangle bucket;
	protected Rectangle ground;
	protected Array<Rectangle> raindrops;
	protected Array<FallingRect> fallingObjects;
	protected FallingRect burntToast;
	
	protected static boolean poisoned = false;
	
	protected long lastTimeDropped;
	protected long rainTimer = 1000;
	protected long delay = 100;
	protected long delayTimer = delay;
	
	protected boolean spawnBurntToast = true;
	protected boolean loseOnMissedDrop = true;
	
	private CharSequence pauseText = "PAUSED";
	private CharSequence pausePromptText;
	private CharSequence optionText1;
	private String optionText2;
	private long tempHighscore;
	private long tempZenHighscore;

	private Vector3 touchPos;
	private Vector3 mousePos;
	private boolean bucketTouched = false;
	
	protected BitmapFont timerFont;
	private BitmapFont scoreFont;
	private BitmapFont mainFont;
	private BitmapFont cornerFont;

	private int burntToastScore;
	private boolean burntToastExists;
	
	private Texture backgroundImg;
	
	private Texture cloudsTop;

	private Texture cloudsBase;

	//private boolean triggerSplash = false;

	public PooledEffect waterSplash;
	private ParticleEffect waterSplashPE;
	private ParticleEffectPool waterSplashPEP;

	private KeySequence konami;

	private boolean doKonamiCheat = false;

	private Integer[] konamiCode = {19, 19, 20, 20, 21, 22, 21, 22, 30, 29};

	private ArrayList<Integer> konamiCodeList;

	private int bucketX;

	private GlyphLayout glayout;
	
	public static Array<PooledEffect> effects = new Array<PooledEffect>();
	
	public GameScreen(final CDGame game, int bucketX) {
		this.game = game;
		//this.bucketX = bucketX;
		initGame(bucketX);
	}
	
	public GameScreen(final CDGame game, int bucketX, boolean bucketTouched) {
		this.game = game;
		//this.bucketX = bucketX;
		this.bucketTouched = bucketTouched;
		initGame(bucketX);
	}
	
	public GameScreen(final CDGame game) {
		this.game = game;
		this.bucketX = game.GAME_WIDTH/2-64/2;
		initGame(bucketX);
	}
	// */
	
	public void initGame(int bucketX) {
		pausePromptText = game.callToAction + " the bucket to resume";
		optionText1 = "Pause (P)";
		optionText2 = "Resume (P)";
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		timerFont = game.assManager.get("timer.ttf", BitmapFont.class);
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		mainFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		cornerFont = game.assManager.get("corner.ttf", BitmapFont.class);
		camera = game.camera;
		
		glayout = new GlyphLayout();
		
		touchPos = new Vector3();
		mousePos = new Vector3();
		
		tempHighscore = game.getHighscore();
		tempZenHighscore = game.getZenHighscore();
		burntToastScore = MathUtils.random(20, 30);
		System.out.println(burntToastScore);
		
		bucketImg = new Texture(Gdx.files.internal("bucket.png"));
		dropImg = new Texture(Gdx.files.internal("drop.png"));
		if(game.usingDesktop || game.GAME_HEIGHT < 768) {
			backgroundImg = new Texture(Gdx.files.internal("bg_mini.png"));
		} else {
			backgroundImg = new Texture(Gdx.files.internal("bg_ipad.png"));
		}
		cloudsTop = new Texture("clouds-top.png");
		cloudsBase = new Texture("clouds-base.png");
		//burntToastImg = new Texture(Gdx.files.internal("burntToast.jpg"));
		
		waterSplashPE = new ParticleEffect();
		waterSplashPE.load(Gdx.files.internal("water-splash.p"), Gdx.files.internal(""));
		waterSplashPEP = new ParticleEffectPool(waterSplashPE, 1, 2);
		waterSplash = waterSplashPEP.obtain();
		waterSplash.start();
		
		
		konamiCodeList = new ArrayList<Integer>();
		Collections.addAll(konamiCodeList, konamiCode);
		konami = new KeySequence(konamiCodeList);
		Gdx.input.setInputProcessor(konami);
		// */
		
		bucket = new Rectangle(bucketX, 20, 64, 64);
		ground = new Rectangle(0, 0, game.GAME_WIDTH, bucket.y);
		raindrops = new Array<Rectangle>();
		fallingObjects = new Array<FallingRect>();
		//burntToast = new FallingRect(MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, 64, 64, false, 10, 0, new Texture("burntToast.jpg"));
		burntToast = new BurntToastObj(game);
	}

	private void spawnRaindrop() {
		if(game.spawnDrops && !burntToastExists) {
			Rectangle raindrop = new Rectangle(MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, 64, 64);
			raindrops.add(raindrop);
			lastTimeDropped = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(backgroundImg, 0, game.GAME_HEIGHT-(backgroundImg.getHeight()));
		game.batch.draw(cloudsBase, 0, game.GAME_HEIGHT-(cloudsBase.getHeight()));		
		game.batch.end();
		
		/*
		game.shapeRender.setProjectionMatrix(camera.combined);
		game.shapeRender.begin(ShapeType.Filled);
		game.shapeRender.setColor(Color.DARK_GRAY);
		game.shapeRender.rect(ground.x, ground.y, ground.width, ground.height);
		game.shapeRender.end();
		// */
		
		if(!game.paused) {
			
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			
			for(Rectangle raindrop : raindrops) {
				game.batch.draw(dropImg, raindrop.x, raindrop.y);
			}
			
			for(FallingRect fallingObject : fallingObjects) {
				game.batch.draw(fallingObject.getImg(), fallingObject.x, fallingObject.y, fallingObject.width, fallingObject.height);
			}
			/*
			if(spawnBurntToast) {
				game.batch.draw(burntToastImg, burntToast.x, burntToast.y);
			}
			// */
			/*
			if(fallingObjects.contains(burntToast, false)) {
				game.batch.draw(burntToastImg, fallingObjects.get(fallingObjects.indexOf(burntToast, false)).getX(), fallingObjects.get(fallingObjects.indexOf(burntToast, false)).getY(), 64, 64);
			}
			// */
			/*
			if(triggerSplash) {
				System.out.println("Splash!");
				waterSplash.draw(game.batch, delta);
				if(waterSplash.isComplete()) {
					waterSplash.reset();
					triggerSplash = false;
				}
			}
			// */
			for (int i = effects.size - 1; i >= 0; i--) {
			    PooledEffect effect = effects.get(i);
			    effect.draw(game.batch, delta);
			    if (effect.isComplete()) {
			        effect.free();
			        effects.removeIndex(i);
			    }
			}
			game.batch.draw(bucketImg, bucket.x, bucket.y);
			if(!game.autoPause) {
				glayout.setText(cornerFont, optionText1);
				cornerFont.draw(game.batch, glayout, 10, glayout.height+10);
			}
			game.batch.end();
		} else {
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			game.batch.draw(bucketImg, bucket.x, bucket.y);
			mainFont.setColor(Color.BLACK);
			glayout.setText(mainFont, pauseText);
			mainFont.draw(game.batch, glayout, game.GAME_WIDTH/2-glayout.width/2, game.GAME_HEIGHT/2-glayout.height+20);
			mainFont.setColor(Color.DARK_GRAY);
			glayout.setText(mainFont, pausePromptText);
			mainFont.draw(game.batch, glayout, game.GAME_WIDTH/2-glayout.width/2, game.GAME_HEIGHT/2-glayout.height-20);
			if(!game.autoPause) {
				glayout.setText(cornerFont, optionText2);
				cornerFont.draw(game.batch, glayout, 10, glayout.height+10);
			}
			game.batch.end();
		}
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(cloudsTop, 0, game.GAME_HEIGHT-(cloudsTop.getHeight()-10));
		
		if(game.gScr instanceof ZenGame) {
			if(game.timerTime<=5) timerFont.setColor(Color.RED);
			else timerFont.setColor(Color.BLUE);
			timerFont.draw(game.batch, game.secondsToTime(game.timerTime, false), game.GAME_WIDTH-130, game.GAME_HEIGHT-10);
		}
		
		scoreFont.setColor(Color.DARK_GRAY);
		if(!game.showZenScores) scoreFont.draw(game.batch, "Score: "+game.score, 10, game.GAME_HEIGHT-10);
		else scoreFont.draw(game.batch, "Score: "+game.zenScore, 10, game.GAME_HEIGHT-10);
		if (game.showMissedDrops) scoreFont.draw(game.batch, "Missed: "+game.missedDrops, 10, game.GAME_HEIGHT-40);
		if(!game.showZenScores) scoreFont.draw(game.batch, "Highscore: "+tempHighscore, 10, game.GAME_HEIGHT-(game.showMissedDrops ? 80 : 40));
		else {
			scoreFont.setColor(Color.GRAY);
			scoreFont.draw(game.batch, "Highscore: "+tempZenHighscore, 10, game.GAME_HEIGHT-(game.showMissedDrops ? 80 : 40));
		}
		//AchievementSystem.drawAchivement(game, cornerFont, scoreFont);
		
		game.batch.end();
		
		update(delta);
	}
	
	public void update(float delta) {
		if(!game.paused) {
			if(Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				
				if(touchPos.x >= bucket.x && touchPos.x <= bucket.x+bucket.width) {
					if(touchPos.y >= bucket.y && touchPos.y <= bucket.y+bucket.height) {
						bucketTouched = true;
					}
				}
				
				if(bucketTouched) {
					bucket.x = touchPos.x-64/2;
				}
				waterSplash.setPosition(bucket.x, bucket.y+64);
			} else {
				if(game.noDrag && !game.autoPause) {
					mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
					camera.unproject(mousePos);
					bucket.x = mousePos.x-64/2;
					Gdx.input.setCursorCatched(true);
					//Gdx.input.setCursorPosition((int)mousePos.x, (int)bucket.y+game.GAME_HEIGHT-55);
					waterSplash.setPosition(bucket.x, bucket.y+64);
					waterSplash.update(delta);
				} else {
					bucketTouched = false;
					if(game.autoPause) pause();
				}
			}
			if(bucketTouched && game.noDrag && !game.autoPause) {
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
					pause();
				}
			}
			
			if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 750*delta;
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 750*delta;
			
			if(bucket.x < 0) bucket.x = 0;
			if(bucket.x > game.GAME_WIDTH-64) bucket.x = game.GAME_WIDTH-64;
			
			if(TimeUtils.nanoTime()-lastTimeDropped>TimeUtils.millisToNanos(rainTimer)) spawnRaindrop();
			
			Iterator<Rectangle> iter = raindrops.iterator();
			while(iter.hasNext()) {
				Rectangle raindrop = iter.next();
				raindrop.y -= 210*delta;
				if(raindrop.y+64 < 0) {
					if(loseOnMissedDrop) {
						game.setScreen(new EndScreen(game));
						dispose();
					}
					game.missedDrops++;
					if(game.gScr instanceof ZenGame) updateZenTotal();
					iter.remove(); continue;
				}
				if(raindrop.overlaps(ground)) {
					if(loseOnMissedDrop) {
						game.setScreen(new EndScreen(game));
						dispose();
					}
					game.missedDrops++;
					if(game.gScr instanceof ZenGame) updateZenTotal();
					iter.remove(); continue;
				}
				if(raindrop.overlaps(bucket)) {
					if(poisoned) {
						if(game.gScr instanceof ZenGame) game.zenScore--;
						else game.score--;
					} else if(doKonamiCheat){
						if(game.gScr instanceof ZenGame) game.zenScore+=10;
						else game.score+=10;
					} else {
						if(game.gScr instanceof ZenGame) game.zenScore++;
						else game.score++;
					}
					
					if(game.gScr instanceof ZenGame) updateZenTotal();
					if(game.score > tempHighscore) tempHighscore = game.score;
					if(game.zenTotal > tempZenHighscore) tempZenHighscore = game.zenTotal;
					manageScoreAchievements();
					waterSplash.reset();
					effects.add(waterSplash);
					//triggerSplash = true;
					//waterSplash.update(delta);
					iter.remove(); continue;
				}
			}
			
			Iterator<FallingRect> fallingIter = fallingObjects.iterator();
			while(fallingIter.hasNext()) {
				FallingRect fallingObject = fallingIter.next();
				fallingObject.y -= 210*delta;
				if(fallingObject.y+64 < 0) {
					if(fallingObject.loseOnMiss) {
						System.out.println("Missed Object!!");
						game.setScreen(new EndScreen(game));
						dispose();
					}
					burntToastExists = false;
					fallingObject.onMiss();
					updateZenTotal();
					fallingIter.remove(); continue;
				}
				if(fallingObject.overlaps(ground)) {
					if(fallingObject.loseOnMiss) {
						System.out.println("Missed Object!!");
						game.setScreen(new EndScreen(game));
						dispose();
					}
					burntToastExists = false;
					fallingObject.onMiss();
					updateZenTotal();
					fallingIter.remove(); continue;
				}
				if(fallingObject.overlaps(bucket)) {
					burntToastExists = false;
					fallingObject.onGet();
					updateZenTotal();
					if(game.score > tempHighscore) tempHighscore = game.score;
					if(game.zenTotal > tempZenHighscore) tempZenHighscore = game.zenTotal;
					fallingIter.remove(); continue;
				}
			}
			
			if(game.score == burntToastScore) {
				if(spawnBurntToast) {
					System.out.println("Triggered Burnt Toast..");
					fallingObjects.add(burntToast);
					burntToastExists = true;
					spawnBurntToast = false;
				}
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.SPACE)) pause();
			
			delayTimer -= delta;
			
			if(delayTimer <= 0) {
				rainTimer -= 10;
				delayTimer = delay;
			}
		} else {
			if(Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.SPACE)) resume();
			if(Gdx.input.isCursorCatched()) {
				Gdx.input.setCursorCatched(false);
			}
			if(Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				
				if(touchPos.x >= bucket.x && touchPos.x <= bucket.x+bucket.width) {
					if(touchPos.y >= bucket.y && touchPos.y <= bucket.y+bucket.height) {
						if(game.usingDesktop) {
							if(Gdx.input.isButtonPressed(Buttons.LEFT)){
								Gdx.input.setCursorCatched(true);
								resume();
							}
						} else {
							resume();
						}
					}
				}
			}
			
			if(konami.sequenceFound()) {
				System.out.println("Konami!!");
				doKonamiCheat = true;
				konami.reset();
			}
			konami.update();
			// */
		}
		
		camera.update();
	}
	
	private void manageScoreAchievements() {
		switch(game.score) {
		case 50:
			// "Fifty Nifty!" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQAg");
			break;
		case 100:
			// "Ten Tens!" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQAw");
			break;
		case 200:
			// "On a roll!" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQBA");
			break;
		case 300:
			// "Quantitative Water" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQBQ");
			break;
		case 500:
			// "Are you human?" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQBg");
			break;
		case 1000:
			// "Get a life!!" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQBw");
			break;
		case 9000:
			// "IT'S OVER 9000!!!" Achievement
			CDGame.googleServices.unlockAchievement("CgkI9czCiZEfEAIQCA");
			break;
		}
	}

	public void updateZenTotal() {
		if(game.missedDrops < game.zenScore) {
			game.zenTotal = game.zenScore - game.missedDrops;
		} else {
			game.zenTotal = game.zenScore;
		}
	}

	@Override
	public void resize(int width, int height) {
		game.resize(width, height);
	}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		Gdx.input.setCursorPosition((int)game.GAME_WIDTH/2, (int)game.GAME_HEIGHT/2);
		game.paused = true;
	}

	@Override
	public void resume() {
		game.paused = false;
	}

	@Override
	public void dispose() {
		Texture[] disposableTextures = {dropImg, bucketImg};
		for(Texture disposable : disposableTextures) {
			disposable.dispose();
		}
		waterSplash.dispose();
	}

}
