package com.mygdx.game.globals;

import com.badlogic.gdx.Gdx;

public class PreferencesGlobals 
{
	// Game
	public static final String PREFERENCES_GAME = "GamePreferences";
	public static final String PREF_KEY_BOOLEAN_TUTORIAL_DONE = "TutorialDonePrefBooleanKey";
	
	// Score
	public static final String PREFERENCES_SCORE = "ScorePreferences";
	public static final String PREF_KEY_INT_BEST_SCORE = "BestScorePrefIntKey";
	public static final String PREF_KEY_INT_BEST_MULTIPLIER = "BestMultiplierPrefIntKey";
	
	
	public void resetAllSaveData()
	{
		Gdx.app.getPreferences(PREFERENCES_GAME).clear();
		Gdx.app.getPreferences(PREFERENCES_GAME).flush();
		

		Gdx.app.getPreferences(PREFERENCES_SCORE).clear();
		Gdx.app.getPreferences(PREFERENCES_SCORE).flush();
	}
}
