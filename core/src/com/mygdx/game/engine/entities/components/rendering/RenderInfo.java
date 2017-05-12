package com.mygdx.game.engine.entities.components.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * This is a class holding a texture to display and what part of the texture to display
 * @author Ramses Di Perna
 *
 */
public class RenderInfo 
{	
	private Texture _textureToDraw = null;
	private FrameInfo[] _frameInfos;
	private Vector2 _innerOffset = new Vector2();
	
	/**
	 * Shows the entire picture given
	 * @param texture to display
	 */
	public RenderInfo(Texture texture)
	{
		_textureToDraw = texture;
		_frameInfos = new FrameInfo[1];	// 1 Cut info required. The info to cut the entire picture.
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
	 * Returns all the FrameInfos linked in the RenderInfo
	 * @return All FrameInfos of the RenderInfo
	 */
	public FrameInfo[] getAllFrameInfos()
	{
		return _frameInfos;
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
	public int getStartX(int frameInfoIndex)
	{
		return _frameInfos[frameInfoIndex].getStartX();
	}
	
	/**
	 * Returns the Y axis start cutting coordinates for the texture.
	 * @return The Y axis start cutting coordinates for the texture.
	 */
	public int getStartY(int frameInfoIndex)
	{
		return _frameInfos[frameInfoIndex].getStartY();
	}
	
	/**
	 * Returns the width it should cut from the start position.
	 * @return The width it should cut from the start position.
	 */
	public int getCutWidth(int frameInfoIndex)
	{
		return _frameInfos[frameInfoIndex].getCutWidth();
	}
	
	/**
	 * Returns the height it should cut from the start position.
	 * @return The height it should cut from the start position.
	 */
	public int getCutHeight(int frameInfoIndex)
	{
		return _frameInfos[frameInfoIndex].getCutHeight();
	}
	
	/**
	 * Affects the rendering. Default (0,0) keeps the RenderInfo at normal space. 1 draws it its mass to one side, and -1 to the opposite.
	 * @return The inner offset value for the rendering.
	 */
	public Vector2 getInnerOffset()
	{
		return _innerOffset;
	}
	
	/**
	 * Sets the inner offset of this RenderInfo
	 * If its 0,0. Its unaffected. If the x is set to 0.5. The texture will be drawn half his width offset to the right. etc
	 * @param xOffset normalized value of how much to put offset in the x axis using the value of the texture width
	 * @param yOffset normalized value of how much to put offset in the y axis using the value of the texture height
	 */
	public void setInnerOffset(float xOffset, float yOffset)
	{
		_innerOffset.x = xOffset;
		_innerOffset.y = yOffset;
	}
	
	/**
	 * Cleans the data in the RenderInfo Class.
	 */
	public void clean()
	{
		_textureToDraw = null;
		_frameInfos = null;
		_innerOffset = null;
	}
}
