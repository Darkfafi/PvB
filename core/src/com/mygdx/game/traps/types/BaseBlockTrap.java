package com.mygdx.game.traps.types;

import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

public abstract class BaseBlockTrap extends BaseTrap
{
	public BaseBlockTrap(Grid grid) 
	{
		super(grid);
	}
	
	@Override
	protected void doEffect()
	{
		this.getGridUserComponent().addGridOccupyTag(GridTags.OCCUPY_TAG_BLOCKED);
	}
}
