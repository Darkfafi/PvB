package com.mygdx.game.traps.types;

import com.mygdx.game.components.GridUserComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;
import com.mygdx.game.traps.ITrap;

/**
 * This is the base class for all the traps in the game.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseTrap extends BaseEntity implements ITrap 
{
	private Grid _grid;
	private GridUserComponent _gridUserComponent;
	private TrapFactory.Direction _direction;
	
	public BaseTrap(Grid grid, TrapFactory.Direction direction)
	{
		_grid = grid;
		_direction = direction;
		_gridUserComponent = this.addComponent(new GridUserComponent(grid, GridTags.OCCUPY_TAG_TRAP, this.getSizeX(), this.getSizeY()));
	}
	
	/**
	 * Places the trap on the grid and sets the transform location on the tile its world location
	 * @param gridXPos is the x position as grid index (x axis)
	 * @param gridYPos is the y position as grid index (y axis)
	 * @return Far left side of the trap in grid position
	 */
	public int place(int gridXPos, int gridYPos)
	{
		int xPosGrid = (_direction == TrapFactory.Direction.Right) ? gridXPos : gridXPos - (this.getSizeX() - 1);
		getTransformComponent().setPosition(_grid.getTileWorldPosition(xPosGrid, gridYPos));
		_gridUserComponent.placeSelfOnLocation(xPosGrid, gridYPos);
		return xPosGrid;
	}
	
	@Override
	protected void destroyed() 
	{
		_gridUserComponent = null;
		_grid = null;
	}
	
	/**
	 * Returns the grid the trap is placed on
	 * @return The grid the trap is placed on
	 */
	protected Grid getGrid()
	{
		return _grid;
	}
	
	/**
	 * Returns the direction which the trap is facing.
	 * @return The direction the trap is facing
	 */
	public TrapFactory.Direction getDirection()
	{
		return _direction;
	}
	
	/**
	 * The Grid user component the traps use to effect the state of the tiles on the grid and to be placed on them
	 * @return The GridUserComponent of the trap
	 */
	protected GridUserComponent getGridUserComponent()
	{
		return _gridUserComponent;
	}
	
	@Override
	public void trigger()
	{
		doEffect();
	}
	
	/**
	 * This is triggered when the trap is triggered * it was allowed to be triggered by the 'canBeTriggered()' method
	 */
	protected abstract void doEffect();
}
