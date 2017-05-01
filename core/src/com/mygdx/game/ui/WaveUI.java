package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.waves.WaveSystem;

public class WaveUI extends BaseEntity 
{
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
		_waveText = new TextEntity(true);
		_waveText.getTransformComponent().setPosition(new Vector2((MyGdxGame.WIDTH / 2), MyGdxGame.HEIGHT - 10));
		_waveText.getRenderComponent().setPivot(new Vector2(0.5f, 0), false);
		_waveText.setFontSize(18);
	}

	@Override
	protected void updated(float dt) 
	{
		_waveText.setText("Wave: " + _waveSystem.getCurrentWave());

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed() 
	{
		_waveSystem = null;
		_waveText.destroy();
		_waveText = null;
	}

}
