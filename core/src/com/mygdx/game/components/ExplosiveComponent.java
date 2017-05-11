package com.mygdx.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.EntitySystem;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.entities.Effect;
import com.mygdx.game.factories.EffectFactory;

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
	
	public Effect triggerExplosion(EffectFactory.ExplosionType explosionType, float dmg, float radius, float scale)
	{
		Effect explosion = EffectFactory.createExplosionEffect(explosionType, 
				this.getParentOfComponent().getTransformComponent().getPositionX(), 
				this.getParentOfComponent().getTransformComponent().getPositionY(), scale);
		
		explosion.getAnimationComponent().setSortingLayer(1);
		explosion.getAnimationComponent().setSortOnY(true);
		
		this.damageEffect(radius * scale, dmg * scale);
		
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
	
	private void damageEffect(float radius, float dmg)
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
				}
			}
		}
	}
	
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
