package com.mygdx.game.score;

import com.mygdx.game.engine.entities.components.BaseEntityComponent;

public class ScoreHolderComponent extends BaseEntityComponent 
{
	public enum ScoreGainType
	{
		Destroy,
		Hit
	}
	
	private int _scoreWorth;
	private ScoreGainType _gainType;
	
	public ScoreHolderComponent(ScoreGainType scoreGainType, int scoreWorth)
	{
		_scoreWorth = scoreWorth;
		_gainType = scoreGainType;
	}
	
	public int getScoreWorth()
	{
		return _scoreWorth;
	}
	
	public ScoreGainType getScoreGainType()
	{
		return _gainType;
	}
	
	public void setScoreWorth(int value)
	{
		_scoreWorth = value;
	}

	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updated(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub
		
	}
}
