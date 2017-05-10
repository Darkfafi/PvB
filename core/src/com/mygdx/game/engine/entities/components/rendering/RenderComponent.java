package com.mygdx.game.engine.entities.components.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.TransformComponent;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.RenderAccessor;

import aurelienribon.tweenengine.Tween;

/**
 * This component contains the 2D render information of the entity it is attached to.
 * (Texture to render, pivot point, height and width)
 * @author Ramses Di Perna
 *
 */
public class RenderComponent extends BaseEntityComponent implements Comparable<RenderComponent>
{
	private RenderInfo _renderInfo = null;
	private int _currentFrameInfo = 0;
	private Vector2 _pivot = new Vector2(0.5f, 0.5f);
	private boolean _flipX = false;
	private boolean _flipY = false;
	private int _sortingLayer = 0;
	private double _innerSortingLayer = 0f;
	private boolean _isUserInterface = false;
	private boolean _isSortedOnY = false;
	private Color _color = new Color(Color.WHITE);
	private boolean _callRenderMethodAfter = true;
	
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
	 * * Returns what frame info index should be displayed
	 * @return The index of the current frame info
	 */
	public int getCurrentFrameInfo()
	{
		return _currentFrameInfo;
	}
	
	/**
	 * Sets the frame info index which should be displayed
	 * @param index is the index of the current frame info
	 */
	public void setCurrentFrameInfo(int index)
	{
		_currentFrameInfo = index;
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
	public double getInnerSortingLayer()
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
		int value = _sortingLayer;
		
		
		return value;
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
		if(_renderInfo == null) { return 0;}
		return this.getParentOfComponent().getTransformComponent().getScaleX() * (float)_renderInfo.getCutWidth(_currentFrameInfo);
	}
	
	/**
	 * Gets the height of the texture * the scaleY of the entity
	 * @return The real height calculated with the texture height.
	 */
	public float getRealHeight()
	{
		if(_renderInfo == null) { return 0;}
		return this.getParentOfComponent().getTransformComponent().getScaleY() * (float)_renderInfo.getCutHeight(_currentFrameInfo);
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
	 * Returns If true, that means the 'Rendered' method in the Entity is called after this component is rendered. Else, if false, it will be called before
	 * @return True if rendered is called after, else false
	 */
	public boolean getCallsRenderAfterEntityRender()
	{
		return _callRenderMethodAfter;
	}
	
	/**
	 * Sets render order.
	 * If true, that means the 'Rendered' method in the Entity is called after this component is rendered. Else, if false, it will be called before
	 * @param renderAfterEntityRender means: True, it will call the 'rendered' method after the entity is rendered, else false
	 */
	public void setCallsRenderAfterEntityRender(boolean renderAfterEntityRender)
	{
		_callRenderMethodAfter = renderAfterEntityRender;
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
	 * @param gameTime means, when set to true, this tween will be effected by the Time Scale of the game, else it will not (False recommended for UI elements)
	 * @returnThe Tween which will be executed.
	 */
	public EngineTween doAlpha(float value, float duration, boolean gameTime)
	{
		return this.startTweenOnComponent(Tween.to(this, RenderAccessor.ALPHA, duration).target(value), gameTime);
	}
	
	/**
	 * Tweens the entity to the given color in the given duration.  
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param color indicates the color to tween to.
	 * @param duration in seconds on how long to take until reaching the color value
	 * @param gameTime means, when set to true, this tween will be effected by the Time Scale of the game, else it will not (False recommended for UI elements)
	 * @returnThe Tween which will be executed.
	 */
	public EngineTween doColor(Color color, float duration, boolean gameTime)
	{
		return this.startTweenOnComponent(Tween.to(this, RenderAccessor.COLOR, duration).target(color.r, color.g, color.b, color.a), gameTime);
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
		_currentFrameInfo  = 0;	
		_renderInfo = info;
	}
	
	/**
	 * Gets the current texture being rendered.
	 * @return The texture currently displayed on screen.
	 */
	public Texture getCurrentTexture()
	{
		if(_renderInfo == null) { return null;}
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
		if(d.isDestroyed()) { return -1; }
		int value = compare(d.getCompareValue(), d1.getCompareValue());
		return value;
	}
	

	// Overriding the compareTo method to sort the layer
	@Override
	public int compareTo(RenderComponent comp) 
	{
		if(this.isDestroyed() || comp == null) { return 0; }
		int value = compare(getCompareValue(), comp.getCompareValue());
		return value;
	}
	
	@Override
	public void awake() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void updated(float deltaTime) 
	{
		if(isSortedOnY())
		{
			this.setInnerSortingLayer(this.getParentOfComponent().getTransformComponent().getPositionY() * 0.000001);
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
	private void setInnerSortingLayer(double value)
	{
		if(value < 0)
			value = 0;
		if(value >= 1)
			value = 0.999999999999999999;
		
		_innerSortingLayer = value;
	}

	@Override
	protected void activeStateChanged() 
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns the sort value of this RenderComponent
	 * @return Compare value as Double
	 */
	private double getCompareValue()
	{
		int sl = _sortingLayer;
		TransformComponent parent = this.getParentOfComponent().getTransformComponent().getParent();
		RenderComponent rc = null;
		if(parent != null)
		{
			rc = parent.getParentOfComponent().getComponent(RenderComponent.class);
			if(rc == null)
			{
				rc = parent.getParentOfComponent().getComponent(AnimationComponent.class);
			}
			
			if(rc != null)
			{
				sl += rc.getSortingLayer();
			}
		}
		
		return (((this.isUserInterface() ? 999f : 0) + sl + 0.95) - _innerSortingLayer);
	}
	
	/**
	 * The compare logics for the sorting algorithm
	 * @param d1 first value to compare
	 * @param d2 second value to compare
	 * @return 1 if d1 is preferred, else -1
	 */
	private int compare(double d1, double d2)
	{
         if (d1 < d2)
             return -1;		// Neither val is NaN, thisVal is smaller
         if (d1 > d2)
             return 1;     	// Neither val is NaN, thisVal is larger
 
         long thisBits = Double.doubleToLongBits(d1);
         long anotherBits = Double.doubleToLongBits(d2);
 
         return (thisBits == anotherBits ?  0 : // Values are equal
                 (thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                  1));                          // (0.0, -0.0) or (NaN, !NaN)
	}
}
