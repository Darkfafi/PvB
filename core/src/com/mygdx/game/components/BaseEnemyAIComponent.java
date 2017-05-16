package com.mygdx.game.components;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Playfield;

/**
 * This is the Base class for all the AI Components. This component holds info on the field and handles navigation to the player base.
 * NOTE: This Component can only be attached to an entity of the class 'Enemy', else this component will give a runtime error.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseEnemyAIComponent extends BaseGridNavigationComponent 
{
	private float _movementSpeed;
	private Playfield _playfield;
	private Enemy _enemy;
	private Vector2 _currentTargetPosition = null;
	private int _sideStepChance = 20;
	
	/**
	 * Setting up the AI requires the following components
	 * (Use the method 'setSideStepChance' to also set the chance of the AI performing a side step action)
	 * @param playfield on which the AI will interact with
	 * @param movementSpeed with which speed the AI will move the Enemy Body
	 * @param unwalkableTags are tags this particular AI instance can't walk over (A.k.a. will avoid)
	 */
	public BaseEnemyAIComponent(Playfield playfield, float movementSpeed, String[] unwalkableTags)
	{
		super(playfield.getGrid(), GridTags.OCCUPY_TAG_ENEMY, 1, 1);
		
		for(int i = 0; i < unwalkableTags.length; i++)
		{
			this.addUnwalkableOccupyTag(unwalkableTags[i]);
		}
		
		_movementSpeed = movementSpeed;
		_playfield = playfield;
	}
	
	/**
	 * Sets the chance of this AI choosing to try a side step. (value between 0 - 100)
	 * @param percentage between 0 and 100.
	 */
	public void setSideStepChance(int percentage)
	{
		if(percentage < 0)
			percentage = 0;
		
		if(percentage > 100)
			percentage = 100;
		
		_sideStepChance = percentage;
	}
	
	/**
	 * Gets the chance of this AI choosing to try a side step
	 * @return The percentage which it is able to side step (between 0 and 100)
	 */
	public int getSideStepChance()
	{
		return _sideStepChance;
	}
	
	@Override
	public void awake()
	{
		super.awake();
		_enemy = (Enemy)this.getParentOfComponent();
	}

	@Override
	protected void destroyed() 
	{
		_enemy.stopAction(false);
		_playfield = null;
		_enemy = null;
		_currentTargetPosition = null;
		super.destroyed();
	}
	
	/**
	 * Returns the movement speed set for this AI Component.
	 * @return A float which represents the movement speed of this Enemy AI
	 */
	protected float getMovementSpeed()
	{
		return _movementSpeed;
	}
	
	/**
	 * Returns the Playfield which was given to this component to be orientated on.
	 * @return The playfield given in its constructor.
	 */
	protected Playfield getPlayfield()
	{
		return _playfield;
	}
	
	/**
	 * Returns the Enemy which this component is attached to
	 * @return The enemy this component is attached to.
	 */
	protected Enemy getAffectedEnemy()
	{
		return _enemy;
	}
	
	/**
	 * This method will cause the enemy this component is attached to, to move up until the given y row to stop moving. (Moving downwards)
	 * NOTE: This should be called every frame in order to keep processing. 
	 * @param yRowToStop is the Y row in grid space to stop moving.
	 */
	protected void moveToPlayerBase(int yRowToStop)
	{
		if(!this.isLocated()) { return; }
		
		if(_currentTargetPosition == null)
		{
			_currentTargetPosition = getTargetPosition();
		}
		
		if(_currentTargetPosition == null) { return; }
		
		if(_enemy.getEnemyState() != Enemy.EnemyState.WalkState)
		{
			if(this.getLocationY() < yRowToStop)
			{
				Vector2 newTileLocation = getNewTargetTile(_sideStepChance);
				
				if(newTileLocation == null) 
				{ 
					_enemy.stopAction(true);
					return;
				}
				
				this.placeSelfOnLocation((int)newTileLocation.x,(int)newTileLocation.y);
				_currentTargetPosition = getTargetPosition();
				_enemy.move(_currentTargetPosition.x, _currentTargetPosition.y, _movementSpeed, false);
			}
			
			_currentTargetPosition = null;
		}
	}
}
