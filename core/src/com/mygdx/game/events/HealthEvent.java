package com.mygdx.game.events;

import com.mygdx.game.engine.events.Event;

/**
 * This class and all events extending this class represent events which involve health.
 * @author Ramses Di Perna
 *
 */
public class HealthEvent extends Event 
{
	private float _healthChanged;
	private float _newHealth;
	
	public HealthEvent(String type, float healthChanged, float newHealth) {
		super(type);
		_newHealth = newHealth;
		_healthChanged = healthChanged;
	}
	
	/**
	 * Returns the amount of health changed to the original health value
	 * @return delta of change
	 */
	public float getHealthChanged()
	{
		return _healthChanged;
	}
	
	/**
	 * Returns the new health value which the delta caused
	 * @return new health value.
	 */
	public float getNewHealth()
	{
		return _newHealth;
	}
	
}
