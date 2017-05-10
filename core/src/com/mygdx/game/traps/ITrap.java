package com.mygdx.game.traps;

/**
 * This interface contains all the methods needed for a class to be a trap on a grid
 * @author Ramses Di Perna
 *
 */
public interface ITrap 
{
	/**
	 * Here the effect will be triggered of the trap
	 */
	public void trigger();
	/**
	 * The amount of tiles this trap covers in the x axis starting from the left side
	 * @return X size in tile amount
	 */
	public int getSizeX();
	/**
	 * The amount of tiles this trap covers in the y axis starting from the upper side
	 * @return Y size in tile amount
	 */
	public int getSizeY();
}
