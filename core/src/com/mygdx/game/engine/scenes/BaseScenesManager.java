package com.mygdx.game.engine.scenes;

import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.EntitySystem;
import com.mygdx.game.engine.entities.components.TransformComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.tweening.EngineTweener;
import com.mygdx.game.engine.tweening.FloatAccessor;
import com.mygdx.game.engine.tweening.RenderAccessor;
import com.mygdx.game.engine.tweening.TransformAccessor;
import com.mygdx.game.engine.tweening.TweenableFloat;
import com.mygdx.game.engine.tweening.Vector2Accessor;

import aurelienribon.tweenengine.Tween;

/**
 * The BaseScenesManager is the system which controls the switching of scenes.
 * This class must be inherited to specify the scenes which are able to be switched to.
 * These scenes are defined in the 'getScene' method.
 * @author Ramses Di Perna
 */
public abstract class BaseScenesManager 
{	
	/**
	 * The current scene active and the current scene which is being deactivated.
	 */
	private Stack<BaseScene> _gameScenes = new Stack<BaseScene>();
	
	private int _sceneToLoad = -1337; 
	
	/**
	 * The Render Components available to the current scene to render its assets with. 
	 */
	private RenderComponents _renderComponents;
	
	public BaseScenesManager(RenderComponents renderComponents)
	{
		_renderComponents = renderComponents;
		
		Tween.registerAccessor(TransformComponent.class, new TransformAccessor());
		Tween.registerAccessor(RenderComponent.class, new RenderAccessor());
		Tween.registerAccessor(Vector2.class, new Vector2Accessor());
		Tween.registerAccessor(TweenableFloat.class, new FloatAccessor());
		Tween.setCombinedAttributesLimit(4);
	}
	
	/**
	 * This method will be called every frame as long as the application is active.
	 * @param dt stands for the deltaTime passed.
	 */
	public void update(float dt)
	{
		if(_sceneToLoad != -1337)
		{
			System.gc();
			pushScene(_sceneToLoad);
			_sceneToLoad = -1337;
		}

		if(_gameScenes.isEmpty()) { return; }
		_gameScenes.peek().update((dt * Engine.TimeScale));
		EngineTweener.updateTweenEngine(dt, Engine.TimeScale);
		EntitySystem.getInstance().updateEntities((dt * Engine.TimeScale));
	}
	
	/**
	 * This method will be called every frame after the 'update' method as long as the application is active.
	 */
	public void render()
	{
		if(_gameScenes.isEmpty()) { return; }
		_gameScenes.peek().render();
		EntitySystem.getInstance().renderEntities(this.getRenderComponents());
	}
	
	/**
	 * Sets the current scene to the scene which is given by the 'getScene' method.
	 * @param sceneId of scene wanting to set. Specified by the extending class.
	 */
	public void setScene(int sceneId)
	{
		popScene();
		EntitySystem.getInstance().destroyAllEntitiesImmediately();
		_renderComponents.resetCams();
		_sceneToLoad = sceneId;
		
	}
	
	/**
	 * The Render Components available to the current scene to render its assets with. 
	 * @return returns the RenderComponents given in this class's constructor.
	 */
	public RenderComponents getRenderComponents() 
	{
		return _renderComponents;
	}
	
	/**
	 * This method will return a new instance of a scene. 
	 * The scene to return is specified by the extending class.
	 * @param sceneId to determine the returning Scene by.
	 * @return Instance of Scene which represents the sceneId
	 */
	protected abstract BaseScene getScene(int sceneId);
	
	/**
	 * Pushes a scene on the stack and initializes it.
	 * @param sceneId to give the 'getScene' method
	 */
	private void pushScene(int sceneId)
	{
		BaseScene bs = getScene(sceneId);
		_gameScenes.push(bs);
		bs.Initialize(this);
	}
	
	/**
	 * Removes the current scene and calls the 'destroyed' method so it can clean itself.
	 */
	private void popScene()
	{
		if(_gameScenes.isEmpty()){return;}
		_gameScenes.pop().destroy();
	}
}
