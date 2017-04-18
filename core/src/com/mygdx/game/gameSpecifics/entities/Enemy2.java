package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.resources.CollisionResources;

public class Enemy2 extends BaseEntity 
{
	private float _time = 0;
	private Animations _animations;
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new AnimationComponent(_animations, true, false));
		this.getTransformComponent().setScale(new Vector2(0.8f, 0.8f));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0.5f), false);
		this.getComponent(AnimationComponent.class).setFlipX(true);
		
		this.addComponent(new CollisionComponent());
		this.getComponent(CollisionComponent.class).setType(CollisionResources.BIT_ENEMY);
		this.getComponent(CollisionComponent.class).addAllowedType(CollisionResources.BIT_ENEMY);
		
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(25f), CollisionResources.convertToPPM(25f), new Vector2(0, 0), 0);
		fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(fixDef);
	}
	
	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		this.getTransformComponent().translatePosition(new Vector2(0.4f, 0));
		this.getTransformComponent().translateRotation(2.5f);
		_time += dt;
		//System.out.println(EntitySystem.getInstance().getEntitiesByClass(Enemy.class));
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
