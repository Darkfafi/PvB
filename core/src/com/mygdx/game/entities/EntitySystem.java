package com.mygdx.game.entities;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entities.components.RenderComponent;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.EventDispatcher;
import com.mygdx.game.events.GlobalDispatcher;
import com.mygdx.game.scenes.RenderComponents;

/**
 * This is a singleton class which handles all the entity handling.
 * By its instance you can find active entities by tags and types.
 * IMPORTANT: This class must be updated in order for all the entities to update and their components.
 * @author Ramses Di Perna
 *
 */
public class EntitySystem 
{
	private static EntitySystem _instance;
	
	// Current active entities.
	private ArrayList<BaseEntity> _allEntities = new ArrayList<BaseEntity>();
	
	// Destroying entities.
	private Stack<BaseEntity> _destroyStackQueue = new Stack<BaseEntity>();
	private Stack<BaseEntity> _destroyStack = new Stack<BaseEntity>();
	
	public static EntitySystem getInstance()
	{
		if(_instance == null)
		{
			_instance = new EntitySystem();
		}
		
		return _instance;
	}
	
	public EntitySystem()
	{
		GlobalDispatcher.getInstance().addEventListener(EntityGlobals.GLOBAL_EVENT_ENTITY_CREATED, EventDispatcher.getEventMethodData("onEntityCreatedEvent", this));
	}
	
	/**
	 * MUST BE CALLED TO UPDATE ALL ENTITIES
	 * Without this being called in the loop the system does not work.
	 * @param dt is the delta time between this frame and the last.
	 */
	public void updateEntities(float dt)
	{
		if(_allEntities.size() == 0){return;}
		
		while(!_destroyStack.isEmpty())
			internalDestroyEntity(_destroyStack.peek());
		
		for(int i = _allEntities.size() - 1; i >= 0; i--)
		{
			if(!_destroyStackQueue.contains(_allEntities.get(i)))
				_allEntities.get(i).update(dt);
			
			if(!_destroyStackQueue.isEmpty())
				_destroyStack.push(_destroyStackQueue.pop());
		}
	}
	
	/**
	 * MUST BE CALLED TO RENDER ALL ENTITIES
	 * Renders all the entities with the 'RenderComponent' which are active.
	 * @param gameRenderComponents which are used to render the entity
	 */
	public void renderEntities(RenderComponents gameRenderComponents)
	{
		RenderComponent rc = null;
		BaseEntity ce = null;
		SpriteBatch sb = gameRenderComponents.getSpriteBatch();
		sb.begin();
		for(int i = _allEntities.size() - 1; i >= 0; i--)
		{
			ce = _allEntities.get(i);
			rc = ce.getComponent(RenderComponent.class);
			
			if(rc == null) { continue; }
			
			sb.draw(rc.getCurrentTexture(), ce.getTransformComponent().getPositionX(), ce.getTransformComponent().getPositionY());
			
		}
		sb.end();
	}
	
	/**
	 * Checks of the entity is ready for destruction next frame.
	 * @param entity to check if its ready for destruction next frame.
	 * @return whether the entity is in the destroyStack.
	 */
	public boolean isEntityInDestroyStack(BaseEntity entity)
	{
		return _destroyStack.contains(entity);
	}
	
	/**
	 * Destroys the given entity.
	 * @param entity to destroy
	 */
	public void destroyEntity(BaseEntity entity)
	{
		if(!_destroyStackQueue.contains(entity))
			_destroyStackQueue.add(entity);
	}
	
	/**
	 * Does the real destruction of the entity
	 * @param entity to destroy
	 */
	private void internalDestroyEntity(BaseEntity entity)
	{
		this.unregisterEntity(entity);
		entity.destroy();
		_destroyStack.remove(entity);
	}
	
	/**
	 * Adds the entity which has fired the event to the entity list.
	 * @param e is the event which was caught.
	 */
	@SuppressWarnings("unused")
	private void onEntityCreatedEvent(Event e)
	{
		EntityEvent event = (EntityEvent)e;
		registerEntity(event.getEntity());
		GlobalDispatcher.getInstance().dispatchEvent(new EntityEvent(EntityGlobals.GLOBAL_EVENT_ENTITY_DESTROYED, event.getEntity()));
	}
	
	/**
	 * Registers the entity as an active entity in the system.
	 * @param entity to register.
	 */
	private void registerEntity(BaseEntity entity) 
	{
		if(_allEntities.contains(entity)){ return; }
		_allEntities.add(entity);
	}
	
	/**
	 * Unregisters the entity so it will be deactivated from the system
	 * @param entity to unregister.
	 */
	private void unregisterEntity(BaseEntity entity) 
	{
		if(!_allEntities.contains(entity)){ return; }
		_allEntities.remove(entity);
	}
}
