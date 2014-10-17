package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MainMenuScreen implements Screen {
	final CDGame game;
	OrthographicCamera camera;
	
	CharSequence welcomeText = "CatchDROP";
	CharSequence promptText1;
	private BitmapFont welcomeFont;
	private BitmapFont promptFont;
	private BitmapFont verisonFont;
	
	public MainMenuScreen(final CDGame game) {
		this.game = game;
		camera = game.camera;
		//camera.setToOrtho(false, game.GAME_WIDTH, game.GAME_HEIGHT);
		promptText1 = game.callToAction + " to play";
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		welcomeFont = game.assManager.get("title.ttf", BitmapFont.class);
		promptFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		verisonFont = game.assManager.get("verison.ttf", BitmapFont.class);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		verisonFont.setColor(Color.WHITE);
		verisonFont.draw(game.batch, CDGame.GAME_VERSION, 5, game.GAME_HEIGHT-5);
		welcomeFont.setColor(Color.GREEN);
		welcomeFont.draw(game.batch, welcomeText.toString(), game.GAME_WIDTH/2-welcomeFont.getBounds(welcomeText).width/2, game.GAME_HEIGHT/2-welcomeFont.getBounds(welcomeText).height+200);
		promptFont.setColor(Color.WHITE);
		promptFont.draw(game.batch, promptText1, game.GAME_WIDTH/2-promptFont.getBounds(promptText1).width/2, game.GAME_HEIGHT/2-promptFont.getBounds(promptText1).height*2);
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta) {
		if(Gdx.input.isTouched()) {
			game.setScreen(game.gScr);
			dispose();
		}
		
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		game.resize(width, height);
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
