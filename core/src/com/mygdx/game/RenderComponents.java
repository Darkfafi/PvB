package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderComponents {

	private SpriteBatch _spriteBatch;
	
	private OrthographicCamera _mainCam;
	private OrthographicCamera _hudCam;
	
	
	public RenderComponents(SpriteBatch sp, OrthographicCamera mainCam, OrthographicCamera hudCam)
	{
		_spriteBatch = sp;
		_mainCam = mainCam;
		_hudCam = hudCam;
	}
	
	public OrthographicCamera getMainCamera()
	{
		return _mainCam;
	}
	
	public OrthographicCamera getHudCamera()
	{
		return _hudCam;
	}
	
	public SpriteBatch getSpriteBatch()
	{
		return _spriteBatch;
	}
}
