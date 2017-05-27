package com.mygdx.game.engine.resources;

import com.badlogic.gdx.assets.AssetManager;

/**
 * This base class contains all the basics a Resource class has to have.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseResource 
{	
	private AssetManager _assetManager;
	
	/**
	 * Gives a load request to the resource so it can give all the data it wants to load into the memory.
	 * @param assetManager to give all the data to.
	 */
	public void loadRequest(AssetManager assetManager)
	{
		_assetManager = assetManager;
		load();
	}
	
	/**
	 * This method will be where all the cleaning of loaded data will be done.
	 */
	public abstract void clean();
	
	/**
	 * This method will be where the textures will be loaded by the extending class.
	 * The extending class must call the 'loadResource' method for each texture to load.
	 */
	protected abstract void load();
	
	/**
	 * Returns the AssetManager which was given to the loadRequest method
	 * @return
	 */
	protected AssetManager getAssetManager()
	{
		return _assetManager;
	}
}
