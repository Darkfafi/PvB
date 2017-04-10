package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gameSpecifics.scenes.GameScenesManager;
import com.mygdx.game.scenes.BaseScenesManager;
import com.mygdx.game.scenes.RenderComponents;

public class MyGdxGame extends ApplicationAdapter 
{	
	// Game Settings
	public static final String TITLE = "Archery Game";
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static final int FRAME_RATE = 60;
	public static final float FRAME_STEP = 1f / FRAME_RATE;
	
	private static GameTextureResources _textureResources = new GameTextureResources();
	private static GameAudioResources _audioResources = new GameAudioResources();
	
	// Scene Handing
	
	private float _timeAfterLastRender = 0;
	private BaseScenesManager _scenesManager;
	
	// Rendering
	private RenderComponents _renderComponents;
	
	private SpriteBatch _batch;
	private OrthographicCamera _mainCam;
	private OrthographicCamera _hudCam; 
	
	public static GameTextureResources getTextureResources()
	{
		return _textureResources;
	}
	
	public static GameAudioResources getAudioResources()
	{
		return _audioResources;
	}
	
	@Override
	public void create () 
	{
		getTextureResources().load();
		getAudioResources().load();
		
		_batch = new SpriteBatch();
		_mainCam = new OrthographicCamera();
		_mainCam.setToOrtho(false, WIDTH, HEIGHT);
		_hudCam = new OrthographicCamera();
		_hudCam.setToOrtho(false, WIDTH, HEIGHT);
		_renderComponents = new RenderComponents(_batch, _mainCam, _hudCam);
		_scenesManager = new GameScenesManager(_renderComponents);

		Gdx.gl.glClearColor(0, 0, 0.25f, 1);
	}

	@Override
	public void render () 
	{
		_timeAfterLastRender += Gdx.graphics.getDeltaTime();
		
		while(_timeAfterLastRender >= FRAME_STEP)
		{
			_timeAfterLastRender -= FRAME_STEP;
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			_scenesManager.update(FRAME_STEP);
			_renderComponents.update(FRAME_STEP);
			_scenesManager.render();
		}
	}
	
	@Override
	public void dispose()
	{
		getTextureResources().clean();
		getAudioResources().clean();
	}
}
