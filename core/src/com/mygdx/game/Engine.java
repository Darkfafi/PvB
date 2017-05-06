package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.engine.resources.BaseAudioResources;
import com.mygdx.game.engine.resources.BaseFontResources;
import com.mygdx.game.engine.resources.BaseTextureResources;
import com.mygdx.game.engine.scenes.BaseScenesManager;
import com.mygdx.game.engine.scenes.RenderComponents;

public class Engine 
{
	/**
	 * The scale on which the game is running. (This will effect all game Delta Time)
	 */
	public static float TimeScale = 1f;
	
	// Scene Handling
	private float _timeAfterLastRender = 0;
	private static BaseScenesManager _scenesManager;
		
	// Rendering
	private RenderComponents _renderComponents;
	
	private static BaseTextureResources _textureResources;
	private static BaseAudioResources _audioResources;
	private static BaseFontResources _fontResources;
	
	private static int _scale;
	private static int _width;
	private static int _height;
	private static int _frameRate;
	private static String _title;
	
	public Engine(String title, int width, int height, int scale, int frameRate, BaseTextureResources textureResources, BaseAudioResources audioResources, BaseFontResources fontResources)
	{
		_textureResources = textureResources;
		_audioResources = audioResources;
		_fontResources = fontResources;
		
		_textureResources.load();
		_audioResources.load();
		_fontResources.load();
		
		_title = title;
		_scale = scale;
		_width = width;
		_height = height;
		_frameRate = frameRate;
	}
	
	public void setScenesManager(BaseScenesManager manager)
	{
		_scenesManager = manager;
		_renderComponents = manager.getRenderComponents();
	}
	
	public static BaseScenesManager getSceneManager() 
	{
		return _scenesManager; 
	}
	
	public static BaseFontResources getFontResources()
	{
		return _fontResources;
	}
	
	public static BaseTextureResources getTextureResources()
	{
		return _textureResources;
	}
	
	public static BaseAudioResources getAudioResources()
	{
		return _audioResources;
	}
	
	public static String getTitle()
	{
		return _title;
	}
	
	public static int getWidth()
	{
		return _width * _scale;
	}
	
	public static int getHeight()
	{
		return _height * _scale;
	}
	
	public static int getFrameRate()
	{
		return _frameRate;
	}
	
	public static float getFrameStep()
	{
		return 1f / (float)_frameRate;
	}
	
	public void update () 
	{
		_timeAfterLastRender += Gdx.graphics.getDeltaTime();
		while(_timeAfterLastRender >= getFrameStep())
		{
			_timeAfterLastRender -= getFrameStep();
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			_scenesManager.update(getFrameStep());
			_renderComponents.update(getFrameStep());
			_scenesManager.render();
		}
	}
}
