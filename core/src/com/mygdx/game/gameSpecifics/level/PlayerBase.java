package com.mygdx.game.gameSpecifics.level;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.gameSpecifics.components.HealthComponent;
import com.mygdx.game.scenes.RenderComponents;

public class PlayerBase extends BaseEntity implements IEventReceiver
{
	@Override
	protected void awake() 
	{
		// TODO Auto-generated method stub
		this.addComponent(new HealthComponent(100f)).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		this.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		System.out.println("HELLLOOOO");
		MyGdxGame.getSceneManager().setScene(0);
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub
		
	}

}
