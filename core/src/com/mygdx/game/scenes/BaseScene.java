package com.mygdx.game.scenes;

import com.mygdx.game.RenderComponents;

public abstract class BaseScene {
	
	private boolean _initialized = false;
	private BaseScenesManager _scenesManager;
	
	public void Initialize(BaseScenesManager manager)
	{
		if(_initialized){return;}
		_initialized = true;
		_scenesManager = manager;
		created();
	}
	
	public abstract void destroyed();
	public abstract void update(float dt);
	public abstract void render();
	protected abstract void created();
	
	protected BaseScenesManager getScenesManager()
	{
		return _scenesManager;
	}
	
	protected RenderComponents getRenderComponents()
	{
		return _scenesManager.getRenderComponents();
	}
}
