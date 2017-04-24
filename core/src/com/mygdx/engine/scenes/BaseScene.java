package com.mygdx.engine.scenes;

/**
 * The BaseScene is a class to extend to create game specific scenes which can be used in the BaseScenesManager
 * @author Ramses Di Perna
 */
public abstract class BaseScene {
	
	/**
	 * Indicates if the class has already been initialized.
	 */
	private boolean _initialized = false;
	private BaseScenesManager _scenesManager;
	
	/**
	 * Initializes the class. After this method is called it can not be called again.
	 * This method will call the 'created' method
	 * @param manage which controls the current scenes.
	 */
	public void Initialize(BaseScenesManager manager)
	{
		if(_initialized){return;}
		_initialized = true;
		_scenesManager = manager;
		created();
	}
	
	/**
	 * This method is called by the BaseScenesManager when the current scene is being destroyed
	 */
	public void destroy()
	{
		destroyed();
		_scenesManager = null;
	}
	
	/**
	 * This method will be called every frame as long as the scene is active.
	 * @param dt stands for the deltaTime passed.
	 */
	public abstract void update(float dt);
	
	/**
	 * This method will be called every frame after the 'update' method as long as the scene is active.
	 */
	public abstract void render();
	
	/**
	 * This method will be called when the class is initialized.
	 */
	protected abstract void created();
	
	/**
	 * This method is called by the BaseScenesManager when the current scene is being destroyed
	 * Here all the cleaning logic of the class must be created. 
	 */
	protected abstract void destroyed();
	
	/**
	 * Getter for the BaseScenesManager parent of the scene.
	 * @return the BaseScenesManager which was given at the initialization of this class.
	 */
	protected BaseScenesManager getScenesManager()
	{
		return _scenesManager;
	}
	
	/**
	 * Getter for the RenderComponents of the BaseScenesManager.
	 * @return the BaseScenesManager's RenderComponents.
	 */
	protected RenderComponents getRenderComponents()
	{
		return _scenesManager.getRenderComponents();
	}
}
