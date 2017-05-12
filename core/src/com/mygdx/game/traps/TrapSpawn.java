package com.mygdx.game.traps;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.level.Grid;
import com.mygdx.game.traps.types.BaseTrap;

/**
 * This class is responsible for spawning the correct trap in the correct manner when its 'spawnTrap' method is called.
 * This will handle the trap's orientation, location and spawning + linking the trap activator correctly
 * @author Ramses Di Perna
 *
 */
public class TrapSpawn extends BaseEntity
{
	private TrapSpawnInfo _info;
	private TrapActivator _activator;
	private BaseTrap _trapSpawned;
	private Grid _grid;
	
	private int _countsSinceReset = 0;
	private int _countsNeededToReset = 0;
	
	/**
	 * The TrapSpawn needs get information on what traps its able to spawn and on what grid. It positions itself using the info given.
	 * @param info containing the information of the traps it can spawn.
	 * @param grid to spawn the traps on and to give to the traps.
	 */
	public TrapSpawn(TrapSpawnInfo info, Grid grid)
	{
		_info = info;	
		_grid = grid;
		_activator = new TrapActivator(null);
		getTransformComponent().setPosition(_grid.getTileWorldPosition(_info.getGridPosX(), _info.getGridPosY()));
		_activator.getTransformComponent().setParent(this.getTransformComponent());
	}
	
	/**
	 * Spawns a trap and trap activator and positions them and links them.
	 */
	public void spawnTrap(float activatorPosition)
	{
		_countsSinceReset = 0;
		if(_trapSpawned != null)
		{
			_trapSpawned.destroy();
		} 
		
		TrapFactory.TrapType typeSpawning = _info.getTrapsAbleToSpawn()[(int) Math.ceil(Math.random() * _info.getTrapsAbleToSpawn().length - 1)];
		_trapSpawned = TrapFactory.createTrap(typeSpawning, _info.getTrapFaceDirection(), _grid);
		_countsNeededToReset = TrapFactory.getCountAmountForTrapReset(typeSpawning);
		
		_trapSpawned.place(_info.getGridPosX(), _info.getGridPosY());
		
		int tileSide = (int)((Math.ceil(_trapSpawned.getSizeX() * activatorPosition)) * _grid.getTileWidth());
		
		float _activatorXPos = tileSide;
		if(_trapSpawned.getDirection() == TrapFactory.Direction.Left)
		{
			_activatorXPos *= -1;
			_activatorXPos += _grid.getTileWidth();
		}
		
		_activator.linkToTrap(_trapSpawned);
		_activator.getTransformComponent().setPosition(_activatorXPos, -45);
	}
	
	public void countForResetTrap()
	{
		_countsSinceReset++;
		if(_countsSinceReset >= _countsNeededToReset)
		{
			resetTrap();
		}
	}
	
	public void forceResetTrap()
	{
		resetTrap();
	}
	
	public TrapActivator getTrapActivator()
	{
		return _activator;
	}
	
	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) 
	{
		
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed()
	{
		_info = null;
		_grid = null;
		
		if(_activator != null)
			_activator.destroy();
		
		_activator = null;
		
		if(_trapSpawned != null)
			_trapSpawned.destroy();
		
		_trapSpawned = null;
	}
	
	private void resetTrap()
	{
		_countsSinceReset = 0;
		if(_activator != null)
		{
			_activator.activateTrap();
		}
	}
}
