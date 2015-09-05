package projects.nerdybuzz.catchdrop.desktop;

import projects.nerdybuzz.catchdrop.CDGame;
import projects.nerdybuzz.catchdrop.googleservices.DesktopGoogleServices;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CatchDROP";
		config.width = 800;
		config.height = 480;
		config.addIcon("drop-16x16-icon.png", Files.FileType.Internal);
		config.addIcon("bucket-32x32-icon.png", Files.FileType.Internal);
		new LwjglApplication(new CDGame(new DesktopGoogleServices(), "Click"), config);
	}
}
