package projects.nerdybuzz.catchdrop.desktop;

import projects.nerdybuzz.catchdrop.CDGame;
import projects.nerdybuzz.catchdrop.googleservices.DesktopGoogleServices;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class AppletLauncher extends LwjglApplet {
	private static final long serialVersionUID = 1L;
	
	static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration() {
		{
		width = 800;
		height = 480;
		}
	};

	public AppletLauncher() {
		super(new CDGame(new DesktopGoogleServices(), "Click"), config);
	}
	
	public static void main(String[] args) {
		new AppletLauncher();
	}
}
