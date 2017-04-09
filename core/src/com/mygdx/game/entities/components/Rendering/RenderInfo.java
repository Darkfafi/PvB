package com.mygdx.game.entities.components.Rendering;

import com.badlogic.gdx.graphics.Texture;

/**
 * This is a class holding a texture to display and what part of the texture to display
 * @author Ramses Di Perna
 *
 */
public class RenderInfo 
{	
	private Texture _textureToDraw = null;
	private FrameInfo[] _frameInfos;
	private int _currentFrameInfo = 0;
	
	public RenderInfo()
	{
		
	}
	
	/**
	 * Shows the entire picture given
	 * @param texture to display
	 */
	public RenderInfo(Texture texture)
	{
		_textureToDraw = texture;
		_frameInfos = new FrameInfo[1];
		_frameInfos[0] = new FrameInfo(0, 0, texture.getWidth(), texture.getHeight());
	}
	
	/**
	 * Shows a part of the texture given. All other info is given in pixels.
	 * @param texture to show partly
	 * @param cutPosX is where the cutting should start in the x axis
	 * @param cutPosY is where the cutting should start in the y axis
	 * @param frameSizeX is how much it should cut starting from the cutPosX
	 * @param frameSizeY is how much it should cut starting from the cutPosY
	 */
	public RenderInfo(Texture texture, FrameInfo[] frames)
	{
		_textureToDraw = texture;
		_frameInfos = frames;
	}
	
	/**
	 * Returns the amount of frame info classes this render info contains.
	 * @return The amount of frame infos 
	 */
	public int getFramesLength()
	{
		return _frameInfos.length;
	}
	/**
	 * Returns what frame info index should be displayed
	 * @return The index of the current frame info
	 */
	public int getCurrentFrameInfo() 
	{
		return _currentFrameInfo;
	}
	
	/**
	 * Sets what frame info index should be displayed
	 * @param currentFrameInfo of the new frame to display
	 */
	public void setCurrentFrameInfo(int currentFrameInfo)
	{
		_currentFrameInfo = currentFrameInfo;
	}
	
	/**
	 * Returns the full texture to draw
	 * @return the entire texture to draw
	 */
	public Texture getTextureToDraw()
	{
		return _textureToDraw;
	}
	
	/**
	 * Returns the X axis start cutting coordinates for the texture.
	 * @return The X axis start cutting coordinates for the texture.
	 */
	public int getStartX()
	{
		return _frameInfos[_currentFrameInfo].getStartX();
	}
	
	/**
	 * Returns the Y axis start cutting coordinates for the texture.
	 * @return The Y axis start cutting coordinates for the texture.
	 */
	public int getStartY()
	{
		return _frameInfos[_currentFrameInfo].getStartY();
	}
	
	/**
	 * Returns the width it should cut from the start position.
	 * @return The width it should cut from the start position.
	 */
	public int getCutWidth()
	{
		return _frameInfos[_currentFrameInfo].getCutWidth();
	}
	
	/**
	 * Returns the height it should cut from the start position.
	 * @return The height it should cut from the start position.
	 */
	public int getCutHeight()
	{
		return _frameInfos[_currentFrameInfo].getCutHeight();
	}
}
