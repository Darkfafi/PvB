package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.enemyEffects.EnemyPoisonComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.hitRegistration.HitGlobals;

/**
 * This projectile is the special Poison Projectile which causes a poison effect on impact on an enemy. 
 * It takes half the enemy health impact and will take the other half in the 3 seconds of poison damage
 * @author Ramses Di Perna
 *
 */
public class PoisonProjectile extends BaseHitProjectile 
{
	public PoisonProjectile() 
	{
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_SPECIAL_ARROW_POISON), 1.3f, 0, 15f);
	}
	
	@Override
	protected void onCollisionEvent(CollisionEvent event) 
	{
		HealthComponent hc = event.getOtherCollisionComponent().getParentOfComponent().getComponent(HealthComponent.class);
		if(hc != null)
		{
			float dmg = (this.getDrawPower() / 15f) * (hc.getHealth() / 2);
			hc.damage(dmg);
		}
		
		if(event.getOtherCollisionComponent().getParentOfComponent().getClass() == Enemy.class)
		{
			Enemy e = (Enemy)event.getOtherCollisionComponent().getParentOfComponent();
			EnemyPoisonComponent fc = e.getComponent(EnemyPoisonComponent.class);
			if(fc == null)
			{
				fc = e.addComponent(new EnemyPoisonComponent(true, this.getHitTool()));
			}
			fc.poison(2f, hc.getHealth(), 3);
		}
		super.onCollisionEvent(event);
	}
	
	@Override
	protected float getWeight() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected int getHitTool() 
	{
		return HitGlobals.TOOL_SPECIAL_ARROW;
	}

	@Override
	protected int[] getGroundHitTypes() 
	{
		return new int[]{ HitGlobals.TYPE_DIRECT_HIT, HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING };
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
