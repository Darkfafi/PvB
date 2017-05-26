package com.mygdx.game.components.attacking;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;

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
	
	public abstract boolean isAttacking();
	
	public void attack(HealthComponent hc)
	{
		if(this.isAttacking()) { return; }
		attackCall(hc);
		this.dispatchEvent(new Event(STARTED_ATTACKING_EVENT));
	}
	
	public void stopAttacking()
	{
		if(!this.isAttacking()) { return; }
		stopAttackingCall();
		this.dispatchEvent(new Event(STOPPED_ATTACKING_EVENT));
	}
	
	public int getRangeInTiles()
	{
		return _rangeInTiles;
	}

	
	public float getDamage()
	{
		return _damage;
	}

	public void setAnimationComponent(AnimationComponent animationComponent)
	{
		_animationComponent = animationComponent;
	}
	
	protected AnimationComponent getAnimationComponent()
	{
		return _animationComponent;
	}
	
	protected void destroyed()
	{
		stopAttacking();
		_animationComponent = null;
	}
	
	protected abstract void attackCall(HealthComponent hc);
	protected abstract void stopAttackingCall();
}
