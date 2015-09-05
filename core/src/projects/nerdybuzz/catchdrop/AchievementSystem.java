package projects.nerdybuzz.catchdrop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.utils.TimeUtils;

public class AchievementSystem {
	private static int timeCounter = 3000;
	private static int effectTimer = 1000;
	
	public static String[][] achievements = {
			//{"Title", "Description"},
			{"New Game", "Start a new game!"},
			
			{"Fifty Nifty", "Catch 50 drops."},
			{"Ten Tens", "Catch 100 drops."},
			{"On a roll", "Catch 200 drops."},
			{"Quantitative Water", "Catch 300 drops."},
			{"Are you human?", "Catch 500 drops."},
			{"Get a life!", "Catch a thousand drops."},
			{"IT'S OVER 9000!!!", "Catch over nine-thousand drops."},
			
			{"So... Thirsty...", "Catch no drops."},
			
			{"It's Poisoned!", "Get poisoned from a poison drop."},
			
			{"Got my toast!", "Catch the burnt toast."}
	};
	private static boolean achivementUnlocked;
	private static String achivementTitle = "";
	private static String achivementDesc = "";
	private static long nowTimeInMillis;
	private static long lastTimeInMillis;
	
	
	public static void giveAchievement(String id, int index) {
		CDGame.googleServices.unlockAchievement(id);
		achivementUnlocked = true;
		achivementTitle = achievements[index][0];
		achivementDesc = achievements[index][1];
	}
	
	// This is kinda messed up...
	/*
	public static void drawAchivement(CDGame game, BitmapFont headingfont, BitmapFont titlefont) {
		TextBounds heading;
		TextBounds title;
		if(achivementUnlocked) {
			heading = headingfont.draw(game.batch, "Achivement Unlocked!", game.GAME_WIDTH/2, slideInEffect(0));
			title = titlefont.draw(game.batch, achivementTitle, game.GAME_WIDTH/2, slideInEffect(10));
			nowTimeInMillis = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
			if(lastTimeInMillis != nowTimeInMillis && !(timeCounter <= 0)) {
				timeCounter--;
				lastTimeInMillis = nowTimeInMillis;
			}
			if(timeCounter <= 0) {
				heading.set(null);
				title.set(null);
			}
		}
		
	}
	// */
	
	public static float slideInEffect(float yDisplacement) {
		float y = 0;
		nowTimeInMillis = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
		if(lastTimeInMillis != nowTimeInMillis && !(effectTimer <= 0)) {
			effectTimer--;
			y+=0.1;
			lastTimeInMillis = nowTimeInMillis;
		}
		if(effectTimer <= 0) {
			y = 20 + yDisplacement;
		}
		return y;
	}
	
	public float slideOutEffect(float yDisplacement) {
		float y = 0;
		nowTimeInMillis = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
		if(lastTimeInMillis != nowTimeInMillis && !(effectTimer <= 0)) {
			effectTimer--;
			y-=0.1;
			lastTimeInMillis = nowTimeInMillis;
		}
		if(effectTimer <= 0) {
			y = 20 + yDisplacement;
		}
		return y;
	}
}
