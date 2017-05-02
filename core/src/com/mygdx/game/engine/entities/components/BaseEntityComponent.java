package com.mygdx.game.engine.entities.components;

import java.util.Stack;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.engine.events.GlobalDispatcher;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.EngineTweenTracker;
import com.mygdx.game.engine.tweening.EngineTweener;

import aurelienribon.tweenengine.Tween;

/**
 * This class must be inherited by a class for it to be able to be added to Entities as component.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseEntityComponent extends EventDispatcher
{
	/**
	 * Indicates whether the component is active or not.
	 */
	private boolean _isActive = true;
	
	/**
	 * Indicates whether the component has been initialized.
	 */
	private boolean _initialized = false;
	
	/**
	 * The parent entity of the component.
	 */
	private BaseEntity _parentOfComponent;
	
	/**
	 * Indicates whether the component has been destroyed.
	 */
	private boolean _isDestroyed = false;
	
	private EngineTweenTracker _tweenTracker = new EngineTweenTracker();
	
	private Stack<EngineTween> _tweensToStartStack = new Stack<EngineTween>();
	
	public EngineTween startTweenOnComponent(Tween tween)
	{
		EngineTween t = new EngineTween(tween);
		_tweensToStartStack.add(t);
		_tweenTracker.registerTween(t);
		return t;
	}
	
	public void stopAllComponentTweens()
	{
		_tweenTracker.stopAllTweens();
	}
	
	/**
	 * Initializes the component to set its correct properties. This method can only be called once.
	 * @param parentEntity of the component. (To which entity the component is attached to)
	 */
	public void initialize(BaseEntity parentEntity)
	{
		if(_initialized){ return; }
		_initialized = true;
		_isActive = true;
		_parentOfComponent = parentEntity;
		
		GlobalDispatcher.getInstance().dispatchEvent(new ComponentEvent(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this));
		
		awake();
	}
	
	/**
	 * Returns the parent of the component given in the 'initialize' method
	 * @return the parent entity of the component.
	 */
	public BaseEntity getParentOfComponent()
	{
		return _parentOfComponent;
	}
	
	/**
	 * Returns if the component is active or not active. 
	 * When it is not active, the component will not be updated and other functionalities can also be turned off
	 * @return The components active state. If active, this will return true, else it returns false.
	 */
	public boolean isActive()
	{
		return _isActive;
	}
	
	public void setActiveState(boolean activeState)
	{
		if(_isActive == activeState) { return; }
		_isActive = activeState;
		activeStateChanged();
	}
	
	/**
	 * Destroys the component. This will automatically be called when it is removed from its parent entity 
	 */
	public void destroy()
	{
		if(_parentOfComponent == null || _isDestroyed){return;}
		if(_parentOfComponent.hasInstanceOfComponent(this))
		{
			_parentOfComponent.removeComponent(this.getClass());
		}
		else
		{						
			_isDestroyed = true;
			GlobalDispatcher.getInstance().dispatchEvent(new ComponentEvent(EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED, this));
			
			destroyed();
			
			stopAllComponentTweens();
			_tweensToStartStack.clear();
			_tweensToStartStack = null;
			_tweenTracker.clean();
			_tweenTracker = null;
			_parentOfComponent = null;
		}
	}
	
	public void update(float deltaTime)
	{
		while(!_tweensToStartStack.isEmpty())
			EngineTweener.startTween(_tweensToStartStack.pop(), EngineTweener.COMPONENT_CHANNEL);
		
		_tweenTracker.updateTracker();
		updated(deltaTime);
	}
	
	/**
	 * This will be called when the component is initialized
	 */
	public abstract void awake();
	
	/**
	 * This will be called every frame for as long as the component is attached to the entity.
	 * @param deltaTime of how much time passed between this frame and the last.
	 */
	protected abstract void updated(float deltaTime);
	
	/**
	 * This will be called when the component is about to be destroyed / removed from the parent entity
	 */
	protected abstract void destroyed();
	
	/**
	 * This will be called when the active state of the component has been changed.
	 */
	protected abstract void activeStateChanged();
}
