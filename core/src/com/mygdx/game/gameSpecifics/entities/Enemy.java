package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.RenderComponent;

public class Enemy extends BaseEntity 
{
	private float _time = 0;
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new RenderComponent()).setCurrentTexture(MyGdxGame.Resources.getTexture("testImage"));
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		this.getTransformComponent().translatePosition(new Vector2(1, 0));
		_time += dt;
		System.out.println(_time);
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
