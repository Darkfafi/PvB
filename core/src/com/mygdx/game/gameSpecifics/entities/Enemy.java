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
	private float _time = 0;
	private float _moveSpeed = 0;
	private Animations _animations;
	
	public Enemy(Animations animations, float health, float movementSpeed)
	{
		_moveSpeed = 0;
		_animations = animations;
		
		this.addComponent(new AnimationComponent(_animations, true, false));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0f), false);
		this.getComponent(AnimationComponent.class).setSortingLayer(1);
		this.getComponent(AnimationComponent.class).setSortOnY(true);
		
		this.addComponent(new HealthComponent(health)).addEventListener(HealthComponent.EVENT_HEALTH_DAMAGED, this);
		
		
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
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		_time += dt;
		//System.out.println(EntitySystem.getInstance().getEntitiesByClass(Enemy.class));
		this.getTransformComponent().translatePosition(new Vector2(0, -this._moveSpeed));
		if(_time > 8f && _time < 10f)
		{
			die();
			_time = 12f;
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
			//death();
			System.out.println("fsfssdf");
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
