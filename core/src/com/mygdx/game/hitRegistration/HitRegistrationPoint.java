package com.mygdx.game.hitRegistration;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.events.EventDispatcher;

/**
 * This class is a singleton where all the hits of the game should be registered to. 
 * This is the center-point where all hit events can be listened to by other classes.
 * @author Ramses Di Perna
 *
 */
public class HitRegistrationPoint extends EventDispatcher
{
	public static final String HIT_REGISTRATED_EVENT = "HitRegistrationEvent";
	
	private static HitRegistrationPoint _instance;
	
	public static HitRegistrationPoint getInstance()
	{
		if(_instance == null)
			_instance = new HitRegistrationPoint();
		
		return _instance;
	}
	
	/**
	 * This registers a hit which dispatches the HIT_REGISTRATED_EVENT type event which other classes can listen to.
	 * @param hitEntity is the entity which was hit. (This may be set as 'null' if you want to call a ground hit for example)
	 * @param toolType indicates the object which hit the entity (Check HitGlobals for all the tools)
	 * @param hitTypes indicates the types of hits these presented (Explosion, Fire etc) (Check HitGlobals for all the types)
	 */
	public void register(BaseEntity hitEntity, int toolType, int[] hitTypes)
	{
		this.dispatchEvent(new HitRegistrationEvent(HIT_REGISTRATED_EVENT, hitEntity, toolType, hitTypes));
	}
	
	/**
	 * This registers a hit which dispatches the HIT_REGISTRATED_EVENT type event which other classes can listen to.
	 * @param hitEntity is the entity which was hit. (This may be set as 'null' if you want to call a ground hit for example)
	 * @param toolType indicates the object which hit the entity (Check HitGlobals for all the tools)
	 * @param hitType indicates the type of hit this presented (Explosion, Fire etc) (Check HitGlobals for all the types)
	 */
	public void register(BaseEntity hitEntity, int toolType, int hitType)
	{
		register(hitEntity, toolType, new int[]{hitType});
	}
}
