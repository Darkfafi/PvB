package com.mygdx.game.components.attacking;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationEvent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;

public class HeavyBanditAttackComponent extends BaseEnemyAttackComponent implements IEventReceiver
{

	private float _dmgDelayTime;
	private float _timePassedUntilDmg = 0;
	private HealthComponent _healthHitting;
	
	public HeavyBanditAttackComponent(float dmg, int rangeInTiles, float attackDelayTime) 
	{
		super(dmg, rangeInTiles);
		_dmgDelayTime = attackDelayTime;
	}


	@Override
	public boolean isAttacking()
	{
		return _healthHitting != null;
	}

	@Override
	protected void attackCall(HealthComponent hc) 
	{
		this.getAnimationComponent().setCurrentAnimation("charge", true);
		this.getAnimationComponent().removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		this.getAnimationComponent().addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		_healthHitting = hc;
	}

	@Override
	protected void stopAttackingCall() 
	{
		this.getAnimationComponent().removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		_healthHitting = null;
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updated(float deltaTime) 
	{
		if(!this.isActive()) { return; }
		if(this.getAnimationComponent().getCurrentAnimation() == "attack" && _healthHitting != null)
		{
			_timePassedUntilDmg += deltaTime;
			if(_timePassedUntilDmg >= _dmgDelayTime)
			{
				_timePassedUntilDmg = 0;

				if(	this._healthHitting == null 
					|| 	this._healthHitting.isDestroyed()
					|| !this._healthHitting.isAlive()) 
				{
					this.stopAttacking();
					return;
				}
				this._healthHitting.damage(this.getDamage());
				
				if(!this._healthHitting.isAlive())
				{
					this.stopAttacking();
					return;
				}
			}
		}
	}

	@Override
	protected void activeStateChanged() 
	{
		
	}
	
	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == AnimationComponent.EVENT_ANIMATION_STOPPED)
		{
			onAnimationStoppedEvent((AnimationEvent)event);
		}
	}

	private void onAnimationStoppedEvent(AnimationEvent event) 
	{
		if(event.getAnimationName() == "charge")
		{
			this.getAnimationComponent().setCurrentAnimation("attack", true);
		}
	}
}
