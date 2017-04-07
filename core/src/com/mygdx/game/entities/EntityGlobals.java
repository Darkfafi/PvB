package com.mygdx.game.entities;

/**
 * A class containing static final variables which are used by the EntitySystem.
 * @author Ramses Di Perna
 *
 */
public final class EntityGlobals 
{
	/**
	 * Event type for when an entity is created (Global event)
	 */
	public static final String GLOBAL_EVENT_ENTITY_CREATED = "EntityCreated";
	
	/**
	 * Event type for when an entity is destroyed (Global event)
	 */
	public static final String GLOBAL_EVENT_ENTITY_DESTROYED = "EntityDestroyed";
}
