package com.mygdx.game.entities.components.Rendering;

import com.badlogic.gdx.graphics.Color;
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
	private boolean _isUserInterface = false;
	private Color _color = new Color(Color.WHITE);
	
	
	public RenderComponent(RenderInfo startRenderInfo, boolean isUI)
	{
		this.setRenderInfo(startRenderInfo);
		_isUserInterface = isUI;
	}
	
	public boolean isUserInterface()
	{
		return _isUserInterface;
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
	public void setPivot(Vector2 newPivotValue, boolean keepPosition)
	{
		if(keepPosition)
		{
			Vector2 differents = new Vector2(_pivot.x, _pivot.y).sub(newPivotValue);
			Vector2 newDelta = this.getParentOfComponent().getTransformComponent().getUpwards();
			newDelta = new Vector2((-this.getRealWidth() * differents.x) * this.getParentOfComponent().getTransformComponent().getRight().x, (-this.getRealWidth() * differents.x) * this.getParentOfComponent().getTransformComponent().getRight().y);
			newDelta.x += this.getParentOfComponent().getTransformComponent().getUpwards().x * (-this.getRealHeight() * differents.y);
			newDelta.y += this.getParentOfComponent().getTransformComponent().getUpwards().y * (-this.getRealHeight() * differents.y);
			this.getParentOfComponent().getTransformComponent().translatePosition(newDelta);
		}
		_pivot = new Vector2(newPivotValue.x, newPivotValue.y);
	}
	
	/**
	 * Gets the RenderInfo set in the RenderComponent to use for display.
	 * @return RenderInfo of the RenderComponent
	 */
	public RenderInfo getRenderInfo()
	{
		return _renderInfo;
	}
	
	/**
	 * This sets the alpha of the Color which is modifying the current entity.
	 * @param alpha to set the current modifying Color to.
	 */
	public void setAlpha(float alpha)
	{
		_color.a = alpha;
	}
	
	/**
	 * This gets the alpha of the Color which is modifying the current entity.
	 * @return alpha of the current modifying color.
	 */
	public float getAlpha()
	{
		return _color.a;
	}
	
	/**
	 * This sets the color which is modifying the current entity.
	 * The color White equals its original color 
	 * @param color to set the value to.
	 */
	public void setColor(Color color)
	{
		_color = new Color(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * This gets the color which is modifying the current entity.
	 * The color White equals its original color 
	 * @return The color this entity has as modification color
	 */
	public Color getColor()
	{
		return new Color(_color.r, _color.g, _color.b, _color.a);
	}
	
	/**
	 * Sets the current render info and cleans the previous one.
	 * @param info to set as new RenderInfo for this component.
	 */
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
		_pivot = null;
		_renderInfo = null;
	}
}
