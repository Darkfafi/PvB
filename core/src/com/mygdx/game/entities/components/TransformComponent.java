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
	private Vector2 _position = new Vector2(0, 0);
	private float _rotation = 0;
	private Vector2 _scale = new Vector2(1, 1);
	
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
	
	/**
	 * Gets the Upward vector of the current transform and its rotation
	 * @return normalized upward vector for current rotation
	 */
	public Vector2 getUpwards()
	{
		Vector2 v = new Vector2(0, 1);
		v.setAngle((-getRotation()) + 90);
		return v.nor();
	}
	
	/**
	 * Gets the Right vector of the current transform and its rotation
	 * @return normalized right vector for current rotation
	 */
	public Vector2 getRight()
	{
		Vector2 v = getUpwards();
		v.setAngle(v.angle() - 90);
		return v.nor();
	}
	
	/**
	 * Moves the entity with the given delta amount
	 * @param delta to add to the current position
	 */
	public void translatePosition(Vector2 delta)
	{
		Vector2 deltaToPosition = new Vector2(delta.x, delta.y);
		deltaToPosition.x += this.getPositionX();
		deltaToPosition.y += this.getPositionY();
		setPosition(deltaToPosition);
	}
	
	/**
	 * Rotates the entity with the given delta amount
	 * @param deltaRotation to add to the current rotation
	 */
	public void translateRotation(float deltaRotation)
	{
		setRotation(_rotation + deltaRotation);
	}
	
	public void translateScale(Vector2 delta)
	{
		Vector2 deltaToScale = new Vector2(delta.x, delta.y);
		deltaToScale.x += this.getScaleX();
		deltaToScale.y += this.getScaleY();
		this.setScale(deltaToScale);	
	}
	
	/**
	 * Makes the Entity which this component is attached to change its angle to look at the given positionToLookAt
	 * @param posToLookAt which will be the target to turn towards
	 * @param percentageTowardsLocation is a value between 0 and 1 with 1 being fully turned to the posToLookAt and 0 being not at all.
	 */
	public void lookAt(Vector2 posToLookAt, float percentageTowardsLocation)
	{
		Vector2 posToLookAtCopy = new Vector2(posToLookAt.x, posToLookAt.y);
		Vector2 difference = posToLookAtCopy.sub(_position);
		difference.nor();
		float angleToTarget =  -((float) Math.toDegrees(Math.atan2(difference.y, difference.x)) - 90);
		float newAngle = getRotation() + getShortestAngleDistance(getRotation(), doAngleCirclation(angleToTarget), percentageTowardsLocation);
		this.setRotation(newAngle);
	}
	
	/**
	 * Sets the position to a new value
	 * @param newPosValue overwriting the x and y position of the entity.
	 */
	public void setPosition(Vector2 newPosValue)
	{
		_position = new Vector2(newPosValue.x, newPosValue.y);
	}
	
	/**
	 * Sets the rotation to a new value in Degrees
	 * @param angleInDeg overwriting the angle of the entity in degrees. 
	 */
	public void setRotation(float angleInDeg)
	{
		_rotation = doAngleCirclation(angleInDeg);
	}
	
	/**
	 * Sets the scale to a new value
	 * @param newScaleValue overwriting the x and y scale of the entity
	 */
	public void setScale(Vector2 newScaleValue)
	{
		_scale = new Vector2(newScaleValue.x, newScaleValue.y);
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
		_position = null;
		_scale = null;
	}
	
	private float getShortestAngleDistance(float angleOne, float angleTwo, float lerpAmount)
	{
		float shortestAngle = ((((angleTwo - angleOne) % 360) + 540) % 360) - 180;
	    return shortestAngle * lerpAmount;
	}
	
	private float doAngleCirclation(float angle)
	{
		while(angle >= 360)
			angle -= 360;
		
		while(angle < 0)
			angle += 360;
		
		return angle;
	}
}
