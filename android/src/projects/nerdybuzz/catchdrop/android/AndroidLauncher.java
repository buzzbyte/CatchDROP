package projects.nerdybuzz.catchdrop.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import projects.nerdybuzz.catchdrop.CDGame;
import projects.nerdybuzz.catchdrop.googleservices.IGoogleServices;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.*;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class AndroidLauncher extends AndroidApplication implements IGoogleServices {
	private GameHelper _gameHelper;
	private final static int REQUEST_CODE_UNUSED = 9002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		_gameHelper.enableDebugLog(false);

		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInSucceeded() {
			}
	
			@Override
			public void onSignInFailed() {
			}
		};

		_gameHelper.setup(gameHelperListener);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new CDGame(this, "Touch", false), config);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				//@Override
				public void run() {
					_gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidLauncher", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				//@Override
				public void run() {
					_gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("AndroidLauncher", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		String str ="https://play.google.com/store/apps/details?id=projects.nerdybuzz.catchdrop";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitClassicScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(_gameHelper.getApiClient(), getString(R.string.chs_leaderboard_id), score);
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.chs_leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to submitting score?
		}
	}
	
	@Override
	public void submitZenScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(_gameHelper.getApiClient(), getString(R.string.zhs_leaderboard_id), score);
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.zhs_leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to submitting score?
		}
	}

	@Override
	public void showClassicScores() {
		if (isSignedIn() == true)
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.chs_leaderboard_id)), REQUEST_CODE_UNUSED);
		else {
			signIn();
			if (isSignedIn() == true)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.chs_leaderboard_id)), REQUEST_CODE_UNUSED);
		}
	}
	
	@Override
	public void showZenScores() {
		if (isSignedIn() == true)
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.zhs_leaderboard_id)), REQUEST_CODE_UNUSED);
		else {
			signIn();
			if (isSignedIn() == true)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.zhs_leaderboard_id)), REQUEST_CODE_UNUSED);
		}
	}

	@Override
	public boolean isSignedIn() {
		return _gameHelper.isSignedIn();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		_gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		_gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		_gameHelper.onActivityResult(requestCode, resultCode, data);
	}
}
