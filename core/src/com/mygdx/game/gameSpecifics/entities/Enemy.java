package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.Rendering.AnimationEvent;
import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.gameSpecifics.components.EnemyPlayfieldAIComponent;
import com.mygdx.game.gameSpecifics.components.HealthComponent;
import com.mygdx.game.gameSpecifics.events.HealthEvent;
import com.mygdx.game.resources.CollisionResources;

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
	
	
	public Enemy(Animations animations, float health)
	{
		_animations = animations;
		
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
			//this.getComponent(AnimationComponent.class).setCurrentAnimation("attack", true);
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
		this.addComponent(new CollisionComponent());
		
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ARROW | CollisionResources.BIT_TRAP;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(25f), CollisionResources.convertToPPM(25f), new Vector2(0, 0), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef, CollisionResources.BIT_ENEMY);
	}
	
	@Override
	protected void updated(float dt) 
	{
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
	}

	@Override
	protected void destroyed() 
	{
		this.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		this.getComponent(AnimationComponent.class).removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
		
	}
	
	private void onDamagedEvent(HealthEvent event) 
	{
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
		
		this.removeComponent(CollisionComponent.class);
		
		this.getComponent(AnimationComponent.class).setCurrentAnimation("death", true);
		this.getComponent(AnimationComponent.class).addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}
}
