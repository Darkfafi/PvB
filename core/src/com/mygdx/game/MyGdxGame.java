package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.engine.scenes.GameCamera;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.scenes.GameScenesManager;
import com.mygdx.game.touchinput.TouchInputHandler;

public class MyGdxGame extends ApplicationAdapter 
{	
	public static final String TITLE = "Bandidos";
	public static final int SCALE = 2;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 480;
	
	// Rendering
	private RenderComponents _renderComponents;
	
	private SpriteBatch _batch;
	private GameCamera _mainCam;
	private GameCamera _hudCam; 
	
	private Engine _engine;
	
	public static TouchInputHandler getInputHandler()
	{
		return (TouchInputHandler) Gdx.input.getInputProcessor();
	}
	
	@Override
	public void create () 
	{
		_engine = new Engine(TITLE, WIDTH, HEIGHT, SCALE, 60, new GameTextureResources(), new GameAudioResources(), new GameFontResources());
		
		Gdx.input.setInputProcessor(new TouchInputHandler(Engine.getWidth(), Engine.getHeight())); // Set up InputHandling
		
		_batch = new SpriteBatch();
		_mainCam = new GameCamera();
		_mainCam.setToOrtho(false, Engine.getWidth(), Engine.getHeight());
		_hudCam = new GameCamera();
		_hudCam.setToOrtho(false, Engine.getWidth(), Engine.getHeight());
		_renderComponents = new RenderComponents(_batch, _mainCam, _hudCam);
		
		_engine.setScenesManager(new GameScenesManager(_renderComponents));
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render () 
	{
		_engine.update();
	}
	
	@Override
	public void dispose()
	{
		
	}
}
