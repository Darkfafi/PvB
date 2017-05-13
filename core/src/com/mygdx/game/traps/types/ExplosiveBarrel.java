package com.mygdx.game.traps.types;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.ExplosiveComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.globals.Tags;

/**
 * This entity is an explosive barrel which can be triggered once to explode and will remove itself when it has been triggered after x amount of time
 * The barrel will only damage enemies.
 * @author Ramses Di Perna
 *
 */
public class ExplosiveBarrel extends BaseEntity 
{
	private float _damage = 0;
	private float _radius = 0;
	private boolean _exploded = false;
	
	/**
	 * Creates an Explosive barrel with its stats in tact from the start
	 * @param dmg represents the damage this barrel will do to all enemies in his explosion radius
	 * @param radius to check for enemies to damage with the given damage amount
	 */
	public ExplosiveBarrel(float dmg, float radius)
	{
		_damage = dmg;
		_radius = radius;
	}
	
	/**
	 * Triggers the barrel to explode.
	 * Note: This can only be triggered once for each instance.
	 */
	public void triggerBarrel()
	{
		if(_exploded) { return; }
		this.getComponent(ExplosiveComponent.class).triggerExplosion(EffectFactory.ExplosionType.BigExplosion, _damage, _radius, 1).getTransformComponent().translatePosition(new Vector2(30, 0));
		_exploded = true;
		
		this.getComponent(RenderComponent.class).setCurrentFrameInfo(1);
		this.getComponent(RenderComponent.class).doAlpha(0, 4f, true).setCallbackMethod(new IEngineTweenMethod()
		{
			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				destroy();
			}
		});
	}
	
	/**
	 * Returns whether this barrel has exploded.
	 * @return True if it has else false
	 */
	public boolean hasExploded()
	{
		return _exploded;
	}
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SHEET_EXPLOSIVE_BARREL_TRAP), false));
		this.getComponent(RenderComponent.class).setSortingLayer(1);
		this.getComponent(RenderComponent.class).setSortOnY(true);
		this.getComponent(RenderComponent.class).setPivot(new Vector2(0.5f, 0), false);
		
		this.addComponent(new ExplosiveComponent(Tags.TAG_ENEMY));
	}

	@Override
	protected void updated(float dt) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub

	}
}