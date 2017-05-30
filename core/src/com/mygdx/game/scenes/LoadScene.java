package com.mygdx.game.scenes;

import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.entities.BasicEntity;

/**
 * This scene is the loading scene
 * In this scene the progress of loading assets will be shown and the scene will switch to the Menu Scene when it has loaded all the assets.
 * @author Ramses Di Perna
 *
 */
public class LoadScene extends BaseScene 
{
	private TextEntity _textEntity;
	private BasicEntity _basicEntity;
	
	@Override
	public void update(float dt) 
	{
		if(Engine.getAssetManager().update())
		{
			this.getScenesManager().setScene(GameScenesManager.MENU_SCENE);
		}
		else
			_textEntity.setText("LOADING: " + (int)(Engine.getAssetManager().getProgress() * 100) + "%");
	}

	@Override
	public void render() 
	{
		
	}

	@Override
	protected void created() 
	{
		_textEntity = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.WHITE_BANDIDOS) ,"LOADING: 0%", true);
		_textEntity.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() / 2 - 60);
		
		_basicEntity = new BasicEntity();
		Animations anims = new Animations("loadingAnim", Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_MEDIUM_BANDIT_0_RUN), true);
		_basicEntity.addComponent(new AnimationComponent(anims, true, true));
		_basicEntity.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() / 2 + 60);
		
	}

	@Override
	protected void destroyed()
	{
		_textEntity = null;
		_basicEntity = null;
	}

}
