package com.mygdx.game.components;

import com.mygdx.game.level.Playfield;

/**
 * This component should be added to an enemy have its body be controlled by an AI system.
 * This will contain various basic tasks as navigation and attacking the player base.
 * @author Ramses Di Perna
 *
 */
public class BasicEnemyAIComponent extends BaseEnemyAIComponent 
{
	public BasicEnemyAIComponent(Playfield playfield, float movementSpeed, String[] unwalkableTags) 
	{
		super(playfield, movementSpeed, unwalkableTags);
	}

	@Override
	protected void updated(float dt)
	{
		super.updated(dt);
		basicAILogics();
	}
	
	/**
	 * Performs the basic AI logics (Navigating on and waiting for walkable tiles and attacking when reached the end)
	 */
	private void basicAILogics() 
	{
		int yLine = this.getGrid().getTileAmountY() - this.getAffectedEnemy().getAttackRange();
		
		moveToPlayerBase(yLine);
		
		if(this.getLocationY() >= yLine)
		{
			this.getAffectedEnemy().attack(this.getPlayfield().getPlayerBase().getComponent(HealthComponent.class));
		}
	}
}
