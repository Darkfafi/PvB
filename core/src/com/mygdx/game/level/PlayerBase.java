package com.mygdx.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This Entity is the base which the Player should protect.
 * @author Ramses Di Perna
 *
 */
public class PlayerBase extends BaseEntity
{	
	private Texture _roofTexture;
	
	@Override
	protected void awake() 
	{
		_roofTexture= Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_ROOF).getTextureToDraw();
		this.addComponent(new RenderComponent(null, false)).setSortingLayer(2);
		this.addComponent(new HealthComponent(100));
	}

	@Override
	protected void updated(float dt)
	{

	}

	@Override
	protected void destroyed() 
	{
		_roofTexture = null;
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		renderComponents.getSpriteBatch().setProjectionMatrix(renderComponents.getMainCamera().combined);
		renderComponents.getSpriteBatch().draw(_roofTexture, 0, -15, _roofTexture.getWidth(), _roofTexture.getHeight());
	}

}
