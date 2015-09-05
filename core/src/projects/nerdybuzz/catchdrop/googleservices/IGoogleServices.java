package projects.nerdybuzz.catchdrop.googleservices;

public interface IGoogleServices {
	public void signIn();
	public void signOut();
	public void rateGame();
	public void submitClassicScore(long score);
	public void submitZenScore(long score);
	public void showClassicScores();
	public void showZenScores();
	public void showAchievements();
	public void unlockAchievement(String achievement_id);
	public boolean isSignedIn();
}