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
	private boolean spawnDrops = true;
	private boolean bucketTouched = false;
	
	private CharSequence pauseText = "PAUSED";
	private CharSequence pausePromptText;
	private long tempHighscore;

	private Vector3 touchPos;

	private BitmapFont scoreFont;

	private BitmapFont mainFont;
	
	public GameScreen(final CDGame game) {
		this.game = game;
		
		pausePromptText = game.callToAction + " the bucket to resume";
		
		if(!game.assManager.isLoaded("title.ttf")) {
			game.assManager.finishLoading();
		}
		
		scoreFont = game.assManager.get("score.ttf", BitmapFont.class);
		mainFont = game.assManager.get("prompt.ttf", BitmapFont.class);
		camera = game.camera;
		
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
		if(spawnDrops) {
			Rectangle raindrop = new Rectangle(MathUtils.random(0, game.GAME_WIDTH-64), game.GAME_HEIGHT, 64, 64);
			raindrops.add(raindrop);
			lastTimeDropped = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		scoreFont.setColor(Color.YELLOW);
		scoreFont.draw(game.batch, "Score: "+game.score, 10, game.GAME_HEIGHT-10);
		scoreFont.draw(game.batch, "Highscore: "+tempHighscore, 10, game.GAME_HEIGHT-40);
		game.batch.end();
		
		if(!game.paused) {
			// TODO Auto-generated method stub
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
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
		} else {
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			mainFont.setColor(Color.WHITE);
			mainFont.draw(game.batch, pauseText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(pauseText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(pauseText).height+20);
			mainFont.setColor(Color.LIGHT_GRAY);
			mainFont.draw(game.batch, pausePromptText.toString(), game.GAME_WIDTH/2-mainFont.getBounds(pausePromptText).width/2, game.GAME_HEIGHT/2-mainFont.getBounds(pausePromptText).height-20);
			game.batch.draw(bucketImg, bucket.x, bucket.y);
			game.batch.end();
			
			game.shapeRender.setProjectionMatrix(camera.combined);
			game.shapeRender.begin(ShapeType.Filled);
			game.shapeRender.setColor(Color.DARK_GRAY);
			game.shapeRender.rect(ground.x, ground.y, ground.width, ground.height);
			game.shapeRender.end();
		}
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
			} else {
				bucketTouched = false;
				pause();
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
					if(iter.hasNext())
						iter.remove();
				}
			}
			
			if(Gdx.input.isTouched()) {
				
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.P)) pause();
			
			delayTimer -= delta;
			
			if(delayTimer <= 0) {
				rainTimer -= 10;
				delayTimer = delay;
			}
		} else {
			if(Gdx.input.isKeyJustPressed(Keys.P)) resume();
			if(Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				
				if(touchPos.x >= bucket.x && touchPos.x <= bucket.x+bucket.width) {
					if(touchPos.y >= bucket.y && touchPos.y <= bucket.y+bucket.height) {
						resume();
					}
				}
			}
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
