package projects.nerdybuzz.catchdrop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	private Rectangle bucket;
	private Rectangle ground;
	private Array<Rectangle> raindrops;
	private long lastTimeDropped;
	private long rainTimer = 1000;
	private long delay = 100;
	private long delayTimer = delay;
	
	
	private CharSequence pauseText = "PAUSED";
	private long tempHighscore;

	private Vector3 touchPos;

	private BitmapFont scoreFont;

	private BitmapFont mainFont;
	
	public GameScreen(final CDGame game) {
		this.game = game;
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		mainFont = game.assManager.get("score.ttf", BitmapFont.class);
		camera = game.camera;
		//camera.setToOrtho(false, game.GAME_WIDTH, game.GAME_HEIGHT);
		
		touchPos = new Vector3();
		
		tempHighscore = game.getHighscore();
		
		bucketImg = new Texture(Gdx.files.internal("bucket.png"));
		dropImg = new Texture(Gdx.files.internal("drop.png"));
		//bucketImg = new Texture("sprites/bucket.png");
		//dropImg = new Texture("sprites/drop.png");
		
		bucket = new Rectangle(game.GAME_WIDTH/2-64/2, 20, 64, 64);
		ground = new Rectangle(0, 0, game.GAME_WIDTH, bucket.y);
		raindrops = new Array<Rectangle>();
		//spawnRaindrop();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle(MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, 64, 64);
		raindrops.add(raindrop);
		lastTimeDropped = TimeUtils.nanoTime();
	}
	
	@Override
	public void render(float delta) {
		if(!game.paused) {
			// TODO Auto-generated method stub
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			scoreFont.setColor(Color.YELLOW);
			scoreFont.draw(game.batch, "Score: "+game.score, 10, game.GAME_HEIGHT-10);
			scoreFont.draw(game.batch, "Highscore: "+tempHighscore, 10, game.GAME_HEIGHT-40);
			game.batch.draw(bucketImg, bucket.x, bucket.y);
			for(Rectangle raindrop : raindrops) {
				game.batch.draw(dropImg, raindrop.x, raindrop.y);
			}
			game.batch.end();
			
			game.shapeRender.setProjectionMatrix(camera.combined);
			game.shapeRender.begin(ShapeType.Filled);
			game.shapeRender.setColor(Color.DARK_GRAY);
			game.shapeRender.rect(ground.x, ground.y, ground.width, ground.height);
			game.shapeRender.end();
			
			update(Gdx.graphics.getDeltaTime());
		} else {
			Gdx.gl.glClearColor(0, 0, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			mainFont.setColor(Color.YELLOW);
			mainFont.draw(game.batch, pauseText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(pauseText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(pauseText).height/4);
			//Rectangle resumeBtn = new Rectangle();
			//resumeBtn.set(game.GAME_WIDTH/2-resumeBtn.width/2, game.GAME_HEIGHT/2-resumeBtn.height/2, 200, 50);
			//resumeBtn.
			game.batch.end();
			if(Gdx.input.isKeyJustPressed(Keys.P)) resume();
		}
	}
	
	public void update(float delta) {
		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x-64/2;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 650*delta;
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 650*delta;
		
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > game.GAME_WIDTH-64) bucket.x = game.GAME_WIDTH-64;
		
		if(TimeUtils.nanoTime()-lastTimeDropped>TimeUtils.millisToNanos(rainTimer)) spawnRaindrop();
		
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 210*delta;
			if(raindrop.y+64 < 0) {
				iter.remove();
				game.setScreen(new EndScreen(game));
				dispose();
			}
			if(raindrop.overlaps(ground)) {
				iter.remove();
				game.setScreen(new EndScreen(game));
				dispose();
			}
			if(raindrop.overlaps(bucket)) {
				game.score++;
				if(game.score > tempHighscore) tempHighscore = game.score;
				iter.remove();
			}
		}
		
		if(Gdx.input.isTouched()) {
			
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.P)) pause();
		
		delayTimer -= delta;
		
		if(delayTimer <= 0) {
			//System.out.println("Triggered delay.");
			rainTimer -= 10;
			delayTimer = delay;
			//System.out.println("rainTimer = " + rainTimer);
		} /* else {
			//System.out.println("delayTimer = " + delayTimer);
		} // */
		
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
		game.paused = true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		game.paused = false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Texture[] disposableTextures = {dropImg, bucketImg};
		for(Texture disposable : disposableTextures) {
			disposable.dispose();
		}
	}

}
