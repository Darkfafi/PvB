package com.mygdx.game.resources;

/**
 * 
 * @author Darkfafi
 *
 */
public abstract class BaseResource 
{
	/**
	 * This method will be where the textures will be loaded by the extending class.
	 * The extending class must call the 'loadResource' method for each texture to load.
	 */
	public abstract void load();
	
	/**
	 * This method will be where all the cleaning of loaded data will be done.
	 */
	public abstract void clean();
}
