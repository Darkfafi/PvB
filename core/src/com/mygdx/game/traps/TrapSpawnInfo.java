package com.mygdx.game.traps;

import com.mygdx.game.factories.TrapFactory;

/**
 * This class contains all the info for a specific trap spawn location on 
 * - Which traps can be spawned on that location
 * - Which direction the trap will be facing
 * @author Darkfafi
 *
 */
public class TrapSpawnInfo 
{
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
	
	private int _gridPosX;
	private int _gridPosY;
	private Direction _trapFaceDirection;
	private TrapFactory.TrapType[] _typesAbleToSpawn;
	
	/**
	 * The trap info needs the following info details in order to function
	 * @param gridPosX represents the X index location it will spawn the traps on the grid
	 * @param gridPosY represents the Y index location it will spawn the traps on the grid
	 * @param trapFaceDirection represents the direction the trap will be facing. At the end of this direction the an activator is adviced to be placed.
	 * @param trapTypes contains the different Trap Types which are able to be spawned on this spawn location
	 */
	public TrapSpawnInfo(int gridPosX, int gridPosY, Direction trapFaceDirection, TrapFactory.TrapType[] trapTypes)
	{
		_gridPosX = gridPosX;
		_gridPosY = gridPosY;
		_trapFaceDirection = trapFaceDirection;
		_typesAbleToSpawn = trapTypes;
	}
	
	/**
	 * Returns a list of TrapTypes which are able to be spawned on this spawn location
	 * @return A list of TrapType Enum items
	 */
	public TrapFactory.TrapType[] getTrapsAbleToSpawn()
	{
		return _typesAbleToSpawn;
	}
	
	/**
	 * Returns the direction the trap is requested to face when spawned
	 * @return Direction for trap
	 */
	public Direction getTrapFaceDirection()
	{
		return _trapFaceDirection;
	}
	
	/**
	 * The X index location the trap will be spawned on
	 * @return index representing an X axis position of the grid
	 */
	public int getGridPosX()
	{
		return _gridPosX;
	}
	
	/**
	 * The Y index location the trap will be spawned on
	 * @return index representing an Y axis position of the grid
	 */
	public int getGridPosY()
	{
		return _gridPosY;
	}
}
