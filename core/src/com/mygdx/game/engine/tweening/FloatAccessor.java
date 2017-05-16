package com.mygdx.game.engine.tweening;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * This Accessor handles / contains all the tweening types for the TweenableFloat.
 * @author Ramses Di Perna
 *
 */
public class FloatAccessor  implements TweenAccessor<TweenableFloat>
{
	/**
	 * Change type the value of the float
	 */
	public static final int CHANGE = 0;
	
	@Override
	public int getValues(TweenableFloat target, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
		case CHANGE:
			returnValues[0] = target.getValue();
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(TweenableFloat target, int tweenType, float[] newValues) 
	{
		switch(tweenType)
		{
		case CHANGE:
			target.setValue(newValues[0]);
			break;

		default:
			assert false;
			break;
		}
	}
	
}
