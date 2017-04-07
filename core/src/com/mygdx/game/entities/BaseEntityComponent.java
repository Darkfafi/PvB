package com.mygdx.game.entities;

/**
 * This class must be inherited by a class for it to be able to be added to Entities as component.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseEntityComponent 
{
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
	
	/**
	 * Initializes the component to set its correct properties. This method can only be called once.
	 * @param parentEntity of the component. (To which entity the component is attached to)
	 */
	public void initialize(BaseEntity parentEntity)
	{
		if(_initialized){ return; }
		_initialized = true;
		_parentOfComponent = parentEntity;
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
			destroyed();
			_parentOfComponent = null;
		}
	}
	
	/**
	 * This will be called when the component is initialized
	 */
	public abstract void awake();
	
	/**
	 * This will be called every frame for as long as the component is attached to the entity.
	 * @param deltaTime of how much time passed between this frame and the last.
	 */
	public abstract void updated(float deltaTime);
	
	/**
	 * This will be called when the component is about to be destroyed / removed from the parent entity
	 */
	protected abstract void destroyed();
}
