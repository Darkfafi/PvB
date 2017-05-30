package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.GameAudioResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

/**
 * This is the base class for projectiles which can hit and damage entities with HealthComponents
 * @author Ramses Di Perna
 *
 */
public abstract class BaseHitProjectile extends BaseProjectile 
{
	private float _damage;
	private float _fullDamageDrawPotential;
	
	private boolean _onHitLandAndDestroy = true;
	
	/**
	 * The base information needed for a Hit projectile
	 * @param renderSheetProjectile is what to render the projectile as
	 * @param speed the max speed the projectile travels
	 * @param damage the max damage the projectile inflicts on impact with an enemy
	 * @param fullDamageDrawPotential the draw strength it should be fired with to meet its full potential in damage and speed
	 */
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
	
	/**
	 * Returns the hit tool which this projectile represents to the HitRegistrationPoint
	 * @return The hit tool which this projectile represents to the HitRegistrationPoint
	 */
	protected abstract int 	 getHitTool();
	/**
	 * Returns The hit types which should be registered to the HitRegistrationPoint when this projectile hits the ground
	 * @return The hit types which should be registered to the HitRegistrationPoint when this projectile hits the ground
	 */
	protected abstract int[] getGroundHitTypes();
	/**
	 * Returns The hit types which should be registered to the HitRegistrationPoint when this projectile hits an entity with a HealthComponent
	 * @return The hit types which should be registered to the HitRegistrationPoint when this projectile hits an entity with a HealthComponent
	 */
	protected abstract int[] getHealthHitTypes();
	/**
	 * Returns The hit types which should be registered to the HitRegistrationPoint when this projectile hits an entity without a HealthComponent
	 * @return The hit types which should be registered to the HitRegistrationPoint when this projectile hits an entity without a HealthComponent
	 */
	protected abstract int[] getNonHealthHitTypes();

	@Override
	protected void onLandedOnGround() 
	{
		HitRegistrationPoint.getInstance().register(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), null, getHitTool(), getGroundHitTypes());
	}
}
