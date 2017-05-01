package com.mygdx.game.engine.entities.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.EntitySystem;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.EngineTweener;
import com.mygdx.game.engine.tweening.TransformAccessor;

import aurelienribon.tweenengine.Tween;

/**
 * This component contains the orientation information of the entity it is attached to.
 * (Position, Rotation, Scale)
 * @author Ramses Di Perna
 *
 */
public class TransformComponent extends BaseEntityComponent
{	
	private TransformComponent _parent = null;
	private ArrayList<TransformComponent> _childeren = new ArrayList<TransformComponent>();
	
	private Vector2 _position = new Vector2(0, 0);
	private float _rotation = 0;
	private Vector2 _scale = new Vector2(1, 1);
	
	/**
	 * Returns the X position of the entity in local (parent) space.
	 * @return X value of the position Vector2.
	 */
	public float getLocalPositionX()
	{
		return _position.x;
	}
	
	/**
	 * Returns the Y position of the entity in local (parent) space.
	 * @return Y value of the position Vector2
	 */
	public float getLocalPositionY()
	{
		return _position.y;
	}
	
	/**
	 * Returns the rotation of the current entity in Degrees in local (parent) space.
	 * @return Rotation value in degrees.
	 */
	public float getLocalRotation()
	{
		return _rotation;
	}
	
	/**
	 * Returns the X Scale of the entity in local (parent) space.
	 * @return X value of the Scale Vector2
	 */
	public float getLocalScaleX()
	{
		return _scale.x;
	}
	/**
	 * Returns the Y Scale of the entity in local (parent) space.
	 * @return Y value of the Scale Vector2
	 */
	public float getLocalScaleY()
	{
		return _scale.y;
	}
	
	/**
	 * Returns the X position of the entity in world space.
	 * @return X value of the position Vector2.
	 */
	public float getPositionX()
	{
		float p = this.getLocalPositionX();
		
		if(this.getParent() != null)
		{
			Vector2 diffVec = new Vector2(this.getLocalPositionX(), this.getLocalPositionY());
			diffVec.setAngle(-(_parent.getRotation() - 90));
			p  = _parent.getPositionX() + diffVec.x;
		}
		
		return p;
	}
	
	/**
	 * Returns the Y position of the entity in world space.
	 * @return Y value of the position Vector2
	 */
	public float getPositionY()
	{
		float p = this.getLocalPositionY();
		
		if(this.getParent() != null)
		{
			Vector2 diffVec = new Vector2(this.getLocalPositionX() * _parent.getScaleX(), this.getLocalPositionY() * _parent.getScaleY());
			diffVec.setAngle(-(_parent.getRotation() - 90));
			p  = _parent.getPositionY() + diffVec.y;
		}
		
		return p;
	}
	
	/**
	 * Returns the rotation of the current entity in Degrees in world space.
	 * @return Rotation value in degrees.
	 */
	public float getRotation()
	{
		float r = this.getLocalRotation();
		
		if(this.getParent() != null)
		{
			r  += _parent.getRotation();
		}
		
		return r;
	}
	
	/**
	 * Returns the X Scale of the entity in world space.
	 * @return X value of the Scale Vector2
	 */
	public float getScaleX()
	{
		float s = this.getLocalScaleX();
		
		if(this.getParent() != null)
		{
			s  *= _parent.getScaleX();
		}
		
		return s;
	}
	/**
	 * Returns the Y Scale of the entity in world space.
	 * @return Y value of the Scale Vector2
	 */
	public float getScaleY()
	{
		float s = this.getLocalScaleY();
		
		if(this.getParent() != null)
		{
			s  *= _parent.getScaleY();
		}
		
		return s;
	}
	
	/**
	 * Returns all the childeren added to this TransformComponent
	 * @return List of childeren TransformComponents.
	 */
	public TransformComponent[] getChilderen()
	{
		TransformComponent[] tcs = new TransformComponent[_childeren.size()];
		for(int i = 0; i < tcs.length; i++)
		{
			tcs[i] = _childeren.get(i);
		}
		return tcs;
	}
	
	/**
	 * Gets the parent for the position of this transform component
	 * @return The TransformComponent this component is attached to.
	 */
	public TransformComponent getParent()
	{
		return _parent;
	}
	
	/**
	 * Sets the parent for the position of this transform component
	 * @return The TransformComponent this component is attached to.
	 */
	public void setParent(TransformComponent parent)
	{
		if(_parent == parent) { return; }
		
		if(_parent != null)
			_parent._childeren.remove(this);
		
		_parent = parent;
		if(_parent != null)
			_parent._childeren.add(this);
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
	 * Tweens the entity to the given position in the given duration. 
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param x Position to tween to
	 * @param y Position to tween to
	 * @param duration in seconds on how long to take until reaching the position.
	 * @return The Tween which will be executed.
	 */
	public EngineTween doPosition(float x, float y, float duration)
	{
		return startTweenOnComponent(Tween.to(this, TransformAccessor.POSITION, duration).target(x, y));
	}
	
	/**
	 * Tweens the entity to the given rotation in the given duration. 
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param degrees To lerp to
	 * @param duration in seconds on how long to take until reaching the rotation.
	 * @param shortestRotation indicates whether it should take a long or a short turn when dealing with from 350 -> 10. True means rotating right and false means rotating left.
	 * @return The Tween which will be executed.
	 */
	public EngineTween doRotation(float degrees, float duration, boolean shortestRotation)
	{
		float newValue = (!shortestRotation) ? degrees : this.getLocalRotation() + getShortestAngleDistance(this.getLocalRotation(), degrees, 1);
		return startTweenOnComponent(Tween.to(this, TransformAccessor.ROTATION, duration).target(newValue));
	}
	
	/**
	 * Tweens the entity to the given scale in the given duration. 
	 * The Tween can still be modified by the return value
	 * The Tween will be updated in the Component channel. (EngineTweener)
	 * @param x scale to tween to
	 * @param y scale to tween to
	 * @param duration in seconds on how long to take until reaching the scale.
	 * @return The Tween which will be executed.
	 */
	public EngineTween doScale(float x, float y, float duration)
	{
		return startTweenOnComponent(Tween.to(this, TransformAccessor.SCALE, duration).target(x, y));
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
		_position.x = newPosValue.x;
		_position.y = newPosValue.y;
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
		_scale.x = newScaleValue.x;
		_scale.y = newScaleValue.y;
	}
	
	@Override
	public void awake() 
	{
		
	}

	@Override
	protected void updated(float deltaTime) 
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void destroyed() 
	{
		for(int i = _childeren.size() - 1; i >= 0; i--)
		{
			if(_childeren.get(i) != null)
				EntitySystem.getInstance().destroyEntityImmediately(_childeren.get(i).getParentOfComponent());
		}
		this.setParent(null);
		_childeren.clear();
		_childeren = null;
		_position = null;
		_scale = null;
		
		EntitySystem.getInstance().destroyEntityImmediately(this.getParentOfComponent());
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

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub
		
	}
}
