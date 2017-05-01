package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.engine.scenes.BaseScenesManager;
import com.mygdx.game.engine.scenes.GameCamera;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.scenes.GameScenesManager;
import com.mygdx.game.touchinput.TouchInputHandler;

public class MyGdxGame extends ApplicationAdapter 
{	
	// Game Settings
	public static final String TITLE = "Archery Game";

	public static final int SCALE = 2;
	public static final int WIDTH = 300 * SCALE;
	public static final int HEIGHT = 480 * SCALE;
	
	public static final int FRAME_RATE = 60;
	public static final float FRAME_STEP = 1f / FRAME_RATE;
	
	private static GameTextureResources _textureResources = new GameTextureResources();
	private static GameAudioResources _audioResources = new GameAudioResources();
	private static GameFontResources _fontResources = new GameFontResources();
	
	// Scene Handling
	private float _timeAfterLastRender = 0;
	private static BaseScenesManager _scenesManager;
	
	// Rendering
	private RenderComponents _renderComponents;
	
	private SpriteBatch _batch;
	private GameCamera _mainCam;
	private GameCamera _hudCam; 
	
	public static GameFontResources getFontResources()
	{
		return _fontResources;
	}
	
	public static GameTextureResources getTextureResources()
	{
		return _textureResources;
	}
	
	public static GameAudioResources getAudioResources()
	{
		return _audioResources;
	}
	
	public static TouchInputHandler getInputHandler()
	{
		return (TouchInputHandler) Gdx.input.getInputProcessor();
	}
	
	public static BaseScenesManager getSceneManager() 
	{
		return _scenesManager; 
	}
	
	@Override
	public void create () 
	{
		getTextureResources().load();
		getAudioResources().load();
		getFontResources().load();
		
		Gdx.input.setInputProcessor(new TouchInputHandler(WIDTH ,HEIGHT)); // Set up InputHandling
		
		_batch = new SpriteBatch();
		_mainCam = new GameCamera();
		_mainCam.setToOrtho(false, WIDTH, HEIGHT);
		_hudCam = new GameCamera();
		_hudCam.setToOrtho(false, WIDTH, HEIGHT);
		_renderComponents = new RenderComponents(_batch, _mainCam, _hudCam);
		_scenesManager = new GameScenesManager(_renderComponents);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
