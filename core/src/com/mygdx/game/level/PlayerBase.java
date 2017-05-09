package com.mygdx.game.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;

public class PlayerBase extends BaseEntity
{
	private Texture _healthBarTexture;
	
	@Override
	protected void awake() 
	{
		_healthBarTexture = Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_HP_BAR).getTextureToDraw();
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_HP_BAR), false)).setColor(new Color(0,0,0,0));
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
		
	}

}
