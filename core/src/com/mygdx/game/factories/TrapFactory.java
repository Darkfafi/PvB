package com.mygdx.game.factories;

import com.mygdx.game.level.Grid;
import com.mygdx.game.traps.types.BaseTrap;
import com.mygdx.game.traps.types.TrainBlockTrap;

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
		Block_TrainTrap
	}
	
	
	/**
	 * This enum represents the Direction the Trap will be facing.
	 * @author Ramses Di Perna
	 *
	 */
	public enum Direction
	{
		Left,
		Right
	}
	
	/**
	 * This creates an instance of the given trap type
	 * @param trapType to create an instance for
	 * @return the trap instance of the given type.
	 */
	public static BaseTrap createTrap(TrapType trapType, Direction direction, Grid grid)
	{
		BaseTrap trap = createTrapInstance(trapType, direction, grid);
		return trap;
	}


	/**
	 * This creates an instance of the given trap type
	 * @param trapType to create an instance for
	 * @return the trap instance of the given type.
	 */
	private static BaseTrap createTrapInstance(TrapType trapType, Direction direction, Grid grid) 
	{
		switch(trapType)
		{
			case Block_TrainTrap:
				return new TrainBlockTrap(grid, direction);
			default:
				return null;
		}
	}
}
