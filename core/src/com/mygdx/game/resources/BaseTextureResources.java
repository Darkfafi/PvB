package com.mygdx.game.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * This class can be inherited to load in textures from the folders and link them to keys.
 * These textures can also be disposed with this class.
 * @author Ramses Di Perna
 */
public abstract class BaseTextureResources 
{	
	private HashMap<String, Texture> _texturesUnderKeys = new HashMap<String, Texture>();
	
	/**
	 * Gets a texture by its key which is was given in the 'loadTextureResources' method
	 * @param key linked to texture
	 * @return texture linked to the given key
	 */
	public Texture getTexture(String key)
	{
		return _texturesUnderKeys.get(key);
	}
	
	/**
	 * Disposes all the textures which have been used from this class.
	 */
	public void disposeAllTextures()
	{
		for(Map.Entry<String, Texture> entry : _texturesUnderKeys.entrySet())
		{
			disposeTexture(entry.getValue());
		}
	}
	
	/**
	 * Disposes the texture linked with the given key.
	 * @param key linked to texture to dispose.
	 */
	public void disposeTexture(String key)
	{
		Texture texture = _texturesUnderKeys.get(key);
		disposeTexture(texture);	
	}
	
	/**
	 * This class will be where the textures will be loaded by the extending class.
	 * The extending class must call the 'loadResource' method for each texture to load.
	 */
	public abstract void loadTextureResources();
	
	/**
	 * Loads a texture at given path into data and links it with the given key
	 * @param key to link the texture to
	 * @param path to where the texture is located
	 */
	protected void loadResource(String key, String path)
	{
		Texture texture = new Texture(Gdx.files.internal(path));
		_texturesUnderKeys.put(key, texture);
	}
	
	/**
	 * Internally disposes the texture it is given. Does nothing if the given texture has a value of 'null'
	 * @param texture to dispose. Does nothing with 'null'
	 */
	private void disposeTexture(Texture texture)
	{
		if(texture != null)
		{
			texture.dispose();
		}
	}
}
