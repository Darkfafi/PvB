package com.mygdx.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.GridTile;
import com.mygdx.game.level.Playfield;

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
	private float _movementSpeed = 1;
	private Vector2 _currentTargetPosition;
	
	public EnemyPlayfieldAIComponent(Playfield playfield, float movementSpeed)
	{
		super(playfield.getGrid(), GridTags.OCCUPY_TAG_ENEMY, 1, 1);
		_movementSpeed = movementSpeed;
		_playfield = playfield;
	}
	
	@Override
	public void awake()
	{
		super.awake();
		_enemy = (Enemy)this.getParentOfComponent();
	}
	
	@Override
	protected void updated(float dt)
	{
		super.updated(dt);
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
		_currentTargetPosition = null;
		super.destroyed();
	}

	private void movementLogics() 
	{
		if(!this.isLocated()) { return; }
		
		if(_currentTargetPosition == null)
		{
			_currentTargetPosition = getTargetPosition();
		}
		
		if(_currentTargetPosition == null) { return; }
		Vector2 ownPos = new Vector2(this.getParentOfComponent().getTransformComponent().getPositionX(), this.getParentOfComponent().getTransformComponent().getPositionY());
		
		
		if(Vector2.dst(ownPos.x, ownPos.y, _currentTargetPosition.x, _currentTargetPosition.y) < 2f)
		{
			// Arrived at selected tile.
			if(this.getLocationY() < this.getGrid().getTileAmountY() - Playfield.BORDER_TILE_Y_LINE_FROM_END)
			{
				Vector2 newTileLocation = getNewTargetTile();
				
				if(newTileLocation == null) 
				{ 
					_enemy.setEnemyState(Enemy.EnemyState.IdleState); 
					return; 
				}
				
				this.placeSelfOnLocation((int)newTileLocation.x,(int)newTileLocation.y);
				_currentTargetPosition = getTargetPosition();
			}
			else
			{
				this._currentState = AIState.Attack;
			}

			_currentTargetPosition = null;
		}
		else
		{
			_enemy.setEnemyState(Enemy.EnemyState.WalkState);
			Vector2 dif = new Vector2(_currentTargetPosition.x - ownPos.x, _currentTargetPosition.y - ownPos.y);
			dif.nor();
			dif.x *= (_movementSpeed * 1.5f);
			dif.y *= _movementSpeed;
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
		_enemy.setEnemyState(Enemy.EnemyState.AttackState); 
		_playfield.getPlayerBase().getComponent(HealthComponent.class).damage(0.1f);
	}
	
	private Vector2 getTargetPosition()
	{
		if(!this.isLocated()) {return null;}
		
		float amountX = (float) Math.random();
		float amountY = (float) Math.random();
		
		if(amountX < 0.3f) { amountX = 0.3f;}
		else if(amountX > 0.7f) { amountX = 0.7f;}
		if(amountY < 0.4f) { amountY = 0.4f;}
		else if(amountY > 0.8f) { amountY = 0.8f;}
		
		float x = this.getLocationX() * this.getGrid().getTileWidth() + (this.getGrid().getTileWidth() * amountX);
		float y = (getGrid().getGridHeight() - this.getLocationY() * this.getGrid().getTileHeight()) + (this.getGrid().getTileHeight() * amountY);
		return new Vector2(x, y);
	}
	
	private boolean canWalkOnTile(int tileX, int tileY)
	{
		if(tileX < 0 || tileX > this.getGrid().getTileAmountX() - 1) { return false;}
		GridTile tile = this.getGrid().getTile(tileX, tileY);
		if(tile == null) { return false; }
		
		if(tile.hasOccupyingTag(GridTags.OCCUPY_TAG_ENEMY) || tile.hasOccupyingTag(GridTags.OCCUPY_TAG_BLOCKED))
		{
			return false;
		}
		
		return true;
	}
	private Vector2 getWalkableTileOnRow(int yRow)
	{
		ArrayList<GridTile> tiles = (ArrayList<GridTile>) this.getGrid().getAllTilesInYRow(yRow);
		Vector2 v = null;
		for(int i = 0; i < tiles.size(); i++)
		{
			v = new Vector2(tiles.get(i).getPositionX(), tiles.get(i).getPositionY());
			if(canWalkOnTile((int)v.x,(int)v.y))
				return v;
		}
		return null;
	}
}
