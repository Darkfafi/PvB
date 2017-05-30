package com.mygdx.game.score;

import com.mygdx.game.engine.entities.components.BaseEntityComponent;

/**
 * This component can be added to an entity to represent it as something which can be used to gain score by the player
 * @author Ramses Di Perna
 *
 */
public class ScoreHolderComponent extends BaseEntityComponent 
{
	/**
	 * The score gain types for the game
	 * @author Ramses Di Perna
	 *
	 */
	public enum ScoreGainType
	{
		/**
		 * This entity represents the gaining of its score should be done after it being destroyed
		 */
		Destroy,
		/**
		 * This entity represents the gaining of its score must be done after it being hit
		 */
		Hit
	}
	
	private int _scoreWorth;
	private ScoreGainType _gainType;
	
	/**
	 * A score component has the have the following information to work with
	 * @param scoreGainType is what the component represents the gaining of score should be
	 * @param scoreWorth is how much score this entity represents. (How much to gain after the action the type represents has been done)
	 */
	public ScoreHolderComponent(ScoreGainType scoreGainType, int scoreWorth)
	{
		setScoreWorth(scoreWorth);
		_gainType = scoreGainType;
	}
	
	/**
	 * Returns the score value of this entity
	 * @return the score worth given in the constructor as integer.
	 */
	public int getScoreWorth()
	{
		return _scoreWorth;
	}
	
	/**
	 * Returns the score gaining type of this entity
	 * @return the score gaining type given in the constructor.
	 */
	public ScoreGainType getScoreGainType()
	{
		return _gainType;
	}
	
	/**
	 * Sets the score worth of this component.
	 * @param value to set the score worth to.
	 */
	public void setScoreWorth(int value)
	{
		_scoreWorth = value;
	}

	@Override
	public void awake() 
	{
		
	}

	@Override
	protected void updated(float deltaTime) 
	{
		
	}

	@Override
	protected void destroyed() 
	{	
	}

	@Override
	protected void activeStateChanged() 
	{
		
	}
}
