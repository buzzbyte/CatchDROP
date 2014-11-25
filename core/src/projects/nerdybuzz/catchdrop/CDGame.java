package projects.nerdybuzz.catchdrop;

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
	//public static final String GAME_VERSION = "v0.3.0 (alpha)";
	public static final String GAME_VERSION = "testing-alpha";
	
	public final int GAME_WIDTH  = 800;
	public final int GAME_HEIGHT = 480;
	
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRender;
	protected BitmapFont font;
	protected OrthographicCamera camera;
	protected MainMenuScreen mMScr;
	protected GameScreen gScr;
	protected SettingsScreen gSettings;
	
	public boolean paused = false;
	public boolean autoPause = true;
	public String autoPauseStr;
	public int score = 0;
	public int zenScore = 0;
	public int zenTotal = 0;
	public int missedDrops = 0;
	public boolean showMissedDrops = false;
	public long highScore;
	public long zenHighScore;
	public boolean showZenScores = false;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;
	public AssetManager assManager;
	public String callToAction = "Touch or Click";
	public Preferences gamePrefs;
	public boolean usingDesktop;
	public boolean noDrag;
	public String dragStr;
	public String gameModeStr;
	public int timerTime;
	public boolean spawnDrops = true;

	public boolean initedSettings = false;
	
	public CDGame() {
		this.callToAction = "Touch or Click";
		this.usingDesktop = true;
		this.noDrag  = true;
	}
	
	public CDGame(String callToAction) {
		this.callToAction = callToAction;
		this.usingDesktop = true;
		this.noDrag  = true;
	}
	
	public CDGame(String callToAction, boolean desktop) {
		this.callToAction = callToAction;
		this.usingDesktop = desktop;
		this.noDrag  = desktop;
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
		
		FreeTypeFontLoaderParameter timerParams = new FreeTypeFontLoaderParameter();
		timerParams.fontFileName = "font/arial.ttf";
		timerParams.fontParameters.size = 45;
		assManager.load("timer.ttf", BitmapFont.class, timerParams);
		
		FreeTypeFontLoaderParameter goverParams = new FreeTypeFontLoaderParameter();
		goverParams.fontFileName = "font/arial.ttf";
		goverParams.fontParameters.size = 85;
		assManager.load("gover.ttf", BitmapFont.class, goverParams);
		
		FreeTypeFontLoaderParameter versionParams = new FreeTypeFontLoaderParameter();
		versionParams.fontFileName = "font/arial.ttf";
		versionParams.fontParameters.size = 20;
		assManager.load("corner.ttf", BitmapFont.class, versionParams);
		assManager.load("size20.ttf", BitmapFont.class, versionParams);
		
		FreeTypeFontLoaderParameter headerParams = new FreeTypeFontLoaderParameter();
		headerParams.fontFileName = "font/arial.ttf";
		headerParams.fontParameters.size = 50;
		assManager.load("heading.ttf", BitmapFont.class, headerParams); 
		
		if(usingDesktop) autoPause = false;
		
		gamePrefs = Gdx.app.getPreferences("Game");
		//gamePrefs.getLong("highscore", 0);
		
		batch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		
		mMScr = new MainMenuScreen(this);
		gScr = new ClassicGame(this);
		//gSettings = new SettingsScreen(this);
		
		this.setScreen(mMScr);
	}

	@Override
	public void render() {
		if(autoPause) autoPauseStr = "ON"; else autoPauseStr = "OFF";
		if(noDrag) dragStr = "OFF"; else dragStr = "ON";
		if(autoPause) noDrag = false;
		if(gScr instanceof ClassicGame) {
			gameModeStr = "Classic";
		} else if(gScr instanceof ZenGame) {
			gameModeStr = "Zen";
		} else {
			gameModeStr = "Classic?";
		}
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
		gamePrefs.putLong("highscore", highscore);
		highScore = highscore;
		gamePrefs.flush();
	}
	
	public long getZenHighscore() {
		return gamePrefs.getLong("zen-highscore", 0);
	}
	
	public void setZenHighscore(long highscore) {
		gamePrefs.putLong("zen-highscore", highscore);
		zenHighScore = highscore;
		gamePrefs.flush();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		assManager.dispose();
	}
	
	public String secondsToTime(int insec, boolean includeHours) {
		int hours, minutes, seconds;
		String returned;
		hours = insec / 3600;
		minutes = (insec / 60) % 60;
		seconds = insec % 60;
		if(includeHours) returned = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		else returned = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		return returned;
	}
}










