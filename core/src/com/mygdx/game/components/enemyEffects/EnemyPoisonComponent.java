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

public class EnemyPoisonComponent extends BaseEntityComponent implements IEventReceiver
{
	private Enemy _enemy;
	private HealthComponent _hc;
	private float _duration = -1;
	private int _damageTikAmount = 0;
	private boolean _removeComponentOnCure = true;
	private int _tool;
	
	// Calculations
	private float _tikDuration = 0;
	private float _tikDamage= 0;
	
	// trackers
	
	private float _currentDamageTikWait = 0;
	
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
	
	public void poison(float duration, float totalDamage, int damageTikAmount)
	{
		_currentDamageTikWait = 0;
		_duration = duration;
		_damageTikAmount = damageTikAmount;
		
		_tikDuration = _duration / (float)_damageTikAmount;
		_tikDamage = totalDamage / (float)_damageTikAmount;
	}
	
	public void cure()
	{
		_duration = -1;
		
		_enemy.getComponent(AnimationComponent.class).setColor(1, 1, 1, 1);
		
		if(_removeComponentOnCure)
			this.destroy();
	}
	
	@Override
	protected void updated(float deltaTime) 
	{	
		if(_duration != -1)
		{
			
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
				cure();
			}
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
