package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.resources.CollisionResources;

public class Enemy extends BaseEntity 
{
	private float _time = 0;
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new AnimationComponent(MyGdxGame.getTextureResources().getRenderInfo("light_Bandit_0_Run"), true, false));
		this.getTransformComponent().setScale(new Vector2(0.7f, 0.7f));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0f), false);
		this.getComponent(AnimationComponent.class).setSortingLayer(1);
		
		this.addComponent(new CollisionComponent());
		this.getComponent(CollisionComponent.class).setType(CollisionResources.BIT_ENEMY);
		this.getComponent(CollisionComponent.class).addAllowedType(CollisionResources.BIT_ENEMY);
		this.getComponent(CollisionComponent.class).addAllowedType(CollisionResources.BIT_FORTRESS);
		
		FixtureDef _fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(25f), CollisionResources.convertToPPM(25f), new Vector2(0, 0), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef);
	}
	
	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
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
