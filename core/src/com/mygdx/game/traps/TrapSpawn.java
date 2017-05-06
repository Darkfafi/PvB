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
	private Grid _grid;
	
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
	public void spawnTrap()
	{
		BaseTrap t = TrapFactory.createTrap(_info.getTrapsAbleToSpawn()[(int) Math.ceil(Math.random() * _info.getTrapsAbleToSpawn().length - 1)], _info.getTrapFaceDirection(), _grid);
		int farLeftSide = t.place(_info.getGridPosX(), _info.getGridPosY());
		
		float _activatorXPos = (t.getSizeX() / 2) * _grid.getTileWidth();
		if(_info.getGridPosX() != farLeftSide)
		{
			_activatorXPos *= -1;
			_activatorXPos += _grid.getTileWidth();
		}
		
		_activator.linkToTrap(t);
		_activator.getTransformComponent().setPosition(_activatorXPos, -35);
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
	}
}
