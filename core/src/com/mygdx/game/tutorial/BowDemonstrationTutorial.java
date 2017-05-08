package com.mygdx.game.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.weapons.BowWeapon;
import com.mygdx.game.entities.weapons.IWeapon;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.level.Playfield;

/**
 * This entity will handle the full course of a bow demonstration animation
 * @author Ramses Di Oerna
 *
 */
public class BowDemonstrationTutorial extends BaseEntity
{
	private IWeapon _bowWeapon;
	private Playfield _playfield;
	
	private boolean _pulling = false;
	private boolean _release = false;
	
	public BowDemonstrationTutorial(IWeapon bow, Playfield playfield)
	{
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP), true)).setPivot(new Vector2(0.3f, 1), false);
		this.getComponent(RenderComponent.class).setActiveState(false);
		_bowWeapon = bow;
		_playfield = playfield;
	}
	
	/**
	 * Starts the tutorial which displays the computer taking over bow control, spawning and killing an enemy and spawning and killing an enemy with a trap.
	 * @param duration on how long this tutorial animation must take.
	 */
	public void startTutorial(final float duration)
	{
		final Enemy enemy1;
		enemy1 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.LightBandit);
		enemy1.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() + 100);
		
		
		
		enemy1.getTransformComponent().doPosition(Engine.getWidth() / 2, Engine.getHeight() / 2, duration * 0.25f, true).setCallbackMethod(
				new IEngineTweenMethod()	
				{

					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						enemy1.setEnemyState(Enemy.EnemyState.IdleState, true);

						getTransformComponent().setPosition(-100, -100);
						getComponent(RenderComponent.class).setActiveState(true);
						// Enemy kill
						shootUsingTarget(Engine.getWidth() / 2, Engine.getHeight() / 1f, duration * 0.333f, 0);

						Enemy enemy2 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.LightBandit);
						enemy2.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() + 100);
						
						enemy2.getTransformComponent().doPosition(Engine.getWidth() / 1.2f, Engine.getHeight() / 1.4f, duration * 0.4166f, true).getTween().delay(duration * 0.275f);
						
						// Shooting train target
						shootUsingTarget(_playfield.getLevelBlueprint().getTrapSpawnInfos()[0].getActivatorPosition() * Engine.getWidth(), 
								Engine.getHeight() / 1f, duration * 0.333f, (duration * 0.333f) + 0.1f);
						
						getTransformComponent().doPosition(-100, -100, 2, true).getTween().delay(duration * 0.75f);
					}
			
			
				}
		);
	}
	
	/**
	 * Makes the computer shoot a target in the given direction using the given coordinates
	 * @param x position to start shooting at
	 * @param y position to start shooting at 
	 * @param duration to how long the shot must take
	 * @param delay on how long to wait until it does this animation.
	 */
	public void shootUsingTarget(final float x, final float y, final float duration, float delay)
	{
		_release = false;
		_pulling = false;
		
		this.getTransformComponent().doPosition(x, y, duration / 2f, true).setCallbackMethod(
				
				new IEngineTweenMethod()	
				{

					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						_bowWeapon.startControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
						_pulling = true;
						getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_DOWN));
						
						getTransformComponent().doPosition(x, y - BowWeapon.MAX_DRAW_LENGTH, duration / 2f, true).setCallbackMethod(
								
								new IEngineTweenMethod()	
								{

									@Override
									public void onMethod(int tweenEventType, EngineTween tween) 
									{
										_release = true;
									}
								}
						);
					}
				}
		).getTween().delay(delay);
	}

	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) 
	{
		if(_pulling)
		{
			_bowWeapon.inControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
			if(_release)
			{
				_bowWeapon.endControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
				getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP));
				_release = false;
				_pulling = false;
			}
		}
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed() 
	{
		_bowWeapon = null;
		_playfield = null;
	}
}
