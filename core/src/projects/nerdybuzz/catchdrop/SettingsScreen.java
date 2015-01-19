package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsScreen implements Screen {
	final CDGame game;
	private OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	
	private BitmapFont headerFont;
	private BitmapFont btnFont;
	
	private TextButton autoPauseSetting;
	private TextButton dragSetting;
	
	private Table table;
	private Texture pixtexture;
	private Pixmap pixmap;
	
	private Screen backScreen;
	private boolean shown = false;
	private TextButton gameModeSetting;
	
	private int gmIndex = 0;
	private int count;
	
	public SettingsScreen(final CDGame game) {
		this.game = game;
		this.backScreen = game.mMScr;
		init(game);
	}
	
	public SettingsScreen(final CDGame game, Screen backScreen) {
		this.game = game;
		this.backScreen = backScreen;
		init(game);
	}
	
	public void init(final CDGame game) {
		camera = game.camera;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		headerFont = game.assManager.get("heading.ttf", BitmapFont.class);
		btnFont = game.assManager.get("size20.ttf", BitmapFont.class);
		
		skin = new Skin();
		pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixtexture = new Texture(pixmap);
		skin.add("white", pixtexture);
		skin.add("heading", headerFont);
		skin.add("normalfont", btnFont);
		skin.add("btnFontColor", Color.BLACK);
		skin.add("default", new BitmapFont());
		
		LabelStyle lblStyle = new LabelStyle();
		lblStyle.font = skin.getFont("heading");
		skin.add("default", lblStyle);
		
		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = skin.newDrawable("white", Color.GRAY);
		btnStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		btnStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		btnStyle.checked = skin.newDrawable("white", Color.WHITE);
		btnStyle.font = skin.getFont("normalfont");
		btnStyle.fontColor = skin.getColor("btnFontColor");
		skin.add("default", btnStyle);
		
		Label scrTitle = new Label("Game Settings", skin);
		gameModeSetting = new TextButton("Game Mode: "+game.gameModeStr,skin);
		autoPauseSetting = new TextButton("Auto-Pause: "+game.autoPauseStr,skin);
		dragSetting = new TextButton("Dragging: "+game.dragStr,skin);
		if(game.autoPause) autoPauseSetting.setChecked(true); else autoPauseSetting.setChecked(false);
		if(game.noDrag) dragSetting.setChecked(false); else dragSetting.setChecked(true);
		TextButton showCHSBtn = new TextButton("Classic Highscores", skin);
		TextButton showZHSBtn = new TextButton("Zen Highscores", skin);
		TextButton backBtn = new TextButton("Back", skin);
		
		gameModeSetting.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) {
					/*
					System.out.println("Setting Clicked!");
					GameScreen[] gameModes = {
							new ZenGame(game),
							new ClassicGame(game)
					};
					
					if(gmIndex>gameModes.length-1) {
						gmIndex = 0;
					}
					game.gScr = gameModes[gmIndex];
					gmIndex++;
					
					System.out.println("Game mode index: "+gmIndex);
					System.out.println("Game mode: "+game.gameModeStr);
					System.out.println("Game mode: "+game.gScr);
					// */
				}
			}
		});
		
		autoPauseSetting.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) {
					System.out.println("Setting Clicked!");
					if(game.autoPause) {
						game.autoPause = false;
					} else {
						game.autoPause = true;
					}
				}
			}
		});
		
		dragSetting.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) {
					System.out.println("Setting Clicked!");
					if(game.noDrag) {
						game.noDrag = false;
					} else {
						game.noDrag = true;
					}
				}
			}
		});
		
		backBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) game.setScreen(backScreen);
			}
		});
		
		showCHSBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) CDGame.googleServices.showClassicScores();
			}
		});
		
		showZHSBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(shown) CDGame.googleServices.showZenScores();
			}
		});
		
		table = new Table();
		table.add(scrTitle).pad(10);
		table.row();
		//table.add(gameModeSetting).width(300).height(50).pad(5);
		table.row();
		if(game.usingDesktop) {
			table.add(autoPauseSetting).width(300).height(50).pad(5);
			table.row();
			table.add(dragSetting).width(300).height(50).pad(5);
			table.row();
		}
		table.add(showCHSBtn).width(300).height(50).pad(5);
		table.row();
		table.add(showZHSBtn).width(300).height(50).pad(5);
		table.row();
		table.add(backBtn).width(300).height(50).pad(5);
		table.setPosition(game.GAME_WIDTH/2, game.GAME_HEIGHT/2);
		stage.addActor(table);
		
		game.initedSettings = true;
	}

	@Override
	public void render(float delta) {
		if(shown) {
			Gdx.gl.glClearColor(0, 0, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
			stage.draw();
			
			update(delta);
		}
	}
	
	public void update(float delta) {
		gameModeSetting.setText("Game Mode: "+game.gameModeStr);
		autoPauseSetting.setText("Auto-Pause: "+game.autoPauseStr);
		dragSetting.setText("Dragging: "+game.dragStr);
		
		if(game.autoPause) autoPauseSetting.setChecked(true); else autoPauseSetting.setChecked(false);
		if(game.noDrag) dragSetting.setChecked(false); else dragSetting.setChecked(true);
		
		
		if(Gdx.input.isKeyJustPressed(Keys.P)) {
			toggleAutoPause();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			toggleDragging();
		}
		// */
		
		/*
		if(shown) {
			System.out.println("Update! #"+count);
			count++;
		}
		// */
		
		camera.update();
	}
	
	public void toggleAutoPause() {
		if(game.autoPause) autoPauseSetting.setChecked(false); else autoPauseSetting.setChecked(true);
	}
	
	public void toggleDragging() {
		if(game.noDrag) dragSetting.setChecked(false); else dragSetting.setChecked(true);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		shown = true;
		System.out.println("Settings Shown.");
	}

	@Override
	public void hide() {
		shown = false;
		System.out.println("Settings Hidden.");
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		skin.dispose();
		stage.dispose();
	}

}
