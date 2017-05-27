package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.enemyEffects.EnemyPoisonComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.hitRegistration.HitGlobals;

public class PoisonProjectile extends BaseHitProjectile 
{
	public PoisonProjectile() 
	{
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_SPECIAL_ARROW_POISON), 1.3f, 5f, 15f);
	}
	
	@Override
	protected void onCollisionEvent(CollisionEvent event) 
	{
		if(event.getOtherCollisionComponent().getParentOfComponent().getClass() == Enemy.class)
		{
			Enemy e = (Enemy)event.getOtherCollisionComponent().getParentOfComponent();
			EnemyPoisonComponent fc = e.getComponent(EnemyPoisonComponent.class);
			if(fc == null)
			{
				fc = e.addComponent(new EnemyPoisonComponent(true, this.getHitTool()));
			}
			fc.poison(6f, 55f, 8);
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
