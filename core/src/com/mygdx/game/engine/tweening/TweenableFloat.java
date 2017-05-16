package com.mygdx.game.engine.tweening;

/**
 * This class represent a referenceable float needed in the EngineTweener to be able to change it during a tween task.
 * This should be used in [component].doFloat(); <--
 * @author Ramses Di Perna
 *
 */
public class TweenableFloat 
{	
	private float _value;
	
	/**
	 * Gives the float its start value
	 * @param value to start the float with.
	 */
	public TweenableFloat(float value)
	{
		setValue(value);
	}
	
	/**
	 * Gets the current value of the float
	 * @return Current value of float.
	 */
	public float getValue()
	{
		return _value;
	}
	
	/**
	 * Sets the current value of the float
	 * @param value to set the float to.
	 */
	public void setValue(float value)
	{
		_value = value;
	}
}
