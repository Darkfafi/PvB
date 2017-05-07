package com.mygdx.game.entities.weapons;

/**
 * The interface which gives all the handels a weapon can be controlled with
 * @author Ramses Di Perna
 *
 */
public interface IWeapon 
{
	/**
	 * Where the touch is when it starts controlling the weapon
	 * @param x position of the touch
	 * @param y position of the touch
	 */
	public void startControl(int x, int y);
	
	/**
	 * Where the touch is while it is controlling the weapon
	 * @param x position of the touch
	 * @param y position of the touch
	 */
	public void inControl(int x, int y);
	
	/**
	 * Where the touch is when the weapon control stops
	 * @param x position of the touch
	 * @param y position of the touch
	 */
	public void endControl(int x, int y);
}
