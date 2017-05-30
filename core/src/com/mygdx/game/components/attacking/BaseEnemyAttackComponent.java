package com.mygdx.game.components.attacking;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;

/**
 * This is the base component of enemy attack logics. A specific handling of enemy attack behavior should derive from this class to be used by the Enemy class
 * @author Ramses Di Perna
 *
 */
public abstract class BaseEnemyAttackComponent extends BaseEntityComponent 
{
	public static final String STARTED_ATTACKING_EVENT = "StartedAttackingEvent";
	public static final String STOPPED_ATTACKING_EVENT = "StoppedAttackingEvent";
	
	private AnimationComponent _animationComponent;
	private float _damage;
	private int _rangeInTiles;
	
	public BaseEnemyAttackComponent(float dmg, int rangeInTiles)
	{
		_damage = dmg;
		_rangeInTiles = rangeInTiles;
	}
	
	/**
	 * Returns whether this component is in its attack handeling after the 'attack' method was called
	 * @return true if its attacking, else false.
	 */
	public abstract boolean isAttacking();
	
	/**
	 * Requests the component to attack the given HealthComponent
	 * @param hc is the health component to attack
	 */
	public void attack(HealthComponent hc)
	{
		if(this.isAttacking()) { return; }
		attackCall(hc);
		this.dispatchEvent(new Event(STARTED_ATTACKING_EVENT));
	}
	
	/**
	 * Stops the attacking on the healthcomponent given in the 'attack' method
	 */
	public void stopAttacking()
	{
		if(!this.isAttacking()) { return; }
		stopAttackingCall();
		this.dispatchEvent(new Event(STOPPED_ATTACKING_EVENT));
	}
	
	/**
	 * Returns the amount of tiles this component has attacking range to. 
	 * @return The attack range in y tiles as integer
	 */
	public int getRangeInTiles()
	{
		return _rangeInTiles;
	}
	
	/**
	 * Returns the damage each attack would inflict on a health component
	 * @return the damage this component would do with each attack as float
	 */
	public float getDamage()
	{
		return _damage;
	}
	
	/**
	 * Sets the animation component this component can use to visualize the attacking motion
	 * @param animationComponent to give this component to visualize the attack with
	 */
	public void setAnimationComponent(AnimationComponent animationComponent)
	{
		_animationComponent = animationComponent;
	}
	
	/**
	 * Returns the animationComponent given to this component in the 'setAnimationComponent' to use to visualize the attacking with
	 * @return The AnimationComponent given using the 'setAnimationComponent' method
	 */
	protected AnimationComponent getAnimationComponent()
	{
		return _animationComponent;
	}
	
	protected void destroyed()
	{
		stopAttacking();
		_animationComponent = null;
	}
	
	/**
	 * Is called when an attack request is put on the component
	 * @param hc is the HealthComponent to attack.
	 */
	protected abstract void attackCall(HealthComponent hc);
	/**
	 * Is called when an stop attacking request is put on the component
	 */
	protected abstract void stopAttackingCall();
}
