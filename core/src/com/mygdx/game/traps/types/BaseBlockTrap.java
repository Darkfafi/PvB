package com.mygdx.game.traps.types;

import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

/**
 * This is the Base of a blocking trap.
 * It will block all the tiles it is placed on when activated.
 * @author Ramses Di Perna
 *
 */
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
		this.getGridUserComponent().addGridOccupyTag(GridTags.OCCUPY_TAG_DAMAGING);
	}
}
