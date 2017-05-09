package com.mygdx.game.engine.resources;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.EntitySystem;
import com.mygdx.game.engine.entities.components.ComponentEvent;
import com.mygdx.game.engine.entities.components.TransformComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionComponentListener;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.GlobalDispatcher;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class handles the Physics of the 'world'. Right now it will only handle Collision. 
 * It will create the bodies needed for the entities for collision to work.
 * @author Djamali
 *
 */
public class PhysicsWorld implements IEventReceiver
{
	private World _world;
	
	private Box2DDebugRenderer _debugRenderer;
	private OrthographicCamera _physicsCam;
	
	private ArrayList<CollisionComponent> _allCollisionComponents = new ArrayList<CollisionComponent>();
	private Stack<Body> _bodiesToDelete = new Stack<Body>();
	private Stack<CollisionComponent> _bodiesToCreate = new Stack<CollisionComponent>();
	
	public PhysicsWorld()
	{
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED, this);
		_world = new World(new Vector2(0, 0), false);
		_world.setContactListener(new CollisionComponentListener());
		_physicsCam = new OrthographicCamera();
		_physicsCam.setToOrtho(false, CollisionResources.convertToPPM((float)Engine.getWidth()), CollisionResources.convertToPPM((float)Engine.getHeight()));
		
		_debugRenderer = new Box2DDebugRenderer();
		checkAllExistingCollisionComponents();
	}
	
	public void update(float dt)
	{
		CollisionComponent cc;
		TransformComponent tc;
		for(int i = _allCollisionComponents.size() - 1; i >= 0; i--)
		{
			cc = _allCollisionComponents.get(i);
			if(cc.getBody() != null && cc.getVelocity().len() == 0)
			{
				tc = cc.getParentOfComponent().getTransformComponent();
				cc.getBody().setTransform(CollisionResources.convertToPPM(tc.getPositionX()), CollisionResources.convertToPPM(tc.getPositionY()), (float) Math.toRadians(-tc.getRotation()));
				
				if(cc.getBody().isActive() != cc.isActive())
				{
					cc.getBody().setActive(cc.isActive());
				}
			}
		}
		
		_world.step(dt, 6, 2);
		
		deleteDeadBodies();
		createNewBodies();
	}

	/**
	 * Returns the world of the PhysicsWorld
	 * @return World instance created by this PhysicsWorld class 
	 */
	public World getWorld()
	{
		return _world;
	}
	
	public void render(RenderComponents rcs)
	{
		rcs.getSpriteBatch().setProjectionMatrix(_physicsCam.combined);
		rcs.getSpriteBatch().begin();
		_debugRenderer.render(_world, _physicsCam.combined);
		rcs.getSpriteBatch().end();
	}
	
	/**
	 * This method checks if the existing CollisionComponent has a Body or not.
	 * If not it will create one.
	 */
	private void checkAllExistingCollisionComponents() 
	{
		ArrayList<BaseEntity> entities = EntitySystem.getInstance().getAllEntities();
		CollisionComponent c = new CollisionComponent();
		
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).getComponent(CollisionComponent.class) != null)
			{
				if(entities.get(i).getComponent(CollisionComponent.class).getBody() != null) { return; }
				else
				{
					c = entities.get(i).getComponent(CollisionComponent.class);
					createBodyForComponent(c);
				}
			}
			else { return; }
		}
	}

	/**
	 * Creates a 'Body' for parameter c, a 'CollisionComponent'
	 * @param c
	 */
	private void createBodyForComponent(CollisionComponent c) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		c.setBody(_world.createBody(bodyDef));
	}

	/**
	 * Cleans this class. This method clears everything that is no longer needed for this class when called.
	 */
	public void clean()
	{
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED, this);
		deleteDeadBodies();
		
		_world = null;
		_debugRenderer = null;
		_physicsCam = null;
		_allCollisionComponents.clear();
		_allCollisionComponents = null;
		_bodiesToDelete.clear();
		_bodiesToDelete = null;
		
		_bodiesToCreate.clear();
		_bodiesToCreate = null;
	}

	@Override
	public void onReceiveEvent(Event event) 
	{	
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED)
		{
			onComponentCreatedEvent(event);
		}
		
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED)
		{
			onComponentDestroyedEvent(event);
		}
	}
	
	/**
	 * When a component is created it will be registered and added to the components list.
	 * @param e
	 */
	private void onComponentCreatedEvent(Event e)
	{
		ComponentEvent event = (ComponentEvent)e;
		
		if(event.getComponent().getClass() == CollisionComponent.class)
		{
			registerComponent((CollisionComponent)event.getComponent());
		}
	}
	
	/**
	 * When a component is destroyed it will be deregistered and removed from the components list.
	 * @param e
	 */
	private void onComponentDestroyedEvent(Event e)
	{
		ComponentEvent event = (ComponentEvent)e;
		if(event.getComponent().getClass() == CollisionComponent.class)
		{
			deregisterComponent((CollisionComponent)event.getComponent());
			_bodiesToDelete.push(((CollisionComponent)event.getComponent()).getBody());
			((CollisionComponent)event.getComponent()).setBody(null);
		}
	}
	
	private void deleteDeadBodies()
	{
		while(!_bodiesToDelete.isEmpty())
			_world.destroyBody(_bodiesToDelete.pop());
	}
	
	private void createNewBodies()
	{
		while(!_bodiesToCreate.isEmpty())
		{
			CollisionComponent cc = _bodiesToCreate.pop();
			if(cc.isDestroyed()) { continue; }
			createBodyForComponent(cc);
		}
	}
	
	/**
	 * Registers a new component as an active one in the system.
	 * @param component
	 */
	private void registerComponent(CollisionComponent component)
	{
		if(component.getClass() == CollisionComponent.class)
		{
			if(_allCollisionComponents.contains(component)) { return; }
			_allCollisionComponents.add(component);
			_bodiesToCreate.add(component);
		}
	}
	
	/**
	 * Deregisters a component. It will remove from the system's list.
	 * @param component
	 */
	private void deregisterComponent(CollisionComponent component)
	{
		if(component.getClass() == CollisionComponent.class)
		{
			if(_allCollisionComponents.contains(component))
			{
				_allCollisionComponents.remove(component);
			}
		}
	}
	
}