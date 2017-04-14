package com.mygdx.game.gameSpecifics.components;

import com.mygdx.game.entities.BaseEntityComponent;
import com.mygdx.game.gameSpecifics.events.HealthEvent;

/**
 * This component holds all the info for health.
 * It can be damaged and healed. It also dispatches events on these occasions
 * @author Ramses Di Perna
 *
 */
public class HealthComponent extends BaseEntityComponent 
{
	/**
	 * This event type is fired by the HealthComponent when it is healed
	 */
	public static final String EVENT_HEALTH_HEALED = "EventHealthHealed";
	/**
	 * This event type is fired by the HealthComponent when it is damaged
	 */
	public static final String EVENT_HEALTH_DAMAHED = "EventHealthDamaged";
	/**
	 * This event type is fired by the HealthComponent when it is killed
	 */
	public static final String EVENT_HEALTH_DIED = "EventHealthDied";
	
	private float _maxHealth;
	private float _currentHealth;
	
	/**
	 * This will set the maximum health and current health for the component.
	 * @param health
	 */
	public HealthComponent(float health)
	{
		_maxHealth = _currentHealth = health;
	}
	
	/**
	 * Returns the normalized value of the current health in relation to the MaxHealth
	 * @return
	 */
	public float getNormalizedHealth()
	{
		return _currentHealth / _maxHealth;
	}
	
	/**
	 * Damages the component, affecting the health amount in a negative way.
	 * This will also trigger the EVENT_HEALTH_DAMAHED and, when the component has no health left, EVENT_HEALTH_DIED
	 * The component can not be damaged when it is already dead. 
	 * @param amount to take off of the health
	 */
	public void damage(float amount)
	{
		if(amount > _currentHealth)
		{
			amount = _currentHealth;
		}
		
		if(amount <= 0 || !isAlive()) {return;}
		
		_currentHealth -= amount;
		
		this.dispatchEvent(new HealthEvent(EVENT_HEALTH_DAMAHED, amount, _currentHealth));
		
		if(_currentHealth == 0)
		{
			this.dispatchEvent(new HealthEvent(EVENT_HEALTH_DIED, amount, _currentHealth));
		}
	}
	
	/**
	 * Heals the component, affecting the health amount in a positive way.
	 * This will also trigger the EVENT_HEALTH_HEALED.
	 * The component can not be healed when it is dead or when it has full health. 
	 * @param amount to add to the current health
	 */
	public void heal(float amount)
	{
		if(amount + _currentHealth > _maxHealth)
		{
			amount = _maxHealth - _currentHealth;
		}

		if(amount <= 0 || !isAlive()) {return;}
		
		this.dispatchEvent(new HealthEvent(EVENT_HEALTH_HEALED, amount, _currentHealth));
		
		_currentHealth += amount;
	}
	
	/**
	 * Calls the Damage() method with enough damage to kill the component.
	 */
	public void kill()
	{
		damage(_currentHealth);
	}
	
	/**
	 * Returns whether the component still has health left.
	 * @return If the health is not below or equal to 0. If it is it returns false, else it returns true.
	 */
	public boolean isAlive()
	{
		return _currentHealth > 0;
	}
	
	/**
	 * Returns the current health value of the component
	 * @return Current health
	 */
	public float getHealth()
	{
		return _currentHealth;
	}
	
	/**
	 * Returns the maximum health this component can have
	 * @return The Max Health.
	 */
	public float getMaxHealth()
	{
		return _maxHealth;
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updated(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub

	}

}
