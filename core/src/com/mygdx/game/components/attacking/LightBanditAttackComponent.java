package com.mygdx.game.components.attacking;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationEvent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.Effect;
import com.mygdx.game.factories.EffectFactory;

public class LightBanditAttackComponent extends BaseEnemyAttackComponent implements IEventReceiver
{
	private HealthComponent _healthComponentTarget = null;
	
	public LightBanditAttackComponent(float dmg, int rangeInTiles) 
	{
		super(dmg, rangeInTiles);
	}

	@Override
	public boolean isAttacking() 
	{
		return _healthComponentTarget != null;
	}

	@Override
	protected void attackCall(HealthComponent hc) 
	{
		_healthComponentTarget = hc;
		this.getAnimationComponent().setCurrentAnimation("attack", true);
		this.getAnimationComponent().removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		this.getAnimationComponent().addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}

	@Override
	protected void stopAttackingCall() 
	{
		this.getAnimationComponent().removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		_healthComponentTarget = null;
	}

	@Override
	public void awake() 
	{

	}

	@Override
	protected void updated(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub

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
		if(event.getAnimationName() == "attack")
		{
			HealthComponent ownHc = this.getParentOfComponent().getComponent(HealthComponent.class);
			if(ownHc == null || !ownHc.isAlive()) { return; }
			
			Effect e = EffectFactory.createExplosionEffect(EffectFactory.ExplosionType.BigExplosion, this.getParentOfComponent().getTransformComponent().getPositionX(), this.getParentOfComponent().getTransformComponent().getPositionY() - 2f, 1);
			e.getAnimationComponent().setSortingLayer(1);
			e.getAnimationComponent().setSortOnY(true);
			_healthComponentTarget.damage(getDamage());
			ownHc.kill();
		}
	}

}
