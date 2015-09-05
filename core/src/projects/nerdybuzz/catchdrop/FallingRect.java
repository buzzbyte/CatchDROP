package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class FallingRect extends Rectangle implements FallingAct {
	private static final long serialVersionUID = 1L;
	
	final CDGame game;
	
	public boolean loseOnMiss = false;
	public int gainValue = 0;
	public int loseValue = 0;
	
	public Texture rectImg;
	
	public FallingRect(final CDGame game) {
		this.game = game;
	}
	
	public FallingRect(final CDGame game, float x, float y, float width, float height) {
		super(x,y,width,height);
		this.game = game;
	}
	
	public FallingRect(final CDGame game, float x, float y, float width, float height, boolean loseOnMiss, int gainValue, int loseValue) {
		super(x,y,width,height);
		this.game = game;
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
	
	public FallingRect(final CDGame game, float x, float y, float width, float height, boolean loseOnMiss, int gainValue, int loseValue, Texture rectImg) {
		super(x,y,width,height);
		this.game = game;
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
		setImg(rectImg);
	}
	
	public FallingRect(final CDGame game, boolean loseOnMiss, int gainValue, int loseValue) {
		this.game = game;
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
	
	public void set(boolean loseOnMiss, int gainValue, int loseValue) {
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
	
	public void set(boolean loseOnMiss, int gainValue, int loseValue, Texture rectImg) {
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
		setImg(rectImg);
	}
	
	public void setImg(Texture rectImg) {
		this.rectImg = rectImg;
	}
	
	public void setImg(String rectImg) {
		this.rectImg = new Texture(rectImg);
	}
	
	public Texture getImg() {
		return rectImg;
	}
	
	public FallingAct action;

	@Override
	public void onMiss() {
		game.missedDrops++;
	}

	@Override
	public void onGet() {
		game.score -= loseValue;
		game.score += gainValue;
	}

	@Override
	public void onSpawn() {
		
	}

	@Override
	public void onDispose() {
		this.dispose();
	}

	public void dispose() {
		rectImg.dispose();
	}
}









