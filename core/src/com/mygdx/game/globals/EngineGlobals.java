package com.mygdx.game.globals;

/**
 * A class containing static final variables which are used by the EntitySystem.
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
	 * Event type for when collision has started (Global Event)
	 */
	public static final String GLOBAL_EVENT_COLLISION_ENTER = "CollisionStarted";
	
	/**
	 * Event type for when collision has stopped (Global Event)
	 */
	public static final String GLOBAL_EVENT_COLLISION_EXIT = "CollisionStopped";
}
