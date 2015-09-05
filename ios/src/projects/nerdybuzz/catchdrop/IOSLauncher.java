package projects.nerdybuzz.catchdrop;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIGraphics;
import org.robovm.apple.uikit.UIScreen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import projects.nerdybuzz.catchdrop.googleservices.DesktopGoogleServices;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
    	String deviceName = UIDevice.getCurrentDevice().getModel();
    	double screenWidth = UIScreen.getMainScreen().getBounds().getWidth();
    	double screenHeight = UIScreen.getMainScreen().getBounds().getHeight();
    	System.out.println("Device Model: " + deviceName);
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new CDGame(new DesktopGoogleServices(), "Touch", false, (int)screenWidth, (int)screenHeight), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}