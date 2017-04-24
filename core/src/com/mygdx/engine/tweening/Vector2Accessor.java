package com.mygdx.engine.tweening;

import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.TweenAccessor;

public class Vector2Accessor implements TweenAccessor<Vector2>
{
	public static final int CHANGE = 0;
	
	@Override
	public int getValues(Vector2 target, int tweenType, float[] returnValues) 
	{
		switch(tweenType)
		{
		case CHANGE:
			returnValues[0] = target.x;
			returnValues[1] = target.y;
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Vector2 target, int tweenType, float[] newValues) 
	{
		switch(tweenType)
		{
		case CHANGE:
			target.x = newValues[0];
			target.y = newValues[1];
 			break;
		default:
			assert false;
			break;
		}
	}

}
