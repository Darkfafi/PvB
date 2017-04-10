package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;

public class Enemy2 extends BaseEntity 
{
	private float _time = 0;
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new AnimationComponent(MyGdxGame.Resources.getRenderInfo("cat"), false));
		this.getTransformComponent().setScale(new Vector2(0.8f, 0.8f));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0.5f));
		this.getComponent(AnimationComponent.class).setFlipX(true);
	}
	
	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		this.getTransformComponent().translatePosition(new Vector2(1, 0));
		this.getTransformComponent().translateRotation(5);
		_time += dt;
		//System.out.println(EntitySystem.getInstance().getEntitiesByClass(Enemy.class));
		if(_time > 2f)
		{
			this.destroy();
		}
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}
	
}
