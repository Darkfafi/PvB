package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.enemyEffects.EnemyFreezeComponent;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.hitRegistration.HitGlobals;

/**
 * This projectile is the special Freeze Projectile which freezes an enemy on impact. 
 * @author Ramses Di Perna
 *
 */
public class FreezeProjectile extends BaseHitProjectile {

	public FreezeProjectile() 
	{
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_SPECIAL_ARROW_FREEZE), 1.3f, 5f, 15f);
	}

	@Override
	protected void onCollisionEvent(CollisionEvent event) 
	{
		if(event.getOtherCollisionComponent().getParentOfComponent().getClass() == Enemy.class)
		{
			Enemy e = (Enemy)event.getOtherCollisionComponent().getParentOfComponent();
			EnemyFreezeComponent fc = e.getComponent(EnemyFreezeComponent.class);
			if(fc == null)
			{
				fc = e.addComponent(new EnemyFreezeComponent(true, this.getHitTool()));
			}
			fc.freeze(3f);
		}
		super.onCollisionEvent(event);
	}

	@Override
	protected float getWeight() 
	{
		return 8;
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		
	}

	@Override
	protected void onLandedOnGround() 
	{
		super.onLandedOnGround();
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
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
