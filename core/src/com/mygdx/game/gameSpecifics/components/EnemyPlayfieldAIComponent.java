package com.mygdx.game.gameSpecifics.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameSpecifics.entities.Enemy;
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
	private Enemy _enemy;
	private AIState _currentState = AIState.Movement;
	
	public EnemyPlayfieldAIComponent(Playfield playfield)
	{
		super(playfield.getGrid(), "Enemy", 1, 1);
		_enemy = (Enemy)this.getParentOfComponent();
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
		_enemy = null;
		super.destroyed();
	}

	private void movementLogics() 
	{
		if(!this.isLocated()) { return; }
		Vector2 target = getTargetPosition();
		if(target == null) { return; }
		Vector2 ownPos = new Vector2(this.getParentOfComponent().getTransformComponent().getPositionX(), this.getParentOfComponent().getTransformComponent().getPositionY());
		
		
		if(Vector2.dst(ownPos.x, ownPos.y, getTargetPosition().x, getTargetPosition().y) < 2f)
		{
			// Arrived at selected tile.
			if(this.getLocationY() < this.getGrid().getTileAmountY() - 2)
			{
				Vector2 newTileLocation = getNewTargetTile();
				if(newTileLocation == null) { return; }
				this.placeSelfOnLocation((int)newTileLocation.x,(int)newTileLocation.y);
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
	
	private Vector2 getNewTargetTile() 
	{
		boolean sideStepPrefered = (float) (Math.random() * 100f) < 20;
		Vector2 nextTile = new Vector2(this.getLocationX(), this.getLocationY() + 1);
		Vector2 sideStep = getSideStep();
		if(sideStepPrefered && sideStep != null)
		{
			return sideStep;
		}
		if(canWalkOnTile((int)nextTile.x, (int)nextTile.y))
		{
			return nextTile;
		}
		
		Vector2 availableInNextRow = getWalkableTileOnRow((int)nextTile.y);
		if(availableInNextRow == null) {return null; }
		int xDir = (int)availableInNextRow.x - this.getLocationX();
		xDir = Math.abs(xDir) / xDir;
		if(!canWalkOnTile(this.getLocationX() + xDir, this.getLocationY()))
		{
			return null;
		}
		return new Vector2(this.getLocationX() + xDir, this.getLocationY());
	}
	
	private Vector2 getSideStep()
	{
		Vector2 nextTile = new Vector2(this.getLocationX(), this.getLocationY() + 1);
		boolean leftFirst = (float) (Math.random() * 100f) < 50;
		int dir = leftFirst ? -1 : 1;
		Vector2 tile = new Vector2(nextTile.x + dir, nextTile.y);
		if(canWalkOnTile((int)tile.x, (int)tile.y))
		{
			return tile;
		}
		else
		{
			tile = new Vector2(nextTile.x - dir, nextTile.y);
			if(canWalkOnTile((int)tile.x, (int)tile.y))
				return tile;
		}
		return null;
	}

	private void attackLogics() 
	{
		_playfield.getPlayerBase().getComponent(HealthComponent.class).damage(0.1f);
	}
	
	private Vector2 getTargetPosition()
	{
		if(!this.isLocated()) {return null;}
		float x = this.getLocationX() * this.getGrid().getTileWidth() + (this.getGrid().getTileWidth() / 2);
		float y = (getGrid().getGridHeight() - this.getLocationY() * this.getGrid().getTileHeight()) + (this.getGrid().getTileHeight() / 2);
		return new Vector2(x, y);
	}
	
	private boolean canWalkOnTile(int tileX, int tileY)
	{
		if(tileX < 0 || tileX > this.getGrid().getTileAmountX() - 1) { return false;}
		GridTile tile = this.getGrid().getTile(tileX, tileY);
		if(tile == null) { return false; }
		
		if(tile.hasOccupyingTag("Enemy"))
		{
			return false;
		}
		
		return true;
	}
	private Vector2 getWalkableTileOnRow(int yRow)
	{
		ArrayList<GridTile> tiles = (ArrayList<GridTile>) this.getGrid().getAllTiles();
		Vector2 v = null;
		for(int i = 0; i < tiles.size(); i++)
		{
			if(tiles.get(i).getPositionY() == yRow)
			{
				v = new Vector2(tiles.get(i).getPositionX(), tiles.get(i).getPositionY());
				if(canWalkOnTile((int)v.x,(int)v.y))
					return v;
			}
		}
		return null;
	}
}
