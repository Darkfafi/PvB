package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.RenderComponent;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.resources.CollisionResources;

public class TestBlock extends BaseEntity {
	
	@Override
	protected void awake() {
		this.addComponent(new RenderComponent(MyGdxGame.getTextureResources().getRenderInfo("knight"), false));
		this.getTransformComponent().setScale(new Vector2(0.4f, 0.4f));
		
		this.addComponent(new CollisionComponent());
		this.getComponent(CollisionComponent.class).setType(CollisionResources.BIT_ENEMY);
		this.getComponent(CollisionComponent.class).addAllowedType(CollisionResources.BIT_ENEMY);
		this.getComponent(CollisionComponent.class).addAllowedType(CollisionResources.BIT_FORTRESS);
		
		FixtureDef _fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(25f, 25f, new Vector2(0, 0), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef);
	}
	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}
}
