package com.mygdx.game.engine.entities.components.rendering;

/**
 * This is a class holding frame cut information
 * @author Ramses Di Perna
 *
 */
public class FrameInfo 
{
	private int _posX, _posY, _frameX, _frameY;
	
	/**
	 * Shows the entire picture given
	 * @param texture to display
	 */
	public FrameInfo()
	{
		_posX = 0;
		_posY = 0;
		_frameX = 0;
		_frameY = 0;
	}
	
	/**
	 * Shows a part of the texture given. All other info is given in pixels.
	 * @param cutPosX is where the cutting should start in the x axis
	 * @param cutPosY is where the cutting should start in the y axis
	 * @param frameSizeX is how much it should cut starting from the cutPosX
	 * @param frameSizeY is how much it should cut starting from the cutPosY
	 */
	public FrameInfo(int cutPosX, int cutPosY, int frameSizeX, int frameSizeY)
	{
		_posX = cutPosX;
		_posY = cutPosY;
		_frameX = frameSizeX;
		_frameY = frameSizeY;
	}
	
	/**
	 * Returns the X axis start cutting coordinates for the texture.
	 * @return The X axis start cutting coordinates for the texture.
	 */
	public int getStartX()
	{
		return _posX;
	}
	
	/**
	 * Returns the Y axis start cutting coordinates for the texture.
	 * @return The Y axis start cutting coordinates for the texture.
	 */
	public int getStartY()
	{
		return _posY;
	}
	
	/**
	 * Returns the width it should cut from the start position.
	 * @return The width it should cut from the start position.
	 */
	public int getCutWidth()
	{
		return _frameX;
	}
	
	/**
	 * Returns the height it should cut from the start position.
	 * @return The height it should cut from the start position.
	 */
	public int getCutHeight()
	{
		return _frameY;
	}
}
