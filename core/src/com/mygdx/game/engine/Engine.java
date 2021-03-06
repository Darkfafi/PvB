package com.mygdx.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.engine.resources.BaseAudioResources;
import com.mygdx.game.engine.resources.BaseFontResources;
import com.mygdx.game.engine.resources.BaseTextureResources;
import com.mygdx.game.engine.scenes.BaseScenesManager;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class should be instantiated at the core of the workspace.
 * It will be the holder and handler for all the core Game Systems and should be updated for it to run.
 * It will handle its own Update loop and will keep it at the given FrameRate.
 * Create a window with the given width, height and title and will hold all the Resources
 * If you want to load a resource, know the size of the screen, get access to scene management or rendering. They are all available as static fields which can be accessed by any class.
 * @author Ramses Di Perna
 *
 */
public class Engine 
{
	/**
	 * The scale on which the game is running. (This will effect all game Delta Time) [DEFAULT VALUE: 1f]
	 */
	public static float TimeScale = 1f;
	
	// Scene Handling
	private float _timeAfterLastRender = 0;
	private static BaseScenesManager _scenesManager;
		
	// Rendering
	private RenderComponents _renderComponents;
	
	private static AssetManager _assetManager;
	private static BaseTextureResources _textureResources;
	private static BaseAudioResources _audioResources;
	private static BaseFontResources _fontResources;
	
	private static int _scale;
	private static int _width;
	private static int _height;
	private static int _frameRate;
	private static String _title;
	
	/**
	 * This creates the core of the game system. The Engine should be updated in a loop by calling the 'update' method
	 * Do not forget to call the 'setScenesManager' method to set the scene manager.
	 * @param title to give the game window
	 * @param width is the width to make the game window in pixels
	 * @param height is the height to make the game window in pixels
	 * @param scale is how much to scale the window size (width and height)
	 * @param frameRate is the frame rate the game should run on (60 fps recommended)
	 * @param textureResources is the texture resources instance which has the loadResource calls for all the game textures
	 * @param audioResources is the audio resources instance which has the loadResource calls for all the game audio
	 * @param fontResources is the font resources instance which has the loadResource calls for all the game fonts
	 */
	public Engine(String title, int width, int height, int scale, int frameRate, BaseTextureResources textureResources, BaseAudioResources audioResources, BaseFontResources fontResources)
	{
		_textureResources = textureResources;
		_audioResources = audioResources;
		_fontResources = fontResources;
		
		_assetManager = new AssetManager();
		
		_textureResources.loadRequest(_assetManager);
		_audioResources.loadRequest(_assetManager);
		_fontResources.loadRequest(_assetManager);
		
		_title = title;
		_scale = scale;
		_width = width;
		_height = height;
		_frameRate = frameRate;
	}
	
	/**
	 * Sets the scene manager of the Engine.
	 * @param manager is the Scene Manager which should be put as static scene manager.
	 */
	public void setScenesManager(BaseScenesManager manager)
	{
		_scenesManager = manager;
		_renderComponents = manager.getRenderComponents();
	}
	
	/**
	 * Returns the AssetManager which is keeps track on how much is loaded of all the resources
	 * @return The AssetManager of the Engine (Can be used for loading screens)
	 */
	public static AssetManager getAssetManager()
	{
		return _assetManager;
	}
	
	/**
	 * Gets the SceneManager set with the 'setScenesManager' method
	 * @return The SceneManager set with the 'setScenesManager' method
	 */
	public static BaseScenesManager getSceneManager() 
	{
		return _scenesManager; 
	}
	
	/**
	 * Returns the FontResources instance given in the Engine Constructor
	 * @return FontResources instance given in the Engine Constructor
	 */
	public static BaseFontResources getFontResources()
	{
		return _fontResources;
	}
	/**
	 * Returns the TextureResources instance given in the Engine Constructor
	 * @return TextureResources instance given in the Engine Constructor
	 */
	public static BaseTextureResources getTextureResources()
	{
		return _textureResources;
	}
	/**
	 * Returns the AudioResources instance given in the Engine Constructor
	 * @return AudioResources instance given in the Engine Constructor
	 */
	public static BaseAudioResources getAudioResources()
	{
		return _audioResources;
	}
	
	/**
	 * Returns the Title of the application given to the Engine
	 * @return The title of the application as String
	 */
	public static String getTitle()
	{
		return _title;
	}
	
	/**
	 * Returns the real width of the screen
	 * @return The width of the screen in pixels
	 */
	public static int getWidth()
	{
		return _width * _scale;
	}
	
	/**
	 * Returns the real height of the screen
	 * @return The height of the screen in pixels
	 */
	public static int getHeight()
	{
		return _height * _scale;
	}
	
	/**
	 * Returns the given Frame Rate of the game
	 * @return Returns the set frame rate as integer.
	 */
	public static int getFrameRate()
	{
		return _frameRate;
	}
	
	/**
	 * Gets the Time it would take to complete a single frame.
	 * @return Time in seconds for a single frame
	 */
	public static float getFrameStep()
	{
		return 1f / (float)_frameRate;
	}
	
	/**
	 * Updates the entire Engine with all the rendering and scene managing
	 */
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
