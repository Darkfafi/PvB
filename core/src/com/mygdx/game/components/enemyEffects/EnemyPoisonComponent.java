package com.mygdx.game.components.enemyEffects;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

/**
 * This component start a poison effect (including the green overlay) on the Enemy entity it is attached to when the 'poison' method is called.
 * This effect will stop when the effect duration has ended or when the 'cure' method is called
 * @author Ramses Di Perna
 *
 */
public class EnemyPoisonComponent extends BaseEntityComponent implements IEventReceiver
{
	private Enemy _enemy;
	private HealthComponent _hc;
	private float _duration = -1;
	private int _damageTickAmount = 0;
	private boolean _removeComponentOnCure = true;
	private int _tool;
	
	// Calculations
	private float _tikDuration = 0;
	private float _tikDamage= 0;
	
	// trackers
	
	private float _currentDamageTikWait = 0;
	
	/**
	 * The constructor of the poison component requires the following info
	 * @param removeComponentOnCure causes, when is set to true, that the component will be destroyed after the poison effect has been done, else it will stay on the entity
	 * @param killTool means when the enemy is killed by the poison, what KillTool it should represent to the HitRegistrationPoint
	 */
	public EnemyPoisonComponent(boolean removeComponentOnCure, int killTool)
	{
		_removeComponentOnCure = removeComponentOnCure;
		_tool = killTool;
	}
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HealthComponent.EVENT_HEALTH_DAMAGED)
		{
			if(((HealthEvent)event).getNewHealth() == 0)
			{
				this.cure();
			}
		}
	}
	
	@Override
	public void awake() 
	{
		_enemy = (Enemy)this.getParentOfComponent();
		_hc = _enemy.getComponent(HealthComponent.class);
		_hc.addEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
	}
	
	/**
	 * Causes the poison to do its damaging effect over time until the duration has ended or when the 'cure' method is called
	 * The Damage for each tick is the 	(totalDamage / damageTikAmount)
	 * The tick will occurs each 		(duration / damageTikAmount)
	 * @param duration  is the duration in game time seconds on how long the poison effect will affect the Enemy Entity this component is attached to
	 * @param totalDamage is the total damage this poison effect will do when it has done its full effect over the given duration
	 * @param damageTikAmount Is the amount of hurt ticks which will be done in the duration
	 */
	public void poison(float duration, float totalDamage, int damageTickAmount)
	{
		_currentDamageTikWait = 0;
		_duration = duration;
		_damageTickAmount = damageTickAmount;
		
		_tikDuration = _duration / (float)_damageTickAmount;
		_tikDamage = totalDamage / (float)_damageTickAmount;
	}
	
	/**
	 * This cures the Enemy from the poison effect which was caused by this component. This method will do nothing if the poison effect caused by this component is not active
	 */
	public void cure()
	{
		if(_duration <= 0)
		{
			if(_duration != -1) { return; }
		}
		
		_duration = -1;
		
		_enemy.getComponent(AnimationComponent.class).setColor(1, 1, 1, 1);
		
		if(_removeComponentOnCure)
			this.destroy();
	}
	
	@Override
	protected void updated(float deltaTime) 
	{	
		if(_duration <= 0) { return; }
		
		_enemy.getComponent(AnimationComponent.class).setColor(0.482f, 0.839f, 0.549f, 1);
		_currentDamageTikWait += deltaTime;
		_duration -= deltaTime;
		if(_currentDamageTikWait >= _tikDuration)
		{
			_currentDamageTikWait = 0;
			_hc.damage(_tikDamage);
			HitRegistrationPoint.getInstance().register(this.getParentOfComponent().getTransformComponent().getPositionX(), this.getParentOfComponent().getTransformComponent().getPositionY(), _enemy, _tool, HitGlobals.TYPE_POISON_HIT);
		}
		
		if(_duration <= 0)
		{
			_duration = -1;
			cure();
		}
	}

	@Override
	protected void destroyed() 
	{
		_enemy = null;
		_hc.removeEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		_hc = null;
	}

	@Override
	protected void activeStateChanged() 
	{
		
	}
}
