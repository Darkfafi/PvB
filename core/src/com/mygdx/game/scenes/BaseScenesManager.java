package com.mygdx.game.scenes;

import java.util.Stack;

import com.mygdx.game.RenderComponents;

public abstract class BaseScenesManager {
	
	private Stack<BaseScene> _gameScenes = new Stack<BaseScene>();
	
	private RenderComponents _renderComponents;
	
	public BaseScenesManager(RenderComponents renderComponents)
	{
		_renderComponents = renderComponents;
	}
	
	public void update(float dt)
	{
		_gameScenes.peek().update(dt);
	}
	
	public void render()
	{
		_gameScenes.peek().render();
	}
	
	public void setScene(int sceneId)
	{
		popScene();
		pushScene(sceneId);
	}
	
	protected abstract BaseScene getScene(int sceneId);
	
	private void pushScene(int sceneId)
	{
		BaseScene bs = getScene(sceneId);
		_gameScenes.push(bs);
		bs.Initialize(this);
	}
	
	private void popScene()
	{
		if(_gameScenes.isEmpty()){return;}
		_gameScenes.pop().destroyed();
	}

	public RenderComponents getRenderComponents() 
	{
		return _renderComponents;
	}
}
