package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
	final CDGame game;
	OrthographicCamera camera;
	
	CharSequence welcomeText = "CatchDROP";
	CharSequence optionText1;
	CharSequence ghscoreText;
	CharSequence ghzenscoreText;
	CharSequence promptText1;
	private BitmapFont welcomeFont;
	private BitmapFont scoreFont;
	private BitmapFont promptFont;
	private BitmapFont cornerFont;
	
	private float bucketX;
	private float bucketY;
	private int settingsBtnX;
	private int settingsBtnY;
	private Vector3 touchPos;
	
	private Texture settingsIcon;
	
	
	public MainMenuScreen(final CDGame game) {
		this.game = game;
		camera = game.camera;
		promptText1 = game.callToAction + " the bucket to play";
		
		bucketX = game.GAME_WIDTH/2-64/2;
		bucketY = 20;
		
		settingsBtnX = 10;
		settingsBtnY = 10;
		
		settingsIcon = new Texture("gear_btn.png");
		
		touchPos = new Vector3();
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		optionText1 = "Auto-Pause: " + game.autoPauseStr + " (P)";
		ghscoreText = "Classic Highscore: " + game.getHighscore();
		ghzenscoreText = "Zen Highscore: " + game.getZenHighscore();
		
		welcomeFont = game.assManager.get("title.ttf", BitmapFont.class);
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		promptFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		cornerFont = game.assManager.get("corner.ttf", BitmapFont.class);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.shapeRender.setProjectionMatrix(camera.combined);
		game.shapeRender.begin(ShapeType.Filled);
		game.shapeRender.setColor(Color.DARK_GRAY);
		game.shapeRender.rect(0, 0, game.GAME_WIDTH, 20);
		game.shapeRender.end();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(new Texture("bucket.png"), bucketX, bucketY);
		cornerFont.setColor(Color.WHITE);
		cornerFont.draw(game.batch, CDGame.GAME_VERSION, 5, game.GAME_HEIGHT-5);
		//if(game.usingDesktop) cornerFont.draw(game.batch, optionText1, 10, cornerFont.getBounds(optionText1).height+10);
		welcomeFont.setColor(Color.GREEN);
		welcomeFont.draw(game.batch, welcomeText.toString(), game.GAME_WIDTH/2-welcomeFont.getBounds(welcomeText).width/2, game.GAME_HEIGHT/2-welcomeFont.getBounds(welcomeText).height+200);
		scoreFont.setColor(Color.YELLOW);
		//if(game.usingDesktop) scoreFont.draw(game.batch, optionText1.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(optionText1).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(optionText1).height+55);
		scoreFont.draw(game.batch, ghscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghscoreText).height+25);
		scoreFont.draw(game.batch, ghzenscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghzenscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghzenscoreText).height-15);
		promptFont.setColor(Color.WHITE);
		promptFont.draw(game.batch, promptText1, game.GAME_WIDTH/2-promptFont.getBounds(promptText1).width/2, game.GAME_HEIGHT/2-promptFont.getBounds(promptText1).height*4);
		game.batch.draw(settingsIcon, settingsBtnX, settingsBtnY, 64, 64);
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta) {
		optionText1 = "Auto-Pause: " + game.autoPauseStr + " (P)";
		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			
			if(touchPos.x >= bucketX && touchPos.x <= bucketX+64) {
				if(touchPos.y >= bucketY && touchPos.y <= bucketY+64) {
					game.score = 0;
					game.missedDrops = 0;
					if(game.gScr instanceof ClassicGame) {
						game.setScreen(new ClassicGame(game));
					} else if(game.gScr instanceof ZenGame) {
						game.setScreen(new ZenGame(game));
					}
					
					dispose();
				}
			}
			
			if(touchPos.x >= settingsBtnX && touchPos.x <= settingsBtnX+64) {
				if(touchPos.y >= settingsBtnY && touchPos.y <= settingsBtnY+64) {
					//System.out.println("Settings button triggered!");
					game.gSettings = new SettingsScreen(game);
					game.setScreen(game.gSettings);
					dispose();
				}
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.P))
			if(game.autoPause) game.autoPause = false; else game.autoPause = true;
		
		camera.update();
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
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
