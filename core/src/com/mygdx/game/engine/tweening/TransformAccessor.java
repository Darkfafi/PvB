package com.mygdx.game.engine.tweening;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.components.TransformComponent;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * This Accessor handles / contains all the tweening types for the TransformComponent.
 * @author Ramses Di Perna
 *
 */
public class TransformAccessor implements TweenAccessor<TransformComponent>
{
	public static final int POSITION = 0;
	public static final int ROTATION = 1;
	public static final int SCALE = 2;
	
	@Override
	public int getValues(TransformComponent target, int tweenType, float[] returnValues) 
	{
		switch(tweenType)
		{
		case POSITION:
			returnValues[0] = target.getLocalPositionX();
			returnValues[1] = target.getLocalPositionY();
			return 2;
		case ROTATION:
			returnValues[0] = target.getLocalRotation();
			return 1;
		case SCALE:
			returnValues[0] = target.getLocalScaleX();
			returnValues[1] = target.getLocalScaleY();
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(TransformComponent target, int tweenType, float[] newValues) 
	{
		switch(tweenType)
		{
		case POSITION:
			target.setPosition(new Vector2(newValues[0], newValues[1]));
			break;
		case ROTATION:
			target.setRotation(newValues[0]);
			break;
		case SCALE:
			target.setScale(new Vector2(newValues[0], newValues[1]));
			break;
		default:
			assert false;
			break;
		}
	}
}
