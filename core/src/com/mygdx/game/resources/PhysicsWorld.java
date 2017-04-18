package com.mygdx.game.resources;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.EntitySystem;
import com.mygdx.game.entities.components.ComponentEvent;
import com.mygdx.game.entities.components.TransformComponent;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.entities.components.collision.CollisionComponentListener;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.GlobalDispatcher;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.globals.EngineGlobals;
import com.mygdx.game.scenes.RenderComponents;

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
	
	public PhysicsWorld()
	{
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED, this);
		_world = new World(new Vector2(0, 0), false);
		_world.setContactListener(new CollisionComponentListener());
		_physicsCam = new OrthographicCamera();
		_physicsCam.setToOrtho(false, CollisionResources.convertToPPM((float)MyGdxGame.WIDTH), CollisionResources.convertToPPM((float)MyGdxGame.HEIGHT));
		
		_debugRenderer = new Box2DDebugRenderer();
		checkAllExistingCollisionComponents();
	}
	
	public void update()
	{
		TransformComponent tc;
		for(int i = _allCollisionComponents.size() - 1; i >= 0; i--)
		{
			if(_allCollisionComponents.get(i).getBody() != null)
			{
				tc = _allCollisionComponents.get(i).getParentOfComponent().getTransformComponent();
				_allCollisionComponents.get(i).getBody().setTransform(CollisionResources.convertToPPM(tc.getPositionX()), CollisionResources.convertToPPM(tc.getPositionY()), (float) Math.toRadians(-tc.getRotation()));
			}
		}
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
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		c.setBody(_world.createBody(bodyDef));
	}

	/**
	 * Cleans this class. This method clears everything that is no longer needed for this class when called.
	 */
	public void clean()
	{
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_DESTROYED, this);
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
	
	private void onComponentDestroyedEvent(Event e)
	{
		ComponentEvent event = (ComponentEvent)e;
		if(event.getComponent().getClass() == CollisionComponent.class)
		{
			deregisterComponent((CollisionComponent)event.getComponent());
			_world.destroyBody(((CollisionComponent)event.getComponent()).getBody());
			((CollisionComponent)event.getComponent()).setBody(null);
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
			createBodyForComponent(component);
		}
	}
	
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