package com.mygdx.game.entities.components;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.BaseEntityComponent;

/**
 * This component contains the orientation information of the entity it is attached to.
 * (Position, Rotation, Scale)
 * @author Ramses Di Perna
 *
 */
public class TransformComponent extends BaseEntityComponent 
{	
	private Vector2 _position = new Vector2();
	private float _rotation = 0;
	private Vector2 _scale = new Vector2();
	
	/**
	 * Returns the X position of the entity.
	 * @return X value of the position Vector2.
	 */
	public float getPositionX()
	{
		return _position.x;
	}
	
	/**
	 * Returns the Y position of the entity
	 * @return Y value of the position Vector2
	 */
	public float getPositionY()
	{
		return _position.y;
	}
	
	/**
	 * Returns the rotation of the current entity in Degrees
	 * @return Rotation value in degrees.
	 */
	public float getRotation()
	{
		return _rotation;
	}
	
	/**
	 * Returns the X Scale of the entity.
	 * @return X value of the Scale Vector2
	 */
	public float getScaleX()
	{
		return _scale.x;
	}
	/**
	 * Returns the Y Scale of the entity.
	 * @return Y value of the Scale Vector2
	 */
	public float getScaleY()
	{
		return _scale.y;
	}
	
	public void translatePosition(Vector2 deltaToPosition)
	{
		deltaToPosition.x += _position.x;
		deltaToPosition.y += _position.y;
		setPosition(deltaToPosition);
	}
	
	/**
	 * Sets the position to a new value
	 * @param newPosValue overwriting the x and y position of the entity.
	 */
	public void setPosition(Vector2 newPosValue)
	{
		_position = newPosValue;
	}
	
	/**
	 * Sets the rotation to a new value in Degrees
	 * @param angleInDeg overwriting the angle of the entity in degrees. 
	 */
	public void setRotation(float angleInDeg)
	{
		_rotation = angleInDeg;
	}
	
	/**
	 * Sets the scale to a new value
	 * @param newScaleValue overwriting the x and y scale of the entity
	 */
	public void setScale(Vector2 newScaleValue)
	{
		_scale = newScaleValue;
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
