package com.mygdx.game.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;

public class PlayerBase extends BaseEntity
{
	private Texture _healthBarTexture;
	
	@Override
	protected void awake() 
	{
		_healthBarTexture = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_HP_BAR).getTextureToDraw();
		this.addComponent(new RenderComponent(MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_HP_BAR), false)).setColor(new Color(0,0,0,0));
		this.addComponent(new HealthComponent(100));
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() 
	{
		_healthBarTexture = null;
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		float nHealth = this.getComponent(HealthComponent.class).getNormalizedHealth();
		renderComponents.getSpriteBatch().setColor(new Color(1,1 - (1 - nHealth),1 - (1 -nHealth),1));
		renderComponents.getSpriteBatch().draw(_healthBarTexture, 0, 10, MyGdxGame.WIDTH * nHealth, 25);
	}

}
