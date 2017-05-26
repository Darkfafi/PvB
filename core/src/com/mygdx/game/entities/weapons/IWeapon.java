package com.mygdx.game.entities.weapons;

/**
 * The interface which gives all the handels a weapon can be controlled with
 * @author Ramses Di Perna
 *
 */
public interface IWeapon 
{
	/**
	 * Weapon types. Every type has a different way of handling and UI drawing.
	 * @author Ramses Di Perna
	 *
	 */
	public enum WeaponType
	{
		/**
		 * Pulling mechanism, projectile which is shown on the weapon and which can be fired at different speeds.
		 * The projectile is of ArrowProjectile type and lands after x amount of distance traveled.
		 */
		BowType
	}
	
	/**
	 * This activates the special ability of the weapon
	 * @param specialType is a user defines special type
	 */
	public void activateSpecial(int specialType);
	
	/**
	 * Returns the weapon type of this weapon instance.
	 * @return WeaponType of this weapon.
	 */
	public WeaponType getWeaponType();
	
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
	
	/**
	 * Makes the class ready for garbage collection.
	 */
	public void clean();
}
