package projects.nerdybuzz.catchdrop;

import projects.nerdybuzz.catchdrop.googleservices.IGoogleServices;

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
	//public static final String GAME_VERSION = "v0.4.3 (alpha)";
	public static final String GAME_VERSION = "testing-alpha";
	
	public int GAME_WIDTH  = 800;
	public int GAME_HEIGHT = 480;
	
	//public int GAME_WIDTH  = 1024;
	//public int GAME_HEIGHT = 768;
	
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

	public CharSequence classicText = "Classic";
	public CharSequence zenText = "Zen";
	
	public static IGoogleServices googleServices;
	
	public CDGame(IGoogleServices googleServices) {
		CDGame.googleServices = googleServices;
		this.callToAction = "Touch or Click";
		this.usingDesktop = true;
		this.noDrag  = true;
	}
	
	public CDGame(IGoogleServices googleServices, String callToAction) {
		CDGame.googleServices = googleServices;
		this.callToAction = callToAction;
		this.usingDesktop = true;
		this.noDrag  = true;
	}
	
	public CDGame(IGoogleServices googleServices, String callToAction, boolean desktop) {
		CDGame.googleServices = googleServices;
		this.callToAction = callToAction;
		this.usingDesktop = desktop;
		this.noDrag  = desktop;
	}
	
	public CDGame(IGoogleServices googleServices, int gwidth, int gheight) {
		CDGame.googleServices = googleServices;
		this.callToAction = "Touch or Click";
		this.usingDesktop = true;
		this.noDrag  = true;
		this.GAME_WIDTH  = gwidth;
		this.GAME_HEIGHT = gheight;
	}
	
	public CDGame(IGoogleServices googleServices, String callToAction, int gwidth, int gheight) {
		CDGame.googleServices = googleServices;
		this.callToAction = callToAction;
		this.usingDesktop = true;
		this.noDrag  = true;
		this.GAME_WIDTH  = gwidth;
		this.GAME_HEIGHT = gheight;
	}
	
	public CDGame(IGoogleServices googleServices, String callToAction, boolean desktop, int gwidth, int gheight) {
		CDGame.googleServices = googleServices;
		this.callToAction = callToAction;
		this.usingDesktop = desktop;
		this.noDrag  = desktop;
		this.GAME_WIDTH  = gwidth;
		this.GAME_HEIGHT = gheight;
	}
	
	public void create() {
		assManager = new AssetManager(); // I manage dat ass...
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter titleParams = new FreeTypeFontLoaderParameter();
		titleParams.fontFileName = "font/atari.ttf";
		titleParams.fontParameters.size = 75;
		assManager.load("title.ttf", BitmapFont.class, titleParams);
		
		FreeTypeFontLoaderParameter promptParams = new FreeTypeFontLoaderParameter();
		promptParams.fontFileName = "font/MyriadPro-Regular.otf";
		promptParams.fontParameters.size = 35;
		assManager.load("prompt.ttf", BitmapFont.class, promptParams);
		
		FreeTypeFontLoaderParameter promptBoldParams = new FreeTypeFontLoaderParameter();
		promptBoldParams.fontFileName = "font/MyriadPro-Bold.otf";
		promptBoldParams.fontParameters.size = 35;
		assManager.load("prompt_bold.ttf", BitmapFont.class, promptBoldParams);
		
		FreeTypeFontLoaderParameter scoreParams1 = new FreeTypeFontLoaderParameter();
		scoreParams1.fontFileName = "font/atari.ttf";
		scoreParams1.fontParameters.size = 20;
		assManager.load("score.ttf", BitmapFont.class, scoreParams1);
		
		FreeTypeFontLoaderParameter timerParams = new FreeTypeFontLoaderParameter();
		timerParams.fontFileName = "font/atari.ttf";
		timerParams.fontParameters.size = 25;
		assManager.load("timer.ttf", BitmapFont.class, timerParams);
		
		FreeTypeFontLoaderParameter goverParams = new FreeTypeFontLoaderParameter();
		goverParams.fontFileName = "font/MyriadPro-Bold.otf";
		goverParams.fontParameters.size = 95;
		assManager.load("gover.ttf", BitmapFont.class, goverParams);
		
		FreeTypeFontLoaderParameter versionParams = new FreeTypeFontLoaderParameter();
		versionParams.fontFileName = "font/prstartk.ttf";
		versionParams.fontParameters.size = 15;
		assManager.load("corner.ttf", BitmapFont.class, versionParams);
		
		FreeTypeFontLoaderParameter menuFontParams = new FreeTypeFontLoaderParameter();
		menuFontParams.fontFileName = "font/MyriadPro-Bold.otf";
		menuFontParams.fontParameters.size = 25;
		assManager.load("size20.ttf", BitmapFont.class, menuFontParams);
		
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
		//gScr = new GameScreen(this);
		//gScr = new ZenGame(this);
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
		googleServices.submitClassicScore(highscore);
	}
	
	public long getZenHighscore() {
		return gamePrefs.getLong("zen-highscore", 0);
	}
	
	public void setZenHighscore(long highscore) {
		gamePrefs.putLong("zen-highscore", highscore);
		zenHighScore = highscore;
		gamePrefs.flush();
		googleServices.submitZenScore(highscore);
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










