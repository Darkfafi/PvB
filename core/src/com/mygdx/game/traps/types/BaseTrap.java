package com.mygdx.game.traps.types;

import com.mygdx.game.components.GridUserComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;
import com.mygdx.game.traps.ITrap;

public abstract class BaseTrap extends BaseEntity implements ITrap 
{
	private Grid _grid;
	private GridUserComponent _gridUserComponent;
	
	public BaseTrap(Grid grid)
	{
		_gridUserComponent = this.addComponent(new GridUserComponent(grid, GridTags.OCCUPY_TAG_TRAP, this.getSizeX(), this.getSizeY()));
	}
	
	@Override
	protected void destroyed() 
	{
		_gridUserComponent = null;
		_grid = null;
	}
	
	protected Grid getGrid()
	{
		return _grid;
	}
	
	protected GridUserComponent getGridUserComponent()
	{
		return _gridUserComponent;
	}
	
	@Override
	public void trigger()
	{
		doEffect();
		//TODO: Active timer or something
	}
	
	protected abstract void doEffect();
}
