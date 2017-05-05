package com.mygdx.game.traps.types;

import com.mygdx.game.level.Grid;

public abstract class BaseDamageRadiusTrap extends BaseTrap 
{	
	// Radius variable, damage variable.
	// Effect does, get all entities with tag "Enemy", get enemies which are in radius range, do damage and give them a subtle blow back
	public BaseDamageRadiusTrap(Grid grid) 
	{
		super(grid);
	}

}
