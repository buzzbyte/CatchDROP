package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EndScreen implements Screen {
	final CDGame game;
	OrthographicCamera camera;
	private CharSequence goverText = "GAME OVER";
	private CharSequence gscoreText;
	private String ghscoreText;
	private CharSequence promptText1;
	private BitmapFont mainFont;
	private BitmapFont scoreFont;
	private BitmapFont promptFont;

	public EndScreen(final CDGame game) {
		this.game = game;
		camera = game.camera;
		
		if(game.score > game.getHighscore()) {
			game.setHighscore(game.score);
		}
		
		gscoreText = "Score: " + game.score;
		ghscoreText = "Highscore: " + game.getHighscore();
		promptText1 = game.callToAction + " to play again";
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		mainFont = game.assManager.get("gover.ttf", BitmapFont.class);
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		promptFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		//camera.setToOrtho(false, game.GAME_WIDTH, game.GAME_HEIGHT);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		mainFont.setColor(Color.RED);
		mainFont.draw(game.batch, goverText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(goverText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(goverText).height+200);
		scoreFont.setColor(Color.YELLOW);
		scoreFont.draw(game.batch, gscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(gscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(gscoreText).height/4);
		scoreFont.draw(game.batch, ghscoreText.toString(), game.GAME_WIDTH/2-scoreFont.getBounds(ghscoreText).width/2, game.GAME_HEIGHT/2-scoreFont.getBounds(ghscoreText).height*2);
		promptFont.setColor(Color.WHITE);
		promptFont.draw(game.batch, promptText1.toString(), game.GAME_WIDTH/2-promptFont.getBounds(promptText1).width/2, game.GAME_HEIGHT/2-promptFont.getBounds(promptText1).height*6);
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta) {
		if(Gdx.input.justTouched()) {
			game.score = 0;
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
