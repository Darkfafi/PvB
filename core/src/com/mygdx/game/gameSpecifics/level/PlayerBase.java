package com.mygdx.game.gameSpecifics.level;

import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.gameSpecifics.components.HealthComponent;

public class PlayerBase extends BaseEntity 
{
	@Override
	protected void awake() 
	{
		// TODO Auto-generated method stub
		this.addComponent(new HealthComponent(100f));
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
