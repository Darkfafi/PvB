package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.ExplosiveComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.globals.Tags;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

public class ExplosiveProjectile extends BaseProjectile
{
	private final float _DAMAGE = 25f; 								// The arrow its damage on full potential
	private final float _FULL_DAMAGE_DRAW_POWER_POTENTIAL = 15f; 	// On what draw power does this arrow do its full damage?
	private ExplosiveComponent _explosiveComponent;
	
	public ExplosiveProjectile() 
	{
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_SPECIAL_ARROW_EXPLOSIVE), 1.3f);
		_explosiveComponent = this.addComponent(new ExplosiveComponent(Tags.TAG_ENEMY, true));
	}

	@Override
	protected void onCollisionEvent(CollisionEvent event) 
	{
		HealthComponent hc = event.getOtherCollisionComponent().getParentOfComponent().getComponent(HealthComponent.class);
		int[] types = new int[]{HitGlobals.TYPE_DIRECT_HIT};
		if(hc != null)
		{
			float dmg = (this.getDrawPower() / _FULL_DAMAGE_DRAW_POWER_POTENTIAL) * _DAMAGE;
			hc.damage(dmg);
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_ARROW_HIT_ENEMY).play(0.8f * (this.getDrawPower() / _FULL_DAMAGE_DRAW_POWER_POTENTIAL), ((float)Math.random() * 0.3f) + 0.9f, 0f);
			types = new int[]{HitGlobals.TYPE_DIRECT_HIT, HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING};
			_explosiveComponent.setEndHitStreakOnMiss(false);
		}
		
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), event.getOtherCollisionComponent().getParentOfComponent(), HitGlobals.TOOL_SPECIAL_ARROW, types);
		
		this.setHeightStage(HeightStage.Landed);
	}

	@Override
	protected float getWeight() 
	{
		return 5f;
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		if(newHeightStage == HeightStage.Landed)
		{
			_explosiveComponent.triggerExplosion(EffectFactory.ExplosionType.SmallExplosion, _DAMAGE * 2f, 80f, 1.5f, HitGlobals.TOOL_SPECIAL_ARROW);
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
}
