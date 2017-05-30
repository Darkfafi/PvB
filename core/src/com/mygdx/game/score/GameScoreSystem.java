package com.mygdx.game.score;

/**
 * This class is a singleton which handles game specific score handling.
 * @author Ramses Di Perna
 *
 */
public class GameScoreSystem extends ScoreSystem
{
	private static GameScoreSystem _instance;
	
	public static GameScoreSystem getInstance()
	{
		if(_instance == null)
			_instance = new GameScoreSystem();
		return _instance;
	}
}
