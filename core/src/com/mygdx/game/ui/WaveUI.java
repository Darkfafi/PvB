package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.waves.WaveSystem;

/**
 * UI for the Wave the Player is on. This Class handles the Wave UI.
 * @author Djamali
 *
 */
public class WaveUI extends BaseEntity 
{
	private BasicEntity _waveIndicatorUI;
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
		_waveIndicatorUI = new BasicEntity();
		_waveIndicatorUI.addComponent(new RenderComponent( Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_WAVE_FIELD), true));
		_waveIndicatorUI.getTransformComponent().setParent(this.getTransformComponent());
		
		_waveText = new TextEntity(true);
		_waveText.getTransformComponent().setParent(_waveIndicatorUI.getTransformComponent());
		_waveText.getRenderComponent().setPivot(new Vector2(0.5f, 0.5f), false);
		_waveText.getTransformComponent().setPosition(
			new Vector2(
				22, 
				5
			)
		);
		_waveText.setFontSize(9);
		_waveText.setFont(Engine.getFontResources().getFontData(GameFontResources.WAVE_FONT_BANDIDOS));
	}

	public Vector2 getBackgroundSize()
	{
		return new Vector2(_waveIndicatorUI.getComponent(RenderComponent.class).getRealWidth(), _waveIndicatorUI.getComponent(RenderComponent.class).getRealHeight());
	}
	
	@Override
	protected void updated(float dt) 
	{
		_waveText.setText(Integer.toString(_waveSystem.getCurrentWave()));

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed() 
	{
		_waveSystem = null;
		_waveText = null;
		_waveIndicatorUI = null;
	}

}
