package com.mygdx.game.resources;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.components.CollisionComponent;
import com.mygdx.game.entities.components.ComponentEvent;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.GlobalDispatcher;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.globals.EngineGlobals;

/**
 * This class handles the Physics of the 'world'. Right now it will only handle Collision. 
 * It will create the bodies needed for the entities for collision to work.
 * @author Djamali
 *
 */
public class PhysicsWorld implements IEventReceiver
{
	private World _world;
	private BodyDef _bodyDef;
	
	private ArrayList<CollisionComponent> _allCollisionComponents = new ArrayList<CollisionComponent>();
	private ArrayList<Body> _allBodies = new ArrayList<Body>();
	
	public PhysicsWorld()
	{
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
		_world = new World(null, false);
		_bodyDef = new BodyDef();
		_bodyDef.type = BodyDef.BodyType.KinematicBody;
		this.createBodies();
	}
	
	/**
	 * Cleans this class. This method clears everything that is no longer needed for this class when called.
	 */
	public void clean()
	{
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED, this);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{	
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_COMPONENT_CREATED)
		{
			onComponentCreatedEvent(event);
		}
	}
	
	/**
	 * When a component is created it will be registered and added to the components list.
	 * @param e
	 */
	private void onComponentCreatedEvent(Event e)
	{
		ComponentEvent event = (ComponentEvent)e;
		registerComponent((CollisionComponent)event.getComponent());
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
		}
	}
	
	/**
	 * Creates Bodies for every registered CollisionComponent.
	 */
	private void createBodies()
	{
		for(int i = 0; i < _allCollisionComponents.size(); i++)
		{		
			if(_allBodies.contains(_allCollisionComponents.get(i).getBody())) { return; }
			else {
				_allCollisionComponents.get(i).setBody(_world.createBody(_bodyDef));
				_allBodies.add(_allCollisionComponents.get(i).getBody());
			}
		}
	}
	
}