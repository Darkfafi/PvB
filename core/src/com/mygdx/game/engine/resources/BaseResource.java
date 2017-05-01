package com.mygdx.game.engine.resources;

/**
 * This base class contains all the basics a Resource class has to have.
 * @author Ramses Di Perna
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