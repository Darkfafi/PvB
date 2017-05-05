package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.components.EnemyPlayfieldAIComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationEvent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.globals.Tags;
import com.mygdx.game.score.GameScoreSystem;

public class Enemy extends BaseEntity implements IEventReceiver
{
	public enum EnemyState
	{
		IdleState,
		WalkState,
		AttackState
	}
	private Animations _animations;
	private float _deathTime = -1f;
	private EnemyState _currentEnemyState = EnemyState.WalkState;
	
	private float _hitEffectTimeTracker = 0f;
	
	private int _baseScoreWorth = 0;
	
	public Enemy(Animations animations, float health, int baseScoreWorth)
	{
		_animations = animations;
		_baseScoreWorth = baseScoreWorth;
		this.addComponent(new AnimationComponent(_animations, true, false));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0f), false);
		this.getComponent(AnimationComponent.class).setSortingLayer(1);
		this.getComponent(AnimationComponent.class).setSortOnY(true);
		
		this.addComponent(new HealthComponent(health)).addEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
	}
	
	public void setEnemyState(EnemyState state)
	{
		if(state == _currentEnemyState) { return; }
		_currentEnemyState = state;
		
		switch(_currentEnemyState)
		{
		case AttackState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("attack", true);
			break;
		case IdleState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("idle", true);
			break;
		case WalkState:
			this.getComponent(AnimationComponent.class).setCurrentAnimation("run", true);
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
		// Death Effect
		if(_deathTime != -1)
		{
			_deathTime += dt;
			if(_deathTime > 2f)
			{
				float alpha = this.getComponent(AnimationComponent.class).getAlpha();
				if(alpha > 0.1f)
				{
					this.getComponent(AnimationComponent.class).setAlpha(alpha - (dt * 2));
				}
				else
				{
					this.destroy();
				}
			}
		}
		
		// Hit Effect
		if(_hitEffectTimeTracker != -1)
		{
			_hitEffectTimeTracker += dt;
			if(_hitEffectTimeTracker > 0.1f && _hitEffectTimeTracker < 0.2f)
			{
				// Red flash effect
				_hitEffectTimeTracker = 0.2f;
				this.getComponent(AnimationComponent.class).setColor(new Color(1,1,1,1));
			}
			
			// Hit back effect
			this.getTransformComponent().translatePosition(new Vector2(0, 1f));
			
			if(_hitEffectTimeTracker > 0.3f)
			{
				_hitEffectTimeTracker = -1;
			}
		}
	}

	@Override
	protected void destroyed() 
	{
		this.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		this.getComponent(AnimationComponent.class).removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		
	}
	
	private void onDamagedEvent(HealthEvent event) 
	{
		this.getComponent(AnimationComponent.class).setColor(new Color(0.9f,0,0,1));
		_hitEffectTimeTracker = 0;
		if(event.getNewHealth() == 0)
		{
			die();
		}
	}

	private void onAnimationStopped(AnimationEvent event) 
	{
		if(event.getAnimationName() == "death")
		{
			this.getComponent(AnimationComponent.class).removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
			_deathTime = 0;
		}
	}

	private void die() 
	{
		EnemyPlayfieldAIComponent c = this.getComponent(EnemyPlayfieldAIComponent.class);
		if(c != null)
		{
			this.removeComponent(EnemyPlayfieldAIComponent.class);
		}
		
		GameScoreSystem.getInstance().addScore(_baseScoreWorth, this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY() + this.getComponent(AnimationComponent.class).getRealHeight() * 0.7f);
		
		this.removeComponent(CollisionComponent.class);
		this.getComponent(AnimationComponent.class).setCurrentAnimation("death", true);
		this.getComponent(AnimationComponent.class).addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub
		
	}
}
