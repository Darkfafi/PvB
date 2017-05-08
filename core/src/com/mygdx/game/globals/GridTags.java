package com.mygdx.game.globals;

/**
 * This class contains all the Grid Occupy tags which are used in the game
 * @author Ramses Di Perna
 *
 */
public final class GridTags 
{
	/**
	 * Tag which marks a tile as a tile containing or reserved by an enemy.
	 */
	public static final String OCCUPY_TAG_ENEMY = "Enemy";
	
	/**
	 * Tag which marks a tile as a trap containing tile (Any type of trap)
	 */
	public static final String OCCUPY_TAG_TRAP = "Trap";
	
	/**
	 * Tag which marks a tile as "Damaging"
	 */
	public static final String OCCUPY_TAG_DAMAGING = "Damaging";
	
	/**
	 * Tag which marks a tile as a blocked tile (Walls etc)
	 */
	public static final String OCCUPY_TAG_BLOCKED = "Blocked";
}
