package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;

public class Enemy extends BaseEntity 
{
	private float _time = 0;
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new AnimationComponent(MyGdxGame.getTextureResources().getRenderInfo("knight"), true, true));
		this.getTransformComponent().setScale(new Vector2(0.7f, 0.7f));
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f,0f), false);
		this.getComponent(AnimationComponent.class).setSortingLayer(1);
	}
	
	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
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
