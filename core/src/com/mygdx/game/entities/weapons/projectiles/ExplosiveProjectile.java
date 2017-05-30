package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.ExplosiveComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.globals.Tags;
import com.mygdx.game.hitRegistration.HitGlobals;

/**
 * This projectile is the special Explosive Projectile which explodes on impact. 
 * This will not stop the consecutive hits when you do not land a direct hit but do hit an enemy with the explosion
 * @author Ramses Di Perna
 *
 */
public class ExplosiveProjectile extends BaseHitProjectile
{
	private ExplosiveComponent _explosiveComponent;
	
	public ExplosiveProjectile() 
	{
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_SPECIAL_ARROW_EXPLOSIVE), 1.3f, 25f, 15f);
		_explosiveComponent = this.addComponent(new ExplosiveComponent(Tags.TAG_ENEMY, true));
	}

	@Override
	protected void onCollisionEvent(CollisionEvent event) 
	{	
		boolean isHealthHit =  event.getOtherCollisionComponent().getParentOfComponent().getComponent(HealthComponent.class) != null;
		
		if(isHealthHit)
		{
			_explosiveComponent.setEndHitStreakOnMiss(false);
		}
		
		super.onCollisionEvent(event);
	}

	@Override
	protected float getWeight() 
	{
		return 8f;
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		if(newHeightStage == HeightStage.Landed)
		{
			_explosiveComponent.triggerExplosion(EffectFactory.ExplosionType.SmallExplosion, 25f * 2f, 80f, 1.5f, HitGlobals.TOOL_SPECIAL_ARROW);
			this.destroy();
		}
	}

	@Override
	protected void onLandedOnGround() 
	{
		
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}
	
	@Override
	protected void destroyed()
	{
		super.destroyed();
		_explosiveComponent = null;
	}

	@Override
	protected int getHitTool() 
	{
		return HitGlobals.TOOL_SPECIAL_ARROW;
	}

	@Override
	protected int[] getGroundHitTypes() 
	{
		return new int[]{ HitGlobals.TYPE_DIRECT_HIT };
	}

	@Override
	protected int[] getHealthHitTypes() 
	{
		return new int[]{ HitGlobals.TYPE_DIRECT_HIT, HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING };
	}

	@Override
	protected int[] getNonHealthHitTypes() 
	{
		return new int[]{ HitGlobals.TYPE_DIRECT_HIT };
	}
}
