package com.mygdx.game.traps.types;

import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

public abstract class BaseBlockTrap extends BaseTrap
{
	public BaseBlockTrap(Grid grid, TrapFactory.Direction direction) 
	{
		super(grid, direction);
	}
	
	@Override
	protected void doEffect()
	{
		this.getGridUserComponent().addGridOccupyTag(GridTags.OCCUPY_TAG_BLOCKED);
	}
}
