package com.mygdx.game.engine.globals;

/**
 * A class containing static final variables which are used by the Engine
 * This includes Entity calls and Component events
 * @author Ramses Di Perna
 *
 */
public final class EngineGlobals 
{
	/**
	 * Event type for when an entity is created (Global event)
	 */
	public static final String GLOBAL_EVENT_ENTITY_CREATED = "EntityCreated";
	
	/**
	 * Event type for when an entity is destroyed (Global event)
	 */
	public static final String GLOBAL_EVENT_ENTITY_DESTROYED = "EntityDestroyed";
	
	/**
	 * Event type for when a component is created (Global event)
	 */
	public static final String GLOBAL_EVENT_COMPONENT_CREATED = "ComponentCreated";
	
	/**
	 * Event type for when a component is destroyed (Global event)
	 */
	public static final String GLOBAL_EVENT_COMPONENT_DESTROYED = "ComponentDestroyed";
	
	/**
	 * Event type for when collision has started (Collision Component Event)
	 */
	public static final String COLLISION_EVENT_COLLISION_ENTER = "CollisionStarted";
	
	/**
	 * Event type for when collision has stopped (Collision Component Event)
	 */
	public static final String COLLISION_EVENT_COLLISION_EXIT = "CollisionStopped";
}
