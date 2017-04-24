package com.mygdx.game.engine.entities;

import com.mygdx.game.engine.events.Event;

/**
 * This Event is an event which also contains an Entity.
 * All Events regarding Entities use this event or an extension of it.
 * @author Ramses Di Perna
 *
 */
public class EntityEvent extends Event 
{
	private BaseEntity _entity;
	
	public EntityEvent(String type, BaseEntity entity) 
	{
		super(type);
		_entity = entity;
	}
	
	/**
	 * Returns the Entity linked to the event.
	 * @return Returns the Entity which was linked into the EntityEvent Constructor
	 */
	public BaseEntity getEntity()
	{
		return _entity;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		_entity = null;
	}
}
