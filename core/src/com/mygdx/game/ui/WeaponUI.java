package com.mygdx.game.ui;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;

public class WeaponUI extends BaseEntity 
{
	@Override
	protected void awake() 
	{
		this.addComponent(new RenderComponent(null, true));
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}

}
