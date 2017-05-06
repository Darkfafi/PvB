package com.mygdx.game.factories;

import com.mygdx.game.level.Grid;
import com.mygdx.game.traps.types.BaseTrap;
import com.mygdx.game.traps.types.TestBlockTrap;

/**
 * This class contains static methods which can be used to create traps. 
 * The traps are defined under Types and can have multiple skins under the same type.
 * The stats can be set in this class for the given type.
 * @author Ramses Di Perna
 *
 */
public class TrapFactory 
{
	// Block 	== Blocking trap. (Things blocking the path of enemies)
	// Harm 	== Damaging trap. (Things that Damage enemies)
	/**
	 * This represents a trap type. Each kind of trap is a trap type and has an effect specified to it
	 * Block 	== Blocking trap. (Things blocking the path of enemies)
	 * Harm 	== Damaging trap. (Things that Damage enemies)
	 * @author Ramses Di Perna
	 *
	 */
	public enum TrapType
	{
		Block_BarrelTrap
	}
	
	
	/**
	 * This creates an instance of the given trap type
	 * @param trapType to create an instance for
	 * @return the trap instance of the given type.
	 */
	public static BaseTrap createTrap(TrapType trapType, Grid grid)
	{
		BaseTrap trap = createTrapInstance(trapType, grid);
		return trap;
	}


	/**
	 * This creates an instance of the given trap type
	 * @param trapType to create an instance for
	 * @return the trap instance of the given type.
	 */
	private static BaseTrap createTrapInstance(TrapType trapType, Grid grid) 
	{
		switch(trapType)
		{
			case Block_BarrelTrap:
				return new TestBlockTrap(grid);
			default:
				return null;
		}
	}
}
