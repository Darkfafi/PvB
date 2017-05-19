package com.mygdx.game.entities.weapons;

import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

public class NormalProjectile extends BaseProjectile 
{
	private final float _DAMAGE = 25f; 								// The arrow its damage on full potential
	private final float _FULL_DAMAGE_DRAW_POWER_POTENTIAL = 15f; 	// On what draw power does this arrow do its full damage?
		
	public NormalProjectile() {
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_ARROW), 1.3f);
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
		}
		
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), event.getOtherCollisionComponent().getParentOfComponent(), HitGlobals.TOOL_ARROW, types);
		
		this.setHeightStage(HeightStage.Landed);
		this.destroy();
	}

	@Override
	protected float getWeight() 
	{
		return 5f;
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onLandedOnGround() 
	{
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), null, HitGlobals.TOOL_ARROW, new int[]{HitGlobals.TYPE_DIRECT_HIT, HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING});
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_ARROW_HIT_NOTHING).play(0.4f * (this.getDrawPower() / _FULL_DAMAGE_DRAW_POWER_POTENTIAL), (float)Math.random() + 0.8f, 0f);
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		// TODO Auto-generated method stub
		
	}
}
