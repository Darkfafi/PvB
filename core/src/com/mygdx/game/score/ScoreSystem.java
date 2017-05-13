package com.mygdx.game.score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.globals.PreferencesGlobals;

/**
 * This a class which can be used to track score and save / load highscore data.
 * @author Ramses Di Perna
 *
 */
public class ScoreSystem 
{	
	private static int DEFAULT_SCORE = 0;
	private static int DEFAULT_MULTIPLIER = 1;
	
	private int _currentScore;
	private int _currentMultiplier;
	
	private int _bestScore;
	private int _bestMultiplier;
	
	private Preferences _scorePrefs; 
	
	/**
	 * Instantiates the ScoreSystem, loads the last best score data and starts a new score session 
	 */
	public ScoreSystem()
	{
		_scorePrefs = Gdx.app.getPreferences(PreferencesGlobals.PREFERENCES_SCORE);
		loadBestData();
		startNewScoreSession();
	}
	/**
	 * Adds Score to the current score session. If the current score reaches higher than the best score, it becomes the best score (Not saved)
	 * @param score to add * the current multiplier to the current score.
	 * @return real score added
	 */
	public int addScore(int score)
	{
		int scoreToAdd = score * _currentMultiplier;
		
		_currentScore += scoreToAdd;
		if(_currentScore > _bestScore)
		{
			_bestScore = _currentScore;
		}
		
		return scoreToAdd;
	}
	
	/**
	 * Subtract the given score value from the current score.
	 * @param score to subtract * the current multiplier from the current score.
	 * @return real score subtracted
	 */
	public int subScore(int score)
	{
		int scoreToSubtract = score * _currentMultiplier;
		_currentScore -= scoreToSubtract;
		return scoreToSubtract;
	}
	
	/**
	 * Gets the value of the current score multiplier.
	 * @return The value of the current multiplier (DEFAULT: 1)
	 */
	public int getMultiplier()
	{
		return _currentMultiplier;
	}
	
	/**
	 * Sets the value of the current score multiplier
	 * If the current multiplier > than the saved best multiplier, it becomes the new best multiplier
	 * @param value to set the current multiplier to.
	 */
	public void setMultiplier(int value)
	{
		_currentMultiplier = value;
		
		if(_currentMultiplier > _bestMultiplier)
		{
			_bestMultiplier = _currentMultiplier;
		}
	}
	
	/**
	 * Gets the current score.
	 * @return The value of the current score.
	 */
	public int getScore()
	{
		return _currentScore;
	}
	
	/**
	 * Gets the best score session. 
	 * If the current score > than the saved best score, it becomes the new one and thus is returned by this method
	 * @return The best score session. 
	 */
	public int getBestScore()
	{
		return _bestScore;
	}
	
	/**
	 * Gets the best multiplier session
	 * If the current multiplier > than the saved best multiplier, it becomes the new one and thus is returned by this method
	 * @return The best multiplier session 
	 */
	public int getBestMultiplier()
	{
		return _bestMultiplier;
	}
	
	/**
	 * Ends the current score session and resets all current score values.
	 * @param saveIfBest saves the best score data into the device if it was reached by this session
	 */
	public void endScoreSession(boolean saveIfBest)
	{
		if(isBestSession() && saveIfBest)
			saveBestData();
		
		startNewScoreSession();
	}
	
	public boolean isBestSession()
	{
		return _currentScore == _bestScore;
	}
	
	/**
	 * Resets all score tracking values.
	 */
	private void startNewScoreSession()
	{
		_currentScore = DEFAULT_SCORE;
		_currentMultiplier = DEFAULT_MULTIPLIER;
	}
	
	/**
	 * Resets the device saved data. Call the 'loadBestData' to reset the data loaded before calling this method also.
	 */
	public void resetBestData()
	{
		insideSaveData(DEFAULT_SCORE, DEFAULT_MULTIPLIER);
	}
	
	/**
	 * Loads the best score and multiplier data into the current session from the current device.
	 */
	public void loadBestData() 
	{
		_bestScore = _scorePrefs.getInteger(PreferencesGlobals.PREF_KEY_INT_BEST_SCORE, DEFAULT_SCORE);
		_bestMultiplier = _scorePrefs.getInteger(PreferencesGlobals.PREF_KEY_INT_BEST_MULTIPLIER, DEFAULT_MULTIPLIER);
	}

	/**
	 * Saves the current BestScore and BestMultiplier data into the current device.
	 */
	public void saveBestData() 
	{
		insideSaveData(_bestScore, _bestMultiplier);
	}
	
	/**
	 * Saves the data internally and 'flushes' it into the computer
	 * @param score to save as best score
	 * @param multiplier to save as best multiplier
	 */
	private void insideSaveData(int score, int multiplier)
	{
		_scorePrefs.putInteger(PreferencesGlobals.PREF_KEY_INT_BEST_SCORE, score);
		_scorePrefs.putInteger(PreferencesGlobals.PREF_KEY_INT_BEST_MULTIPLIER, multiplier);
		_scorePrefs.flush();
	}
}
