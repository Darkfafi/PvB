package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.waves.WaveSystem;

public class WaveUI extends BaseEntity 
{
	private WaveSystem _waveSystem;
	
	private BitmapFont _font = new BitmapFont();
	
	public WaveUI(WaveSystem waveSystem)
	{
		_waveSystem = waveSystem;
		_font.getData().scaleX = _font.getData().scaleY = (1.2f);
	}
	

	@Override
	protected void awake()
	{
		this.addComponent(new RenderComponent(null, true));
	}

	@Override
	protected void updated(float dt) 
	{
		

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		String text = "Wave: " + _waveSystem.getCurrentWave();
		_font.draw(renderComponents.getSpriteBatch(), text, (MyGdxGame.WIDTH / 2) - _font.getBounds(text).width / 2, MyGdxGame.HEIGHT - 10);
	}

	@Override
	protected void destroyed() 
	{
		_waveSystem = null;
		_font = null;
	}

}
