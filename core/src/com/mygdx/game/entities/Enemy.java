package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Engine;
import com.mygdx.game.components.BasicEnemyAIComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.attacking.BaseEnemyAttackComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationEvent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.globals.Tags;
import com.mygdx.game.score.ScoreHolderComponent;

/**
 * This class is the holder of Enemy Data which is formed by the EnemyFactory
 * @author Ramses Di Perna
 *
 */
public class Enemy extends BaseEntity implements IEventReceiver
{
	public enum EnemyState
	{
		IdleState,
		WalkState,
		DeathState
	}
	
	private Animations _animations;
	private EnemyState _currentEnemyState = null;
	
	private float _hitEffectTimeTracker = 0f;
	
	private Vector2 _targetLocation = new Vector2();
	private BaseEnemyAttackComponent _attackComponent;
	
	private boolean _idleAnimationWhenReached = false;
	private float _movementSpeed = 0;
	
	public Enemy(Animations animations, float health, int baseScoreWorth, BaseEnemyAttackComponent attackComponentAdded)
	{
		_animations = animations;
		_attackComponent = attackComponentAdded;
		this.addComponent(new ScoreHolderComponent(ScoreHolderComponent.ScoreGainType.Destroy, baseScoreWorth));
		
		AnimationComponent ac = this.addComponent(new AnimationComponent(_animations, true, false));
		ac.setPivot(0.5f, 0f, false);
		ac.setSortingLayer(1);
		ac.setSortOnY(true);
		
		this.addComponent(new HealthComponent(health)).addEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		
		attackComponentAdded.setAnimationComponent(ac);
	}
	
	public EnemyState getEnemyState()
	{
		return _currentEnemyState;
	}
	
	public int getAttackRange()
	{
		if(_attackComponent == null) { return 0; }
		return _attackComponent.getRangeInTiles();
	}
	
	public void move(float x, float y, float movementSpeed, boolean idleAnimationWhenReached)
	{
		setEnemyState(EnemyState.WalkState, true);
		_targetLocation.x = x;
		_targetLocation.y = y;
		_movementSpeed = movementSpeed;
		_idleAnimationWhenReached = idleAnimationWhenReached;
	}
	
	public void stopAction(boolean playIdleAnimation)
	{
		setEnemyState(EnemyState.IdleState, playIdleAnimation);
		if(_attackComponent != null)
			_attackComponent.stopAttacking();
	}
	
	public void attack(HealthComponent hc)
	{
		if(_attackComponent.isAttacking() || _currentEnemyState == EnemyState.DeathState) { return; }
		stopAction(true);
		if(!hc.isAlive()) { return; }
		
		_attackComponent.attack(hc);
	}
	
	public void setEnemyState(EnemyState state, boolean setAnimation)
	{
		if(_currentEnemyState == EnemyState.DeathState) { return; } // Can't switch state when dead
		
		if(this.getComponent(AnimationComponent.class) == null) { return; }
		
		_currentEnemyState = state;
		if(!setAnimation) { return; }
		
		switch(_currentEnemyState)
		{
		case IdleState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("idle", true);
			break;
		case WalkState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("run", true);
			break;
		case DeathState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("death", true);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HealthComponent.EVENT_HEALTH_DAMAGED)
		{
			onDamagedEvent((HealthEvent)event);
		}
		
		if(event.getType() == AnimationComponent.EVENT_ANIMATION_STOPPED)
		{
			onAnimationStopped((AnimationEvent)event);
		}
	}

	@Override
	protected void awake() 
	{	
		this.addTag(Tags.TAG_ENEMY);
		this.addComponent(new CollisionComponent());
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ARROW;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(25f), CollisionResources.convertToPPM(40f), new Vector2(0, CollisionResources.convertToPPM(50)), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef, CollisionResources.BIT_ENEMY);
	}
	
	@Override
	protected void updated(float dt) 
	{
		// Hit Effect
		if(_hitEffectTimeTracker != -1)
		{
			_hitEffectTimeTracker += dt;
			if(_hitEffectTimeTracker > 0.1f && _hitEffectTimeTracker < 0.2f)
			{
				// Red flash effect
				_hitEffectTimeTracker = 0.2f;
				this.getComponent(AnimationComponent.class).setColor(1,1,1,1);
			}
			
			// Hit back effect
			this.getTransformComponent().translatePosition(0, 1f);
			
			if(_hitEffectTimeTracker > 0.3f)
			{
				_hitEffectTimeTracker = -1;
			}
		}
		
		// Movement
		if(_currentEnemyState == EnemyState.WalkState && 
				!(this.getTransformComponent().getPositionX() == _targetLocation.x 
			&& this.getTransformComponent().getPositionY() == _targetLocation.y))
		{
			Vector2 dif = new Vector2(_targetLocation.x - this.getTransformComponent().getLocalPositionX(), _targetLocation.y - this.getTransformComponent().getPositionY());
			
			float realDif = dif.len();
			
			dif.nor();
			dif.x *= (_movementSpeed * 1.5f) * (dt * Engine.getFrameRate());
			dif.y *= _movementSpeed * (dt * Engine.getFrameRate());
			
			if(dif.len() > realDif)
				getTransformComponent().setPosition(_targetLocation.x, _targetLocation.y);
			else
				getTransformComponent().translatePosition(dif.x, dif.y);
			
			if((this.getTransformComponent().getPositionX() == _targetLocation.x 
			&& this.getTransformComponent().getPositionY() == _targetLocation.y) || dif.len() > realDif)
			{
				this.stopAction(_idleAnimationWhenReached);
			}
		}
	}

	@Override
	protected void destroyed() 
	{
		this.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		this.getComponent(AnimationComponent.class).removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		_attackComponent = null;
		
	}
	
	private void onDamagedEvent(HealthEvent event) 
	{
		this.getComponent(AnimationComponent.class).setColor(0.9f, 0, 0, 1);
		_hitEffectTimeTracker = 0;
		
		spawnHitEffect((event.getNewHealth() == 0) ? 1.35f : 1f);
		
		if(event.getNewHealth() == 0)
		{
			die();
		}
	}

	private void spawnHitEffect(float scale)
	{
		EffectFactory.createHitEffect(
				this.getTransformComponent().getPositionX(), 
				this.getTransformComponent().getPositionY() + this.getComponent(AnimationComponent.class).getRealHeight() / 2, scale, 
				new Color(174f/255f, 0, 0, 1)
				).getAnimationComponent().setSortingLayer(this.getComponent(AnimationComponent.class).getSortingLayer() + 1);
	}
	
	private void onAnimationStopped(AnimationEvent event) 
	{
		if(event.getAnimationName() == "death")
		{
			AnimationComponent ac = this.getComponent(AnimationComponent.class);
			ac.removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
			ac.setPivot(0.5f, 1f, true);
			ac.doAlpha(0, 0.5f, TweenStartType.GameTime).delay(2f).setCallbackMethod(new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					destroy();
				}
			});
			
		}
	}

	private void die() 
	{	
		BasicEnemyAIComponent c = this.getComponent(BasicEnemyAIComponent.class);
		if(c != null)
		{
			this.removeComponent(BasicEnemyAIComponent.class);
		}
		this.setEnemyState(EnemyState.DeathState, true);
		
		createBloodPool();
		
		this.removeComponent(CollisionComponent.class);
		this.getComponent(AnimationComponent.class).addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}
	
	private void createBloodPool()
	{
		EffectFactory.createBloodPool(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY() + this.getComponent(AnimationComponent.class).getRealHeight() * 0.25f, 1, 4f);
	}
	
	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub
		
	}
}
