package projects.nerdybuzz.catchdrop.googleservices;

public class DesktopGoogleServices implements IGoogleServices {

	@Override
	public void signIn() {
		System.out.println("DesktopGoogleServies: signIn()");
	}

	@Override
	public void signOut() {
		System.out.println("DesktopGoogleServies: signOut()");
	}

	@Override
	public void rateGame() {
		System.out.println("DesktopGoogleServies: rateGame()");
	}

	@Override
	public void showClassicScores() {
		System.out.println("DesktopGoogleServies: showClassicScores()");
	}
	
	@Override
	public void showZenScores() {
		System.out.println("DesktopGoogleServies: showZenScores()");
	}

	@Override
	public boolean isSignedIn() {
		System.out.println("DesktopGoogleServies: isSignedIn()");
		return false;
	}

	@Override
	public void submitClassicScore(long score) {
		System.out.println("DesktopGoogleServies: submitClassicScore("+score+")");
	}

	@Override
	public void submitZenScore(long score) {
		System.out.println("DesktopGoogleServies: submitZenScore("+score+")");
	}

	@Override
	public void showAchievements() {
		System.out.println("DesktopGoogleServies: showAchievements()");
	}

	@Override
	public void unlockAchievement(String achievement_id) {
		System.out.println("DesktopGoogleServies: unlockAchievement("+achievement_id+")");
	}
}
