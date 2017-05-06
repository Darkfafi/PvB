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
	private int _gridPosX;
	private int _gridPosY;
	private TrapFactory.Direction _trapFaceDirection;
	private TrapFactory.TrapType[] _typesAbleToSpawn;
	private float _activatorPosition = 0.2f;
	
	/**
	 * The trap info needs the following info details in order to function.
	 * @param gridPosX represents the X index location it will spawn the traps on the grid.
	 * @param gridPosY represents the Y index location it will spawn the traps on the grid.
	 * @param trapFaceDirection represents the direction the trap will be facing. At the end of this direction the an activator is adviced to be placed.
	 * @param activatorPosition is the normalized activator position in relation to the trap size.
	 * @param trapTypes contains the different Trap Types which are able to be spawned on this spawn location.
	 */
	public TrapSpawnInfo(int gridPosX, int gridPosY, TrapFactory.Direction trapFaceDirection, float activatorPosition, TrapFactory.TrapType[] trapTypes)
	{
		_activatorPosition = activatorPosition;
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
	public TrapFactory.Direction getTrapFaceDirection()
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
	
	/**
	 * Returns the activator normalized position (0 - 1)
	 * @return The normalized activator position in relation to the trap size
	 */
	public float getActivatorPosition()
	{
		return _activatorPosition;
	}
}
