package com.mygdx.game.hitRegistration;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.events.Event;

/**
 * This event represents a hit registration event and holds the entity which got hit, what kind of types the hit was and what the tool was which hit it.
 * @author Ramses Di Perna
 *
 */
public class HitRegistrationEvent extends Event
{
	private BaseEntity _hitEntity;
	private int _hitTool;
	private int[] _hitTypes;
	
	/**
	 * Repairs the event for launch.
	 * @param type indicates the type of event
	 * @param hitEntity is the entity which got hit
	 * @param hitTool represents the object which hit the entity
	 * @param hitTypes represents the types this hit was (Explosion, fire etc.)
	 */
	public HitRegistrationEvent(String type, BaseEntity hitEntity, int hitTool, int[] hitTypes) 
	{
		super(type);
		_hitEntity = hitEntity;
		_hitTool = hitTool;
		_hitTypes = hitTypes;
	}
	
	public BaseEntity getHitEntity()
	{
		return _hitEntity;
	}
	
	/**
	 * Returns the tool which hit the entity
	 * @return integer which represents the type of tool which was used
	 */
	public int getHitTool()
	{
		return _hitTool;
	}
	
	/**
	 * Returns the hit types which hit the entity
	 * @return integers which represent the types of hits which hit the entity 
	 */
	public int[] getHitTypes()
	{
		return _hitTypes;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		_hitEntity = null;
	}
}
