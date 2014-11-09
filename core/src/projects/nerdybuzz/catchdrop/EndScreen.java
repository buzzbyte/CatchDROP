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
	private String ghscoreText;
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
	

	public EndScreen(final CDGame game) {
		this.game = game;
		camera = game.camera;
		
		bucketX = game.GAME_WIDTH/2-64/2;
		bucketY = 20;
		
		settingsBtnX = 10;
		settingsBtnY = 10;
		
		settingsIcon = new Texture("gear_btn.png");
		
		touchPos = new Vector3();
		
		if(game.score > game.getHighscore()) {
			game.setHighscore(game.score);
		}
		
		gscoreText = "Score: " + game.score;
		ghscoreText = "Highscore: " + game.getHighscore();
		promptText1 = game.callToAction + " the bucket to play again";
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
		game.batch.draw(new Texture("bucket.png"), bucketX, bucketY);
		mainFont.setColor(Color.RED);
		mainFont.draw(game.batch, goverText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(goverText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(goverText).height+200);
		scoreFont.setColor(Color.YELLOW);
		scoreFont.draw(game.batch, gscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gscoreText).height+55);
		scoreFont.draw(game.batch, ghscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghscoreText).height+20);
		promptFont.setColor(Color.WHITE);
		promptFont.draw(game.batch, promptText1.toString(), game.GAME_WIDTH/2-promptFont.getBounds(promptText1).width/2, game.GAME_HEIGHT/2-promptFont.getBounds(promptText1).height*2);
		//if(game.usingDesktop) cornerFont.draw(game.batch, optionText1, 10, cornerFont.getBounds(optionText1).height+10);
		game.batch.draw(settingsIcon, settingsBtnX, settingsBtnY, 44, 44);
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta) {
		optionText1 = "Auto-Pause: " + game.autoPauseStr + " (P)";
		
		if(Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			
			if(touchPos.x >= bucketX && touchPos.x <= bucketX+64) {
				if(touchPos.y >= bucketY && touchPos.y <= bucketY+64) {
					game.score = 0;
					game.setScreen(new GameScreen(game));
					dispose();
				}
			}
			
			if(touchPos.x >= settingsBtnX && touchPos.x <= settingsBtnX+44) {
				if(touchPos.y >= settingsBtnY && touchPos.y <= settingsBtnY+44) {
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
