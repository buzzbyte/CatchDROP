package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class EndScreen implements Screen {
	final CDGame game;
	OrthographicCamera camera;
	private CharSequence goverText = "GAME OVER";
	CharSequence optionText1;
	private CharSequence gscoreText;
	private CharSequence gzenscoreText;
	private CharSequence gmissedText;
	private CharSequence gtotalText;
	private CharSequence ghscoreText;
	private CharSequence ghzenscoreText;
	private CharSequence gnewhighscoreText;
	private CharSequence promptText1;
	private BitmapFont mainFont;
	private BitmapFont scoreFont;
	private BitmapFont promptFont;
	@SuppressWarnings("unused")
	private BitmapFont cornerFont;
	
	private float bucketX;
	private float bucketY;
	private int settingsBtnX;
	private int settingsBtnY;
	private Vector3 touchPos;
	
	private Texture settingsIcon;
	private boolean showMissedDrops;
	private boolean showZenScores;
	

	public EndScreen(final CDGame game) {
		this.game = game;
		camera = game.camera;
		showMissedDrops = game.showMissedDrops;
		showZenScores = game.showZenScores;
		
		bucketX = game.GAME_WIDTH/2-64/2;
		bucketY = 20;
		
		settingsBtnX = 10;
		settingsBtnY = 10;
		
		settingsIcon = new Texture("gear_btn.png");
		
		touchPos = new Vector3();
		
		gnewhighscoreText = "";
		
		if(game.score > game.getHighscore()) {
			game.setHighscore(game.score);
			gnewhighscoreText = "New Classic Highscore!";
		}
		
		if(game.zenTotal > game.getZenHighscore()) {
			game.setZenHighscore(game.zenTotal);
			gnewhighscoreText = "New Zen Highscore!";
		}
		
		if(game.missedDrops < game.zenScore) {
			game.zenTotal = game.zenScore - game.missedDrops;
		} else {
			game.zenTotal = game.zenScore;
		}
		
		gscoreText = "Score: " + game.score;
		gzenscoreText = "Score: " + game.zenScore;
		gmissedText = "Missed: " + game.missedDrops;
		gtotalText = "Total: " + game.zenTotal;
		ghscoreText = "Highscore: " + game.getHighscore();
		ghzenscoreText = "Highscore: " + game.getZenHighscore();
		promptText1 = game.callToAction + " a bucket to play again";
		optionText1 = "Auto-Pause: " + game.autoPauseStr + " (P)";
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		mainFont = game.assManager.get("gover.ttf", BitmapFont.class);
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		promptFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		cornerFont = game.assManager.get("corner.ttf", BitmapFont.class);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.shapeRender.setProjectionMatrix(camera.combined);
		game.shapeRender.begin(ShapeType.Filled);
		game.shapeRender.setColor(Color.DARK_GRAY);
		game.shapeRender.rect(0, 0, game.GAME_WIDTH, 20);
		game.shapeRender.end();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(new Texture("bucket-classic.png"), bucketX/2, bucketY); // Classic
		game.batch.draw(new Texture("bucket-zen.png"), bucketX*1.5f, bucketY); // Zen
		//scoreFont.setColor(Color.BLUE);
		//scoreFont.draw(game.batch, game.classicText, (bucketX/2)-(scoreFont.getBounds(game.classicText).width/2), bucketY+40);
		//scoreFont.setColor(Color.GREEN);
		//scoreFont.draw(game.batch, game.zenText, (bucketX*1.5f)+(scoreFont.getBounds(game.zenText).width/2), bucketY+40);
		promptFont.setColor(Color.GREEN);
		promptFont.draw(game.batch, gnewhighscoreText.toString(), game.GAME_WIDTH/2-promptFont.getBounds(gnewhighscoreText).width/2, game.GAME_HEIGHT-30);
		mainFont.setColor(Color.RED);
		mainFont.draw(game.batch, goverText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(goverText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(goverText).height+200+10);
		scoreFont.setColor(Color.YELLOW);
		if(!showZenScores) scoreFont.draw(game.batch, gscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gscoreText).height+55+10);
		else scoreFont.draw(game.batch, gzenscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gzenscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gzenscoreText).height+55+10);
		if(showMissedDrops) scoreFont.draw(game.batch, gmissedText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gmissedText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gmissedText).height+20+10);
		if(!showZenScores && !showMissedDrops) scoreFont.draw(game.batch, ghscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghscoreText).height+20);
		else if(showZenScores && showMissedDrops) {
			scoreFont.draw(game.batch, gtotalText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gtotalText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gtotalText).height-15+10);
			scoreFont.draw(game.batch, ghzenscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghzenscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghzenscoreText).height-50+10);
		}
		promptFont.setColor(Color.WHITE);
		promptFont.draw(game.batch, promptText1.toString(), game.GAME_WIDTH/2-promptFont.getBounds(promptText1).width/2, game.GAME_HEIGHT/2-promptFont.getBounds(promptText1).height*(showMissedDrops ? 4 : 2));
		//if(game.usingDesktop) cornerFont.draw(game.batch, optionText1, 10, cornerFont.getBounds(optionText1).height+10);
		game.batch.draw(settingsIcon, settingsBtnX, settingsBtnY, 64, 64);
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta) {
		optionText1 = "Auto-Pause: " + game.autoPauseStr + " (P)";
		
		if(Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			
			if(game.gScr instanceof ClassicGame) {
				game.classicText = "Play Again";
				game.zenText = "Zen";
			} else if(game.gScr instanceof ZenGame) {
				game.classicText = "Classic";
				game.zenText = "Play Again";
			}
			
			/*
			if(touchPos.x >= bucketX && touchPos.x <= bucketX+64) {
				if(touchPos.y >= bucketY && touchPos.y <= bucketY+64) {
					game.score = 0;
					game.zenScore = 0;
					game.missedDrops = 0;
					if(game.gScr instanceof ClassicGame) {
						if(!game.spawnDrops) game.spawnDrops = true;
						game.setScreen(new ClassicGame(game));
					} else if(game.gScr instanceof ZenGame) {
						if(!game.spawnDrops) game.spawnDrops = true;
						game.setScreen(new ZenGame(game));
					}
					
					dispose();
				}
			}
			// */
			
			if(touchPos.x >= bucketX/2 && touchPos.x <= bucketX/2+124) {
				if(touchPos.y >= bucketY && touchPos.y <= bucketY+64) {
					game.score = 0;
					game.zenScore = 0;
					game.zenTotal = 0;
					game.missedDrops = 0;
					game.gScr = new ClassicGame(game, (int)bucketX, false);
					if(!game.spawnDrops) game.spawnDrops = true;
					game.setScreen(new ClassicGame(game, (int)bucketX, true));
					dispose();
				}
			}
			
			if(touchPos.x >= bucketX*1.5f && touchPos.x <= bucketX*1.5f+64) {
				if(touchPos.y >= bucketY && touchPos.y <= bucketY+64) {
					game.score = 0;
					game.zenScore = 0;
					game.zenTotal = 0;
					game.missedDrops = 0;
					game.gScr = new ZenGame(game, (int)bucketX, false);
					if(!game.spawnDrops) game.spawnDrops = true;
					game.setScreen(new ZenGame(game, (int)bucketX, true));
					dispose();
				}
			}
			
			if(touchPos.x >= settingsBtnX && touchPos.x <= settingsBtnX+64) {
				if(touchPos.y >= settingsBtnY && touchPos.y <= settingsBtnY+64) {
					//System.out.println("Settings button triggered!");
					game.gSettings = new SettingsScreen(game, this);
					game.setScreen(game.gSettings);
				}
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.P))
			if(game.autoPause) game.autoPause = false; else game.autoPause = true;
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {
		if(Gdx.input.isCursorCatched()) {
			Gdx.input.setCursorCatched(false);
		}
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
