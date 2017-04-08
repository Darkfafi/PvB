package com.mygdx.game.entities.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.BaseEntityComponent;

/**
 * This component contains the 2D render information of the entity it is attached to.
 * (Texture to render, pivot point, height and width)
 * @author Ramses Di Perna
 *
 */
public class RenderComponent extends BaseEntityComponent 
{
	private Texture _currentTexture = null;
	private Vector2 _pivot = new Vector2(0.5f, 0.5f);
	
	/**
	 * Gets the width of the texture * the scaleX of the entity
	 * @return The real width calculated with the texture width.
	 */
	public float getRealWidth()
	{
		return this.getParentOfComponent().getTransformComponent().getScaleX() * _currentTexture.getWidth();
	}
	
	/**
	 * Gets the height of the texture * the scaleY of the entity
	 * @return The real height calculated with the texture height.
	 */
	public float getRealHeight()
	{
		return this.getParentOfComponent().getTransformComponent().getScaleY() * _currentTexture.getHeight();
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
	 * Gets the current texture being rendered.
	 * @return The texture currently displayed on screen.
	 */
	public Texture getCurrentTexture()
	{
		return _currentTexture;
	}
	
	/**
	 * Sets the current texture to render
	 * @param texture to set as new texture to render on screen.
	 */
	public void setCurrentTexture(Texture texture)
	{
		_currentTexture = texture;
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

	}

}
