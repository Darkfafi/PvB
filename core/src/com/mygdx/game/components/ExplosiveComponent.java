package com.mygdx.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.EntitySystem;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.entities.Effect;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

/**
 * This component, when triggered, makes an explosion effect of the given type and damages all entities in radius with the 'HealthComponent' attached and if they are any of the given tags to effect
 * @author Ramses Di Perna
 *
 */
public class ExplosiveComponent extends BaseEntityComponent 
{
	private String[] _tagsToEffect;
	
	public ExplosiveComponent(String[] tagsToEffect)
	{
		_tagsToEffect = tagsToEffect;
	}
	
	public ExplosiveComponent(String tagToEffect)
	{
		_tagsToEffect = new String[]{ tagToEffect };
	}
	
	/**
	 * This method triggers an explosion by this component on the location of the entity its attached to.
	 * @param explosionType is the type of explosion effect to spawn
	 * @param dmg is the damage which should be caused to the hit entities holding an HealthComponent
	 * @param radius is the radius in which the entities should stand from this entity in distance in order to be affected by the explosion.
	 * @param scale is the scale of the explosion, this affects the explosion effect, damage and radius. 
	 * @return The explosion effect created.
	 */
	public Effect triggerExplosion(EffectFactory.ExplosionType explosionType, float dmg, float radius, float scale, int hitTool)
	{
		Effect explosion = EffectFactory.createExplosionEffect(explosionType, 
				this.getParentOfComponent().getTransformComponent().getPositionX(), 
				this.getParentOfComponent().getTransformComponent().getPositionY(), scale);
		
		explosion.getAnimationComponent().setSortingLayer(1);
		explosion.getAnimationComponent().setSortOnY(true);
		
		this.damageEffect(radius * scale, dmg * scale, hitTool);
		
		return explosion;
	}

	@Override
	public void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updated(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Damages all the entities in the given radius when they hold any of the tags which where given in this component's constructor and hold a 'HealthComponent'
	 * @param radius in which to damage entities
	 * @param dmg is the damage to inflict to the HealthComponent of the affected entities.
	 */
	private void damageEffect(float radius, float dmg, int toolType)
	{
		ArrayList<BaseEntity> allEntities = EntitySystem.getInstance().getAllEntities();
		BaseEntity current;
		for(int i = 0; i < allEntities.size(); i++)
		{
			current = allEntities.get(i);
			
			if(!hasTagToHit(current)) { continue; }
			
			if(Vector2.dst(this.getParentOfComponent().getTransformComponent().getPositionX(), 
						   this.getParentOfComponent().getTransformComponent().getPositionY(), 
						   current.getTransformComponent().getPositionX(), 
						   current.getTransformComponent().getPositionY()) <= radius)
			{
				HealthComponent hc = current.getComponent(HealthComponent.class);
				if(hc != null)
				{
					hc.damage(dmg);
					HitRegistrationPoint.getInstance().register(hc.getParentOfComponent(), toolType, new int[]{ HitGlobals.TYPE_EXPLOSION_HIT });
				}
			}
		}
	}
	
	/**
	 * Returns whether the given entity holds any of the tags which should be affected by the explosion.
	 * @param entity to check for tags
	 * @return True if it should be affected by the hit, else false.
	 */
	private boolean hasTagToHit(BaseEntity entity)
	{
		for(int i = 0; i < this._tagsToEffect.length; i++)
		{
			if(entity.hasTag(this._tagsToEffect[i]))
				return true;
		}
		return false;
	}
}
