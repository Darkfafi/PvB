package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.gameSpecifics.components.HealthComponent;
import com.mygdx.game.resources.CollisionResources;

public class Enemy extends BaseEntity 
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
		
		this.addComponent(new HealthComponent(health));
		
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
		if(_time > 2f)
		{
			//this.destroy();
		}
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}
	
}
