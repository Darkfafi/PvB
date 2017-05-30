package com.mygdx.game.entities.weapons.projectiles;

import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.hitRegistration.HitGlobals;

/**
 * This projectile is the Projectile which damages an enemy on impact. (The basic arrow the bow in the game shoots)
 * @author Ramses Di Perna
 *
 */
public class NormalProjectile extends BaseHitProjectile 
{
	private float _drawStrengthPotentialDamage;
	
	public NormalProjectile(float damage, float fullDamageDrawPotential) {
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_ARROW), 1.3f, damage, fullDamageDrawPotential);
		_drawStrengthPotentialDamage = fullDamageDrawPotential;
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
		super.onLandedOnGround();
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_ARROW_HIT_NOTHING).play(0.4f * (this.getDrawPower() / _drawStrengthPotentialDamage), (float)Math.random() + 0.8f, 0f);
	}

	@Override
	protected void onStageChanged(HeightStage newHeightStage) 
	{
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected int getHitTool() 
	{
		return HitGlobals.TOOL_ARROW;
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
