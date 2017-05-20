package com.mygdx.game.engine.entities;

import java.util.ArrayList;
import java.util.Stack;

import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.TransformComponent;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.engine.events.GlobalDispatcher;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class must be extended to be part of the EntitySystem. 
 * It will allow the class to be found by tag, to be updated and to have EntityComponents.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseEntity extends EventDispatcher
{
	private ArrayList<String> _tags = new ArrayList<String>();
	private ArrayList<BaseEntityComponent> _components = new ArrayList<BaseEntityComponent>();
	
	private boolean _isDestroyed = false;
	
	private TransformComponent _transformComponent;
	

	private Stack<BaseEntityComponent> _destroyStackQueue = new Stack<BaseEntityComponent>();
	private Stack<BaseEntityComponent> _destroyStack = new Stack<BaseEntityComponent>();
	
	public BaseEntity()
	{
		EntitySystem.getInstance(); // To create an instance of the EntitySystem if not already active.
		GlobalDispatcher.getInstance().dispatchEvent(new EntityEvent(EngineGlobals.GLOBAL_EVENT_ENTITY_CREATED, this));
		_transformComponent = this.addComponent(new TransformComponent());
		awake();
	}
	
	/**
	 * Returns the automatically TransformComponent of the Entity
	 * @return The default TransformComponent.
	 */
	public TransformComponent getTransformComponent()
	{
		return _transformComponent;
	}
	
	/**
	 * Creates and adds a component to the entity. Also calls the Components 'initialize' method.
	 * @param classType of component to create and add.
	 * @return the instance of the created component.
	 */
	public <T extends BaseEntityComponent> T addComponent(T instanceComponent)
	{
		if(_components == null) {System.out.println("WARNING: Can't add component "+ instanceComponent +" on destroyed entity"); return null; }
		_components.add(instanceComponent);
		instanceComponent.initialize(this);
		return instanceComponent;
	}
	
	/**
	 * Returns the instance of the given component type if attached to the entity.
	 * @param classType which the component should be assignable from.
	 * @return the component found. If no component of the given type was found, it returns 'null'
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntityComponent> T getComponent(Class<T> classType)
	{
		if(_components == null) { System.out.println("WARNING: Can't get component "+ classType +" on destroyed entity"); return null; }
		for(int i = 0; i < _components.size(); i++)
		{
			if(_components.get(i).getClass() == classType)
			{
				return (T)_components.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Removes the component of the given type from the Entity and destroys the component.
	 * @param classType which the component should be assignable from.
	 */
	public <T extends BaseEntityComponent> void removeComponent(Class<T> classType)
	{
		if(_components == null) {return;}
		T component = getComponent(classType);
		
		if(component != null)
		{
			if(!_destroyStackQueue.contains(component))
				_destroyStackQueue.add(component);
		}
	}
	
	/**
	 * Checks whether the entity has the same instance of the component as the one given.
	 * @param componentInstrance to check
	 * @return 'true' if it has the components instance else 'false'
	 */
	public boolean hasInstanceOfComponent(BaseEntityComponent componentInstance)
	{
		if(_components == null) { return false;}
		for(int i = 0; i < _components.size(); i++)
		{
			if(_components.get(i) == componentInstance)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a tag to the entity which can be used to be located by the 'EntitySystem'
	 * It will not be added if the tag is already on the entity.
	 * @param tag to attach to the entity. 
	 */
	public void addTag(String tag)
	{
		if(_tags == null) {return; }
		if(hasTag(tag)){ return;}
		_tags.add(tag);
	}
	
	/**
	 * Removes the given tag from the entity if it has it.
	 * @param tag to remove from entity
	 */
	public void removeTag(String tag)
	{
		if(_tags == null) {return; }
		if(!hasTag(tag)) { return;}
		_tags.remove(tag);
	}
	
	/**
	 * Returns whether the entity has the given tag.
	 * @param tag to check
	 * @return 'true' if it has the given tag else it returns 'false'
	 */
	public boolean hasTag(String tag)
	{
		if(_tags == null) {return false; }
		if(_tags.size() == 0){ return false; }
		
		for(int i = 0; i < _tags.size(); i++)
		{
			if(_tags.get(i) == tag)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method will be called every frame by the EntitySystem for as long as the entity is active.
	 * @param dt equals the delta time which has passed between the frames
	 */
	public void update(float dt) 
	{
		while(_destroyStack != null && !_destroyStack.isEmpty())
			removeComponent(_destroyStack.pop());
		
		if(_isDestroyed || _components == null) { return; }
		
		for(int i = _components.size() - 1; i >= 0; i--)
		{
			if(_components == null || _isDestroyed) { return; }

			if(_components.get(i).isActive())
			{
				_components.get(i).update(dt);
			}
			
			if(_destroyStackQueue != null && !_destroyStackQueue.isEmpty())
				_destroyStack.push(_destroyStackQueue.pop());
			
		}
		updated(dt);
	}
	
	/**
	 * This method will be called every render update by the EntitySystem for as long as the entity is active.
	 * @param renderComponents which contains all the render information
	 */
	public void render(RenderComponents renderComponents)
	{
		if(_isDestroyed) { return; }
		rendered(renderComponents);
	}
	
	/**
	 * This method will destroy the entity. This can also be done through the method EntitySystem.destroyEntity();
	 */
	public void destroy()
	{
		if(_isDestroyed) { return; }
		if(EntitySystem.getInstance().isEntityInDestroyStack(this))
		{
			_isDestroyed = true;
			GlobalDispatcher.getInstance().dispatchEvent(new EntityEvent(EngineGlobals.GLOBAL_EVENT_ENTITY_DESTROYED, this));
			destroyed();
			
			for(int i = _components.size() - 1; i >= 0; i--)
			{
				removeComponent(_components.get(i));
			}
			
			this.clean();
			_transformComponent = null;
			_tags.clear();
			_tags = null;
			_components.clear();
			_components = null;
			_destroyStackQueue.clear();
			_destroyStack.clear();
			_destroyStackQueue = null;
			_destroyStack = null;
		}
		else
		{
			EntitySystem.getInstance().destroyEntity(this);
		}
	}
	
	/**
	 * Returns true if this entity has been destroyed this frame, else false
	 * @return true if this entity has been destroyed this frame, else false
	 */
	public boolean isDestroyed()
	{
		return _isDestroyed;
	}
	
	/**
	 * Is called when the Entity is instantiated. 
	 */
	protected abstract void awake();
	
	/**
	 * Will be called every frame for as long as the entity is active.
	 * @param dt equals the delta time which has passed between the frames
	 */
	protected abstract void updated(float dt);
	
	/**
	 * Is called every render update. This method can be used to draw things which are not entities.
	 * NOTE: This method will only be called if this entity has a RenderComponent attached to it!
	 * @param renderComponents which contains all the render informations
	 */
	protected abstract void rendered(RenderComponents renderComponents);
	
	/**
	 * Will be called when the entity is in its final destroy phase
	 */
	protected abstract void destroyed();
	
	/**
	 * Removes the given component from the Entity and destroys that component.
	 * @param component to remove.
	 */
	private void removeComponent(BaseEntityComponent component)
	{
		_components.remove(component);
		component.destroy();
	}
}
