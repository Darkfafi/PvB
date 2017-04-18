package com.mygdx.game.gameSpecifics.components;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameSpecifics.level.GridTile;
import com.mygdx.game.gameSpecifics.level.Playfield;

public class EnemyPlayfieldAIComponent extends GridUserComponent 
{
	public enum AIState
	{
		Movement,
		Attack
	}
	
	private Playfield _playfield;
	private AIState _currentState = AIState.Movement;
	
	public EnemyPlayfieldAIComponent(Playfield playfield)
	{
		super(playfield.getGrid(), "Enemy", 1, 1);
		_playfield = playfield;
	}

	@Override
	public void updated(float dt)
	{
		if(_currentState == AIState.Movement)
		{
			movementLogics();
		}
		else if(_currentState == AIState.Attack)
		{
			attackLogics();
		}
	}

	@Override
	protected void destroyed() 
	{
		_playfield = null;
		super.destroyed();
	}

	private void movementLogics() 
	{
		if(!this.isLocated()) { return; }
		Vector2 target = getTargetPosition();
		if(target == null) { return; }
		Vector2 newTileLocation = new Vector2(this.getLocationX(), this.getLocationY() + 1);
		GridTile newTile = this.getGrid().getTile((int)newTileLocation.x, (int)newTileLocation.y);
		Vector2 ownPos = new Vector2(this.getParentOfComponent().getTransformComponent().getPositionX(), this.getParentOfComponent().getTransformComponent().getPositionY());
		if(Vector2.dst(ownPos.x, ownPos.y, getTargetPosition().x, getTargetPosition().y) < 2f)
		{
			// Arrived at selected tile.
			if(this.getLocationY() < this.getGrid().getTileAmountY() - 2)
			{
				if(!newTile.hasOccupyingTag("Enemy"))
				{
					this.placeSelfOnLocation(newTile.getPositionX(), newTile.getPositionY());
				}
			}
			else
			{
				this._currentState = AIState.Attack;
			}
		}
		else
		{
			Vector2 dif = getTargetPosition().sub(ownPos);
			dif.nor();
			this.getParentOfComponent().getTransformComponent().translatePosition(dif);
		}
	}
	
	private void attackLogics() 
	{
		
	}
	
	private Vector2 getTargetPosition()
	{
		if(!this.isLocated()) {return null;}
		float x = this.getLocationX() * this.getGrid().getTileWidth() + (this.getGrid().getTileWidth() / 2);
		float y = (getGrid().getGridHeight() - this.getLocationY() * this.getGrid().getTileHeight()) + (this.getGrid().getTileHeight() / 2);
		return new Vector2(x, y);
	}
}
