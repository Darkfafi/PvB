package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

public abstract class BaseHitProjectile extends BaseProjectile 
{
	private float _damage;
	private float _fullDamageDrawPotential;
	
	//private int _tool;
	//private int[] _groundHitTypes;
	//private int[] _healthHitTypes;
	//private int[] _nonHealthHitTypes;
	
	private boolean _onHitLandAndDestroy = true;
	
	public BaseHitProjectile(RenderInfo renderSheetProjectile, float speed, final float damage , final float fullDamageDrawPotential) 
	{
		super(renderSheetProjectile, speed);
		_fullDamageDrawPotential = fullDamageDrawPotential;
		_damage = damage;
	}
	
	@Override
	protected void onCollisionEvent(CollisionEvent event) {
		HealthComponent hc = event.getOtherCollisionComponent().getParentOfComponent().getComponent(HealthComponent.class);
		int[] types = getNonHealthHitTypes();
		if(hc != null)
		{
			float dmg = (this.getDrawPower() / _fullDamageDrawPotential) * _damage;
			hc.damage(dmg);
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_ARROW_HIT_ENEMY).play(0.8f * (this.getDrawPower() / _fullDamageDrawPotential), ((float)Math.random() * 0.3f) + 0.9f, 0f);
			types = getHealthHitTypes();
		}
		
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), event.getOtherCollisionComponent().getParentOfComponent(), getHitTool(), types);
		
		if(_onHitLandAndDestroy)
		{
			this.setHeightStage(HeightStage.Landed);
			this.destroy();
		}
	}

	protected void setOnHitLandAndDestroy(boolean value)
	{
		_onHitLandAndDestroy = value;
	}
	
	protected abstract int 	 getHitTool();
	protected abstract int[] getGroundHitTypes();
	protected abstract int[] getHealthHitTypes();
	protected abstract int[] getNonHealthHitTypes();

	@Override
	protected void onLandedOnGround() 
	{
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), null, getHitTool(), getGroundHitTypes());
	}
}
