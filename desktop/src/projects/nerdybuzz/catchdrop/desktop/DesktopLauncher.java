package projects.nerdybuzz.catchdrop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import projects.nerdybuzz.catchdrop.CDGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CatchDROP";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CDGame("Click"), config);
	}
}
