package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.math.Rectangle;

public class FallingRect extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public boolean loseOnMiss = false;
	public int gainValue = 0;
	public int loseValue = 0;
	
	public FallingRect() {}
	
	public FallingRect(float x, float y, float width, float height) {
		super(x,y,width,height);
	}
	
	public FallingRect(float x, float y, float width, float height, boolean loseOnMiss, int gainValue, int loseValue) {
		super(x,y,width,height);
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
	
	public FallingRect(boolean loseOnMiss, int gainValue, int loseValue) {
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
	
	public void set(boolean loseOnMiss, int gainValue, int loseValue) {
		this.loseOnMiss = loseOnMiss;
		this.gainValue = gainValue;
		this.loseValue = loseValue;
	}
}
