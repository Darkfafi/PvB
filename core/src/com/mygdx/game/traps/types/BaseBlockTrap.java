package com.mygdx.game.traps.types;

import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

public abstract class BaseBlockTrap extends BaseTrap
{
	protected boolean canBeTriggered = true;
	
	public BaseBlockTrap(Grid grid, TrapFactory.Direction direction) 
	{
		super(grid, direction);
	}
	
	@Override
	protected void doEffect()
	{
		this.getGridUserComponent().addGridOccupyTag(GridTags.OCCUPY_TAG_BLOCKED);
		canBeTriggered = false;
	}
	
	@Override
	public boolean canBeTriggered()
	{
		return canBeTriggered;
	}
}
