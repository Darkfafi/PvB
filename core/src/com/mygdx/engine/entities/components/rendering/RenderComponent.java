package com.mygdx.engine.entities.components.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.entities.components.BaseEntityComponent;
import com.mygdx.engine.tweening.EngineTweener;
import com.mygdx.engine.tweening.RenderAccessor;

import aurelienribon.tweenengine.Tween;

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
	private float _innerSortingLayer = 0f;
	private boolean _isUserInterface = false;
	private boolean _isSortedOnY = false;
	private Color _color = new Color(Color.WHITE);
	
	public RenderComponent(RenderInfo startRenderInfo, boolean isUI)
	{
		this.setRenderInfo(startRenderInfo);
		_isUserInterface = isUI;
	}
	
	/**
	 * Returns whether this is rendered on the main camera or the hud camera
	 * @return returns false when it is rendered on the main camera and true when rendered on the hud camera
	 */
	public boolean isUserInterface()
	{
		return _isUserInterface;
	}
	
	/**
	 * Indicates whether the renderComponent is rendered in its layer on its y position.
	 * @return true if its sorted on y position, else it returns false.
	 */
	public boolean isSortedOnY()
	{
		return _isSortedOnY;
	}
	
	/**
	 * Sets if the entity should be rendered on its Y position. 
	 * @param sortOnY true means it will be sorted on Y position, else it will not. 
	 */
	public void setSortOnY(boolean sortOnY)
	{
		if(_isSortedOnY == sortOnY) { return; }
		_isSortedOnY = sortOnY;
		if(!_isSortedOnY)
			_innerSortingLayer = 0f;
	}
	
	/**
	 * Returns the sorting value in the entity its layer which it is sorted on.
	 * @return The higher the value the later it is rendered (The opposite of the normal Sort Layer)
	 */
	public float getInnerSortingLayer()
	{
		return _innerSortingLayer;
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
	 * Tweens the entity to the given alpha in the given duration.  
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param value indicates the alpha to tween to. 1 is fully visible and 0 is not visible
	 * @param duration in seconds on how long to take until reaching the alpha value
	 * @returnThe Tween which will be executed.
	 */
	public Tween doAlpha(float value, float duration)
	{
		return EngineTweener.startTween(Tween.to(this, RenderAccessor.ALPHA, duration), EngineTweener.COMPONENT_CHANNEL).target(value);
	}
	
	/**
	 * Tweens the entity to the given color in the given duration.  
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param color indicates the color to tween to.
	 * @param duration in seconds on how long to take until reaching the color value
	 * @returnThe Tween which will be executed.
	 */
	public Tween doColor(Color color, float duration)
	{
		return EngineTweener.startTween(Tween.to(this, RenderAccessor.COLOR, duration), 
				EngineTweener.COMPONENT_CHANNEL).target(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * This sets the color which is modifying the current entity.
	 * The color White equals its original color 
	 * @param color to set the value to.
	 */
	public void setColor(Color color)
	{
		_color.r = color.r;
		_color.g = color.g;
		_color.b = color.b;
		_color.a = color.a;
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
		_renderInfo = info;
		
		if(_renderInfo != null)
			_renderInfo.setCurrentFrameInfo(0);
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
		int value = (int)Math.floor(((d.getSortingLayer()+ 0.95f) - d.getInnerSortingLayer()) - ((d1.getSortingLayer()+ 0.95f) - d1.getInnerSortingLayer()));
	  	value = (value == 0) ? 1 : value;
		return value;
	}
	

	// Overriding the compareTo method to sort the layer
	@Override
	public int compareTo(RenderComponent comp) 
	{
		int value = (int)Math.floor(((_sortingLayer + 0.95f) - _innerSortingLayer) - ((comp.getSortingLayer() + 0.95f) - comp.getInnerSortingLayer()));
		value = (value == 0) ? 1 : value;
		return value;
	}
	
	@Override
	public void awake() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updated(float deltaTime) 
	{
		if(isSortedOnY())
		{
			this.setInnerSortingLayer(this.getParentOfComponent().getTransformComponent().getPositionY() * 0.0001f);
		}
	}

	@Override
	protected void destroyed() 
	{
		_pivot = null;
		_renderInfo = null;
		_color = null;
	}
	
	/**
	 * Sets the inner layer sorting value and clamps it between 0 and 0.95f.
	 * @param value indicates the depth the entity should be rendered in its layer. The higher the value, the later it will be rendered.
	 */
	private void setInnerSortingLayer(float value)
	{
		if(value < 0)
			value = 0;
		if(value > 0.95f)
			value = 0.95f;
		
		_innerSortingLayer = value;
	}

	@Override
	protected void activeStateChanged() 
	{
		// TODO Auto-generated method stub
		
	}
}
