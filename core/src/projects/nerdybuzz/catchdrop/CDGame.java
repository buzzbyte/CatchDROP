package projects.nerdybuzz.catchdrop;

import org.json.simple.JSONObject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CDGame extends Game {
	public static final String GAME_VERSION = "v2.0-alpha";
	//public static final String GAME_VERSION = "testing-alpha";
	
	public final int GAME_WIDTH  = 800;
	public final int GAME_HEIGHT = 480;
	
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRender;
	protected BitmapFont font;
	protected OrthographicCamera camera;
	protected MainMenuScreen mMScr;
	protected GameScreen gScr;
	
	public boolean paused = false;
	public int score = 0;
	public long highScore;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;
	public AssetManager assManager;
	public String callToAction = "Tap or Click";
	public JSONObject gameJson;
	public Preferences gamePrefs;
	
	public CDGame() {}
	
	public CDGame(String callToAction) {
		this.callToAction = callToAction;
	}
	
	public void create() {
		assManager = new AssetManager();
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter titleParams = new FreeTypeFontLoaderParameter();
		titleParams.fontFileName = "font/arial.ttf";
		titleParams.fontParameters.size = 95;
		assManager.load("title.ttf", BitmapFont.class, titleParams);
		
		FreeTypeFontLoaderParameter promptParams = new FreeTypeFontLoaderParameter();
		promptParams.fontFileName = "font/arial.ttf";
		promptParams.fontParameters.size = 35;
		assManager.load("prompt.ttf", BitmapFont.class, promptParams);
		
		FreeTypeFontLoaderParameter scoreParams1 = new FreeTypeFontLoaderParameter();
		scoreParams1.fontFileName = "font/arial.ttf";
		scoreParams1.fontParameters.size = 27;
		assManager.load("score.ttf", BitmapFont.class, scoreParams1);
		
		FreeTypeFontLoaderParameter goverParams = new FreeTypeFontLoaderParameter();
		goverParams.fontFileName = "font/arial.ttf";
		goverParams.fontParameters.size = 85;
		assManager.load("gover.ttf", BitmapFont.class, goverParams);
		
		FreeTypeFontLoaderParameter versionParams = new FreeTypeFontLoaderParameter();
		versionParams.fontFileName = "font/arial.ttf";
		versionParams.fontParameters.size = 20;
		assManager.load("verison.ttf", BitmapFont.class, versionParams);
		
		/*
		if(Gdx.files.internal("data.json").exists()) {
			System.out.println("Data file exists!");
			
			//gameJson = new JSONObject(Gdx.files.internal("data.json"));
			JSONParser jsonParser = new JSONParser();
			try {
				Object dataJson = jsonParser.parse(Gdx.files.internal("data.json").reader());
				JSONObject dataObj = (JSONObject) dataJson;
				JSONObject jsonHighScore = (JSONObject) dataObj.get("classic_mode");
				highScore = (Long) jsonHighScore.get("highscore");
				System.out.println("Strored Highscore: " + jsonHighScore.get("highscore"));
				System.out.println("Highscore: " + highScore);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("'data.json' does not exist in assets directory. Starting game with defaults (e.g. no highscore saving)...");
		}
		// */
		
		gamePrefs = Gdx.app.getPreferences("Game");
		//gamePrefs.getLong("highscore", 0);
		
		batch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		
		mMScr = new MainMenuScreen(this);
		gScr = new GameScreen(this);
		
		this.setScreen(mMScr);
	}

	@Override
	public void render() {
		if(assManager.update() && assManager.isLoaded("title.ttf")) {
			super.render();
		}
	}
	
	@Override
	public void pause() {
		if(this.getScreen() == gScr) {
			System.out.println("Paused.");
			paused = true;
		}
	}

	@Override
	public void resume() {
		//System.out.println("Resumed.");
		//paused = false;
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = GAME_WIDTH;
		camera.viewportHeight = GAME_HEIGHT;
	}
	
	public long getHighscore() {
		return gamePrefs.getLong("highscore", 0);
	}
	
	public void setHighscore(long highscore) {
		/*
		if(Gdx.files.internal("data.json").exists()) {
			JSONParser jsonParser = new JSONParser();
			try {
				Object dataJson = jsonParser.parse(Gdx.files.internal("data.json").reader());
				JSONObject dataObj = (JSONObject) dataJson;
				JSONObject jsonHighScore = (JSONObject) dataObj.get("classic_mode");
				jsonHighScore.put("highscore", highscore);
				highScore = highscore;
				/*
				FileWriter dataWriter = new FileWriter(Gdx.files.internal("data.json").file());
				dataWriter.write(dataObj.toJSONString());
				dataWriter.flush();
				dataWriter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// */
		gamePrefs.putLong("highscore", highscore);
		highScore = highscore;
		gamePrefs.flush();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		assManager.dispose();
	}
}










