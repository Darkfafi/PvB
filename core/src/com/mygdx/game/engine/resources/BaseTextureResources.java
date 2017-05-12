package com.mygdx.game.engine.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.engine.entities.components.rendering.FrameInfo;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;

/**
 * This class can be inherited to load in textures from the folders and link them to keys.
 * These textures can also be disposed with this class.
 * @author Ramses Di Perna
 */
public abstract class BaseTextureResources extends BaseResource
{	
	private HashMap<String, RenderInfo> _texturesUnderKeys = new HashMap<String, RenderInfo>();
	
	/**
	 * Gets a texture by its key which is was given in the 'loadTextureResources' method
	 * @param key linked to texture
	 * @return texture linked to the given key
	 */
	public RenderInfo getRenderInfo(String key)
	{
		return _texturesUnderKeys.get(key);
	}
	
	/**
	 * Disposes all the textures which have been used from this class.
	 */
	public void disposeAllTextures()
	{
		for(Map.Entry<String, RenderInfo> entry : _texturesUnderKeys.entrySet())
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
		RenderInfo texture = _texturesUnderKeys.get(key);
		disposeTexture(texture);	
	}
	
	@Override
	public void clean()
	{
		disposeAllTextures();
		_texturesUnderKeys.clear();
		_texturesUnderKeys = null;
	}
	
	/**
	 * Loads a texture at given path into data and links it with the given key
	 * @param key to link the texture to
	 * @param path to where the texture is located
	 */
	protected RenderInfo loadResource(String key, String path)
	{
		Texture texture = new Texture(Gdx.files.internal(path));
		RenderInfo ri = new RenderInfo(texture);
		_texturesUnderKeys.put(key, ri);
		return ri;
	}
	
	protected RenderInfo loadResource(String key, String pathSheet, String pathXml)
	{
		XmlReader reader = new XmlReader();
		Texture texture = new Texture(Gdx.files.internal(pathSheet));
		RenderInfo renderInfo = null;
		Element xmlFile;
		try {
			xmlFile = reader.parse(Gdx.files.internal(pathXml));
			Array<Element> allFrames = xmlFile.getChildrenByName("frame");
			
			FrameInfo currentFrameInfo;
			FrameInfo[] frameInfos = new FrameInfo[allFrames.size];
			
			for(int i = 0; i < allFrames.size; i++)
			{
				Element frame = allFrames.get(i);
					
				int xCut = Integer.parseInt(frame.getChildByName("xPos").getText());
				int yCut = Integer.parseInt(frame.getChildByName("yPos").getText());
				int widthCut = Integer.parseInt(frame.getChildByName("width").getText());
				int heightCut = Integer.parseInt(frame.getChildByName("height").getText());
					
				currentFrameInfo = new FrameInfo(xCut, yCut, widthCut, heightCut);
					
				frameInfos[i] = currentFrameInfo;
				
			}
			
			renderInfo = new RenderInfo(texture, frameInfos);
			_texturesUnderKeys.put(key, renderInfo);
			return renderInfo;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Internally disposes the texture it is given. Does nothing if the given texture has a value of 'null'
	 * @param texture to dispose. Does nothing with 'null'
	 */
	private void disposeTexture(RenderInfo texture)
	{
		if(texture != null)
		{
			texture.getTextureToDraw().dispose();
		}
	}
}
