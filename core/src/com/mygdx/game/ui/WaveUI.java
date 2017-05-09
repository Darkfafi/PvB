package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.waves.WaveSystem;

/**
 * UI for the Wave the Player is on. This Class handles the Wave UI.
 * @author Djamali
 *
 */
public class WaveUI extends BaseEntity 
{
	private RenderInfo _waveIndicatorUI;
	private WaveSystem _waveSystem;
	private TextEntity _waveText;
	
	public WaveUI(WaveSystem waveSystem)
	{
		_waveSystem = waveSystem;
	}
	

	@Override
	protected void awake()
	{
		this.addComponent(new RenderComponent(null, true));
		_waveIndicatorUI = Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_WAVE_FIELD);
		
		_waveText = new TextEntity(true);
		_waveText.getTransformComponent().setPosition(
			new Vector2(
				(0.5f * _waveIndicatorUI.getTextureToDraw().getWidth()) + 15, 
				Engine.getHeight() - (_waveIndicatorUI.getTextureToDraw().getHeight() / 2) - 5
			)
		);
		_waveText.getRenderComponent().setPivot(new Vector2(0f, 0.5f), false);
		_waveText.setFontSize(9);
		_waveText.setFont(Engine.getFontResources().getFontData(GameFontResources.WAVE_FONT_BANDIDOS));
	}

	@Override
	protected void updated(float dt) 
	{
		_waveText.setText(Integer.toString(_waveSystem.getCurrentWave()));

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		renderComponents.getSpriteBatch().draw(
				_waveIndicatorUI.getTextureToDraw(), 
				10, 
				(Engine.getHeight() - _waveIndicatorUI.getTextureToDraw().getHeight()) - 10, 
				_waveIndicatorUI.getTextureToDraw().getWidth(), 
				_waveIndicatorUI.getTextureToDraw().getHeight()
		);
	}

	@Override
	protected void destroyed() 
	{
		_waveSystem = null;
		_waveText.destroy();
		_waveText = null;
		_waveIndicatorUI = null;
	}

}
