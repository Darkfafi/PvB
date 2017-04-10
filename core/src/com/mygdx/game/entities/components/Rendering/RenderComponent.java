package com.mygdx.game.entities.components.Rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.BaseEntityComponent;

/**
 * This component contains the 2D render information of the entity it is attached to.
 * (Texture to render, pivot point, height and width)
 * @author Ramses Di Perna
 *
 */
public class RenderComponent extends BaseEntityComponent implements Comparable<RenderComponent>
{
	private RenderInfo _renderInfo = new RenderInfo();
	private Vector2 _pivot = new Vector2(0.5f, 0.5f);
	private boolean _flipX = false;
	private boolean _flipY = false;
	private int _sortingLayer = 0;
	
	public RenderComponent(RenderInfo startRenderInfo)
	{
		this.setRenderInfo(startRenderInfo);
	}
	
	/**
	 * The sorting layer indicates the render layer of the entity.
	 * The higher the value, the more priority the entity has. (0 will be rendered under 1 and so on)
	 * @return The sorting layer of the entity
	 */
	public int getSortingLayer()
	{
		return _sortingLayer;
	}
	
	/**
	 * The sorting layer indicates the render layer of the entity.
	 * The higher the value, the more priority the entity has. (0 will be rendered under 1 and so on)
	 * @param sortingLayer is the new layer sorting value for the entity
	 */
	public void setSortingLayer(int sortingLayer)
	{
		_sortingLayer = sortingLayer;
	}
	
	/**
	 * Gets the width of the texture * the scaleX of the entity
	 * @return The real width calculated with the texture width.
	 */
	public float getRealWidth()
	{
		return this.getParentOfComponent().getTransformComponent().getScaleX() * (float)_renderInfo.getCutWidth();
	}
	
	/**
	 * Gets the height of the texture * the scaleY of the entity
	 * @return The real height calculated with the texture height.
	 */
	public float getRealHeight()
	{
		return this.getParentOfComponent().getTransformComponent().getScaleY() * (float)_renderInfo.getCutHeight();
	}
	
	/**
	 * Gets the pivot point's x position. 0 being the far left side and 1 being the far right side of the image.
	 * @return Pivot point's X range
	 */
	public float getPivotX()
	{
		return _pivot.x;
	}
	
	/**
	 * Gets the pivot point's position. 0 being the bottom and 1 being the top of the image
	 * @return Pivot point's Y range
	 */
	public float getPivotY()
	{
		return _pivot.y;
	}
	
	/**
	 * Sets the pivot point to the new value.
	 * X = 0 being the far left side and 1 being the far right side of the image.
	 * Y = 0 being the bottom and 1 being the top of the image
	 * @param newPivotValue
	 */
	public void setPivot(Vector2 newPivotValue)
	{
		_pivot = newPivotValue;
	}
	
	public RenderInfo getRenderInfo()
	{
		return _renderInfo;
	}
	
	public void setRenderInfo(RenderInfo info)
	{
		if(_renderInfo != null)
			_renderInfo.clean();
		
		_renderInfo = info;
	}
	
	/**
	 * Gets the current texture being rendered.
	 * @return The texture currently displayed on screen.
	 */
	public Texture getCurrentTexture()
	{
		return _renderInfo.getTextureToDraw();
	}
	
	/**
	 * Sets the current texture to render
	 * @param texture to set as new texture to render on screen.
	 */
	public void setCurrentTexture(Texture texture)
	{
		_renderInfo = new RenderInfo(texture);
	}
	
	/**
	 * Returns whether the image is flipped Horizontally
	 * @return true indicates it flips else it does not
	 */
	public boolean getFlipX()
	{
		return _flipX;
	}
	
	/**
	 * Returns whether the image is flipped Vertically
	 * @return true indicates it flips else it does not
	 */
	public boolean getFlipY()
	{
		return _flipY;
	}

	/**
	 * Sets whether the image should flip horizontally 
	 * @param flipX true indicates it flips else it does not
	 */
	public void setFlipX(boolean flipX)
	{
		_flipX = flipX;
	}
	
	/**
	 * Sets whether the image should flip vertically 
	 * @param flipY true indicates it flips else it does not
	 */
	public void setFlipY(boolean flipY)
	{
		_flipY = flipY;
	}

	// Overriding the compare method to sort the layer
	public int compare(RenderComponent d, RenderComponent d1)
	{
	  return d.getSortingLayer() - d1.getSortingLayer();
	}
	

	// Overriding the compareTo method to sort the layer
	@Override
	public int compareTo(RenderComponent comp) 
	{
		return _sortingLayer - comp.getSortingLayer();
	}
	
	@Override
	public void awake() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updated(float deltaTime) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void destroyed() 
	{
		// TODO Auto-generated method stub
		_renderInfo.clean();
	}
}
