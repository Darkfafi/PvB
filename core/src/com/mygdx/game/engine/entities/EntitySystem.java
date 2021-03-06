package com.mygdx.game.engine.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.GlobalDispatcher;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This is a singleton class which handles all the entity handling.
 * By its instance you can find active entities by tags and types.
 * IMPORTANT: This class must be updated in order for all the entities to update and their components.
 * @author Ramses Di Perna
 *
 */
public class EntitySystem implements IEventReceiver
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
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_ENTITY_CREATED, this);
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
			if(_allEntities.size() <=  i) { return; }
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
		ArrayList<RenderComponent> rcs = (ArrayList<RenderComponent>) getSortedRenderElements();
		ArrayList<RenderComponent> uiRcs = new ArrayList<RenderComponent>();
		SpriteBatch sb = gameRenderComponents.getSpriteBatch();
		
		sb.setProjectionMatrix(gameRenderComponents.getMainCamera().combined);
		sb.begin();
		for(int i = 0; i < rcs.size(); i++)
		{
			if(!rcs.get(i).isUserInterface())
			{
				renderRenderComponent(rcs.get(i), gameRenderComponents);
			}
			else
			{
				uiRcs.add(rcs.get(i));
			}
		}
		sb.setProjectionMatrix(gameRenderComponents.getHudCamera().combined);
		for(int i = 0; i < uiRcs.size(); i++)
		{
			renderRenderComponent(uiRcs.get(i), gameRenderComponents);
		}
		sb.end();
	}
	
	/**
	 * Destroys all currently active entities.
	 */
	public void destroyAllEntities()
	{
		for(int i = _allEntities.size() - 1; i >= 0; i--)
		{
			this.destroyEntity(_allEntities.get(i));
		}
	}
	
	/**
	 * Destroys all currently active entities this exact frame.
	 */
	public void destroyAllEntitiesImmediately()
	{
		for(int i = _allEntities.size() - 1; i >= 0; i--)
		{
			_destroyStack.add(_allEntities.get(i));
			this.internalDestroyEntity(_allEntities.get(i));
		}
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
	 * Destroys the given entity this exact frame.
	 */
	public void destroyEntityImmediately(BaseEntity entity)
	{
		_destroyStack.add(entity);
		this.internalDestroyEntity(entity);
	}
	
	@Override
	public void onReceiveEvent(Event event) {
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_ENTITY_CREATED)
		{
			onEntityCreatedEvent(event);	
		}
	}
	
	/**
	 * Gets and returns all the existing entities
	 * @return All the entities in the '_allEntities' ArrayList
	 */
	public ArrayList<BaseEntity> getAllEntities()
	{
		return _allEntities;
	}
	
	// Getting entities
	/**
	 * Returns the first entity active with the given tag
	 * @param tag which the entity must have
	 * @return the entity with the given tag
	 */
	public BaseEntity getEntityByTag(String tag)
	{
		for(int i = 0; i < _allEntities.size(); i++)
		{
			if(_allEntities.get(i).hasTag(tag))
				return _allEntities.get(i);
		}
		
		return null;
	}
	
	/**
	 * Returns all entities active with the given tag
	 * @param tag which all the returning entities must have
	 * @return all the entities with the given tag
	 */
	public BaseEntity[] getEntitiesByTag(String tag)
	{
		Collection<BaseEntity> entities = new ArrayList<BaseEntity>();
		
		for(int i = 0; i < _allEntities.size(); i++)
		{
			if(_allEntities.get(i).hasTag(tag))
				entities.add(_allEntities.get(i));
		}
		
		return (BaseEntity[]) entities.toArray();
	}
	
	/**
	 * Returns an entity of the given class. (Does not count for extending the given class)
	 * WARNING: This method looks at the direct class! So everything which inherits the given class is not returned! 
	 * @param directClass which the entity is to return.
	 * @return the entity which is of the same class given. 
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T getEntityByClass(Class<T> directClass)
	{
		for(int i = 0; i < _allEntities.size(); i++)
		{
			if(_allEntities.get(i).getClass() == directClass)
				return (T) _allEntities.get(i);
		}
		
		return null;
	}
	
	/**
	 * Returns all entities of the given class. (Does not count for extending the given class)
	 * WARNING: This method looks at the direct class! So everything which inherits the given class is not returned! 
	 * @param directClass which all the entities are to return.
	 * @return the entities which are of the same class given. 
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Collection<T> getEntitiesByClass(Class<T> directClass)
	{
		Collection<T > entities = new ArrayList<T>();
		for(int i = 0; i < _allEntities.size(); i++)
		{
			if(_allEntities.get(i).getClass() == directClass)
				entities.add((T) _allEntities.get(i));
		}
		
		return entities;
	}
	
	private void renderRenderComponent(RenderComponent rc, RenderComponents gameRenderComponents)
	{
		SpriteBatch sb = gameRenderComponents.getSpriteBatch();
		RenderInfo ri = rc.getRenderInfo();
		BaseEntity ce = rc.getParentOfComponent();
		
		if(!rc.getCallsRenderAfterEntityRender())
			ce.render(gameRenderComponents);
		
		sb.setColor(rc.getColor());
		if(ri != null)
			sb.draw(
					ri.getTextureToDraw(),
					ce.getTransformComponent().getPositionX() - (rc.getRealWidth() * rc.getPivotX()) +  (ri.getInnerOffset().x * rc.getRealWidth()),  	/* x the x-coordinate in screen space                                            */
					ce.getTransformComponent().getPositionY() - (rc.getRealHeight() * rc.getPivotY())+  (ri.getInnerOffset().y * rc.getRealHeight()),  	/* y the y-coordinate in screen space                                            */
				    rc.getRealWidth() * (1 - rc.getPivotX()),          /* originX the x-coordinate of the scaling and rotation origin relative to the screen space coordinates   */
				    rc.getRealHeight() * (rc.getPivotY()),         /* originY the y-coordinate of the scaling and rotation origin relative to the screen space coordinates   */
				    rc.getRealWidth(),           			 		/* width the width in pixels                                                     */
				    rc.getRealHeight(),				     	    	/* height the height in pixels                                                   */
				    1,    										 	/* scaleX the scale of the rectangle around originX/originY in x                 */
				    1,     											/* scaleY the scale of the rectangle around originX/originY in y                 */
				    360 - ce.getTransformComponent().getRotation(), /* rotation the angle of counter clockwise rotation of the rectangle around originX/originY               */
				    ri.getStartX(rc.getCurrentFrameInfo()),     /* srcX the x-coordinate in texel space                                          */
				    ri.getStartY(rc.getCurrentFrameInfo()),     /* srcY the y-coordinate in texel space 										 */
				    ri.getCutWidth(rc.getCurrentFrameInfo()),   /* srcWidth the source with in texels                                            */
				    ri.getCutHeight(rc.getCurrentFrameInfo()), 	/* srcHeight the source height in texels                                         */
				    rc.getFlipX(),                   			/* flipX whether to flip the sprite horizontally                                 */
				    rc.getFlipY());                  			/* flipY whether to flip the sprite vertically   								 */
		
		sb.setColor(1, 1, 1, 1);
		
		if(rc.getCallsRenderAfterEntityRender())
			ce.render(gameRenderComponents);
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
	private void onEntityCreatedEvent(Event e)
	{
		EntityEvent event = (EntityEvent)e;
		registerEntity(event.getEntity());
	}
	
	/**
	 * Gets the RenderComponent for all entities and sorts them on layer
	 * @return Layer sorted list of RenderComponent elements for each active entity 
	 */
	private Collection<RenderComponent> getSortedRenderElements()
	{
		List<RenderComponent> rcs = new ArrayList<RenderComponent>();
		BaseEntity ce;
		RenderComponent rc;
		for(int i = 0; i < _allEntities.size(); i++)
		{
			ce = _allEntities.get(i);
			rc = ce.getComponent(RenderComponent.class);
			if(rc == null) 
			{ 
				rc = ce.getComponent(AnimationComponent.class);
				if(rc == null){ continue; }
			}
			
			if(!rc.isActive()) { continue; }
			
			rcs.add(rc);
		}
		if(rcs.size() > 0)
			Collections.sort(rcs);
		
		return rcs;
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
