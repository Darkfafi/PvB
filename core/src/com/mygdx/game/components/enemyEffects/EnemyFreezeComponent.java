package com.mygdx.game.components.enemyEffects;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Enemy.EnemyState;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;

/**
 * This component start a freeze effect (including the ice cube) on the Enemy entity it is attached to when the 'freeze' method is called.
 * This effect will stop when the effect duration has ended or when the 'unfreeze' method is called
 * @author Ramses Di Perna
 *
 */
public class EnemyFreezeComponent extends BaseEntityComponent implements IEventReceiver
{
	private Enemy _enemy;
	private float _duration = 0;
	private EnemyState _preEnemyState;
	private boolean _removeComponentOnUnfreeze = true;
	private BasicEntity _iceBlockEntity;
	private HealthComponent _hc;
	private int _tool;
	
	/**
	 * The constructor of the freeze component requires the following info
	 * @param removeComponentOnUnfreeze causes, when is set to true, that the component will be destroyed after the freeze effect has been done, else it will stay on the entity
	 * @param killTool means when the enemy is killed while frozen, what KillTool it should represent to the HitRegistrationPoint
	 */
	public EnemyFreezeComponent(boolean removeComponentOnUnfreeze, int killTool)
	{
		_removeComponentOnUnfreeze = removeComponentOnUnfreeze;
		_tool = killTool;
	}
	
	@Override
	public void awake() 
	{
		_enemy = (Enemy)this.getParentOfComponent();
		_hc = _enemy.getComponent(HealthComponent.class);
		if(_hc != null)
			_hc.addEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
	}
	
	/**
	 * Freezes the Enemy Entity this component is attached to for the given duration or until the 'unfreeze' method is called
	 * @param duration to freeze the Enemy in game time
	 */
	public void freeze(float duration)
	{
		if(_duration == 0)
		{
			_preEnemyState = _enemy.getEnemyState();
		}
		_enemy.setEnemyState(EnemyState.ShockedState, true);
		_duration = duration;
		
		if(_iceBlockEntity == null)
		{
			_iceBlockEntity = new BasicEntity();
			RenderComponent rc = _iceBlockEntity.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_ICE_CUBE_EFFECT), false));
			rc.setSortOnY(true);
			rc.setPivot(0.5f, 0, false);
			_iceBlockEntity.getTransformComponent().setParent(_enemy.getTransformComponent());
			_iceBlockEntity.getTransformComponent().translatePosition(0, -1);
			_iceBlockEntity.getTransformComponent().setScale(0, 0);
			_iceBlockEntity.getTransformComponent().doScale(1.4f, 1.3f, 0.25f, TweenStartType.GameTime).ease(EaseType.BackOut);
			_enemy.getTransformComponent().doScale(0.60f, 0.7f, 0.4f, TweenStartType.GameTime).ease(EaseType.BackOut);
		}
		else
		{
			_iceBlockEntity.getComponent(RenderComponent.class).stopAllComponentTweens();
			_iceBlockEntity.getComponent(RenderComponent.class).setAlpha(1);
		}
		
		_iceBlockEntity.getComponent(RenderComponent.class).doAlpha(0, 0.5f, TweenStartType.GameTime).setCallbackMethod(new IEngineTweenMethod()
		{
			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				_iceBlockEntity.destroy();
			}
		}).delay(_duration - 0.5f);
	}
	
	/**
	 * Unfreezes the Enemy Entity this component is attached to and has called the freeze effect on. If this Enemy instance is not frozen by this component, this method does nothing
	 */
	public void unFreeze()
	{
		if(_duration <= 0)
		{
			if(_duration != -1) { return; }
		}
		
		_duration = 0;
		_enemy.setEnemyState(_preEnemyState, true);
		_enemy.getTransformComponent().doScale(1f, 1f, 0.1f, TweenStartType.GameTime);
		
		if(_removeComponentOnUnfreeze)
			this.destroy();
	}
	
	/**
	 * Unfreezes the component and destroys the iceBlock effect with a quick effect instead of a melting effect.
	 */
	public void destroyIceBlock()
	{
		if(_iceBlockEntity == null || _iceBlockEntity.isDestroyed()) { return; }
		
		_iceBlockEntity.getComponent(RenderComponent.class).stopAllComponentTweens();
		boolean b = _removeComponentOnUnfreeze;
		_removeComponentOnUnfreeze = false;
		unFreeze();
		_removeComponentOnUnfreeze = b;
		_iceBlockEntity.getTransformComponent().doScale(0, 0, 0.35f, TweenStartType.GameTime).setCallbackMethod(new IEngineTweenMethod(){

			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				_iceBlockEntity.destroy();
				_iceBlockEntity = null;
			}
		});
	}
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HealthComponent.EVENT_HEALTH_DAMAGED)
		{
			if(((HealthEvent)event).getNewHealth() == 0)
			{
				HitRegistrationPoint.getInstance().register(
						_enemy.getTransformComponent().getPositionX()
						, _enemy.getTransformComponent().getPositionY(), _enemy, _tool, new int[]{ HitGlobals.TYPE_ONLY_BONUS_SCORE, HitGlobals.TYPE_COLD_HIT });
				this.destroyIceBlock();
			}
		}
	}
	
	@Override
	protected void updated(float deltaTime) 
	{
		if(_iceBlockEntity != null)
		{
			if(_iceBlockEntity.isDestroyed())
				_iceBlockEntity = null;
		}
		
		if(_duration <= 0) { return; }
		_duration -= deltaTime;
		if(_duration <= 0)
		{
			_duration = -1;
			unFreeze();
		}
	}

	@Override
	protected void destroyed() 
	{
		if(_hc != null)
			_hc.removeEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		
		_enemy = null;
		_iceBlockEntity = null;
		_hc = null;
	}

	@Override
	protected void activeStateChanged() 
	{
		
	}
}
